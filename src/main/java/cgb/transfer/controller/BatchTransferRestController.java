package cgb.transfer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import cgb.transfer.dto.TransferRequest;
import cgb.transfer.entity.Transfer;
import cgb.transfer.service.TransferService;
import cgb.transfer.exception.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/batch")
public class BatchTransferRestController {

	@Autowired 
	private TransferService transferService;
	
	@PostMapping
	public ResponseEntity<?> createBatchTransfer(@RequestBody List<TransferRequest> listTransfers) {
		try {
			ArrayList<Transfer> list = new ArrayList<Transfer>();
			for (TransferRequest transferRequest: listTransfers) {
		    	Transfer transfer = transferService.createTransfer(
		                transferRequest.getSourceAccountNumber(),
		                transferRequest.getDestinationAccountNumber(),
		                transferRequest.getAmount(),
		                transferRequest.getTransferDate(),
		                transferRequest.getDescription()
		    			);
		    	list.add(transfer);
			}
			return ResponseEntity.ok(list);
		} catch(Exception e) {
            TransferResponse errorResponse = new TransferResponse("FAILURE", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
		}
	}
	
	
	
}
