package cgb.transfer.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import cgb.transfer.dto.BatchTransferRequest;
import cgb.transfer.entity.BatchTransfer;
import cgb.transfer.entity.Transfer;
import cgb.transfer.service.BatchTransferService;
import cgb.transfer.service.TransferService;
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

	@Autowired
	/**
	 * Lien au Service de gestion des transferts.
	 */
	private TransferService transferService;

	@PostMapping("/async")
	/**
	 * Fonction de gestion de la création d'un transfert par lot.
	 * 
	 * @param transferRequest L'objet JSON envoyé dans le corps de la requête POST
	 * @return L'objet JSON correspondant au transfer s'il est valide. Une réponse
	 *         'BAD_REQUEST' si une RuntimeException est rencontrée.
	 */
	public ResponseEntity<?> createTransfer(@RequestBody BatchTransferRequest batchTransferRequest)
			throws InvalidAccountException, NegativeTransferAmountException, DateTransferException,
			InsufficientFundsException, IOException {
		try {
			String refLot = batchTransferService.generateRefLot();
			
			batchTransferService.createBatchTransfer(batchTransferRequest.getSourceAccountNumber(),
					batchTransferRequest.getDescription(),
					batchTransferRequest.getListTransfers());

			Map<String, Object> response = new HashMap<String, Object>();
			response.put("refLot", refLot);
			response.put("dateLancement", LocalDate.now());
			response.put("message", "Traitement lancé");
			response.put("etat", "received");

			return ResponseEntity.ok().body(response);
		} catch (RuntimeException e) {
			TransferResponse errorResponse = new TransferResponse("FAILURE", e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
		}
	}
	
	@DeleteMapping
	/**
	 * Fonction de suppression d'un transfert par lot dans la BDD.
	 * 
	 * @param id  L'identifiant unique du transfert par lot à supprimer, 
	 * 			  renseigné dans le corps de la requête.
	 * @return  L'objet supprimé si l'opération a réussi.
	 * 			Une réponse 'BAD_REQUEST' si une RuntimeException 
	 * 			ou une DeleteTransferException est rencontrée.
	 */
	public ResponseEntity<?> deleteBatchTransfer(@RequestBody Long id) {
		try {
			BatchTransfer batch = batchTransferService.deleteBatchTransfer(id);
			System.out.println(batch);
			TransferResponse succesResponse = new TransferResponse("SUCCESS", batch.toString());
			return ResponseEntity.ok(succesResponse);
		}catch (RuntimeException | DeleteTransferException e) {
			TransferResponse errorResponse = new TransferResponse("FAILURE", e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
		} 
	}

	@GetMapping("/{refLot}")
	public ResponseEntity<?> getTransfer(@PathVariable String refLot) {
		BatchTransfer batch = batchTransferService.findBatchByRefLot(refLot);
        List<Transfer> list = transferService.getTransferFromBatch(refLot);
        batch.setListTransfers(list);

        return ResponseEntity.ok(batch);
	}

}
