package cgb.transfer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import cgb.transfer.dto.TransferRequest;
import cgb.transfer.entity.BatchTransfer;
import cgb.transfer.entity.State;
import cgb.transfer.entity.Transfer;
import cgb.transfer.exception.*;
import cgb.transfer.repository.AccountRepository;
import cgb.transfer.repository.BatchTransferRepository;
import cgb.transfer.repository.TransferRepository;
import java.time.LocalDate;
import java.util.List;

/**
 * La classe de Service permettant le lien entre Repository et Controller.
 */
@Service
public class BatchTransferService {
	
	@Autowired
	private AccountRepository accountRepository;
	
	@Autowired
	private BatchTransferRepository batchTransferRepository;
	
	@Autowired
	private TransferRepository transferRepository;
	
	@Autowired
	private TransferService transferService;
	
	@Async
	public BatchTransfer createBatchTransfer(String refLot, String sourceAccountNumber, String description, List<TransferRequest> listTransfers) throws InvalidAccountException, NegativeTransferAmountException, DateTransferException, InsufficientFundsException {
		BatchTransfer batch = new BatchTransfer();
		batch.setRefLot(refLot);
		batch.setSourceAccountNumber(sourceAccountNumber);
		batch.setDescription(description);
		batch.setDate(LocalDate.now());
		batch.setState(State.RECEIVED.getNom());
		
		if (!accountRepository.findById(sourceAccountNumber).isPresent()) {
			throw new InvalidAccountException("Source");
		}
		
		for (TransferRequest transferRequest: listTransfers) {
			Transfer transfer = transferService.createTransfer(sourceAccountNumber,
					transferRequest.getDestinationAccountNumber(),
					transferRequest.getAmount(),
					LocalDate.now(),
					description);
			transfer.setBatch_id(batch);
			transferRepository.save(transfer);
		}
		
		batch.setState(State.CLOSED.getNom());
		
		return batchTransferRepository.save(batch);
	}

}
