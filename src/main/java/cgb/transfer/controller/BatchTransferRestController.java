package cgb.transfer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import cgb.transfer.dto.BatchTransferRequest;
import cgb.transfer.entity.BatchTransfer;
import cgb.transfer.service.BatchTransferService;
import cgb.transfer.exception.*;


/**
 * Classe de gestion de la route pour la gestion des transferts par lot.
 */
@RestController
@RequestMapping("/api/batch")
public class BatchTransferRestController {

	@Autowired
	/**
	 * Lien au Service de gestion des transferts par lot.
	 */
	private BatchTransferService batchTransferService;

	@PostMapping("/async")
	/**
	 * Fonction de gestion de la création d'un transfert par lot.
	 * 
	 * @param transferRequest  L'objet JSON envoyé dans le corps de la requête POST
	 * @return  L'objet JSON correspondant au transfer s'il est valide.
	 * 			Une réponse 'BAD_REQUEST' si une RuntimeException est rencontrée.
	 */
	public ResponseEntity<?> createTransfer(@RequestBody BatchTransferRequest batchTransferRequest) throws InvalidAccountException {
		try {
			BatchTransfer batch = batchTransferService.createBatchTransfer(
					batchTransferRequest.getRefLot(),
					batchTransferRequest.getSourceAccountNumber(),
					batchTransferRequest.getDescription(),
					batchTransferRequest.getListTransfers()
					);
			return ResponseEntity.ok(batch);
		} catch(RuntimeException e) {
			TransferResponse errorResponse = new TransferResponse("FAILURE", e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
		}
	}

}
