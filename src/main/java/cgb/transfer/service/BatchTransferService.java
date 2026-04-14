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
import jakarta.transaction.Transactional;

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
	@Transactional
	public void createBatchTransfer(String sourceAccountNumber, String description, List<TransferRequest> listTransfers) throws InvalidAccountException, NegativeTransferAmountException, DateTransferException, InsufficientFundsException {
		BatchTransfer batch = new BatchTransfer();
		batch.setRefLot(generateRefLot());
		batch.setSourceAccountNumber(sourceAccountNumber);
		batch.setDescription(description);
		batch.setDate(LocalDate.now());
		batch.setState(State.RECEIVED.getNom());
		batchTransferRepository.save(batch);
		
		if (!accountRepository.findById(sourceAccountNumber).isPresent()) {
			throw new InvalidAccountException("Source");
		}
		
		for (TransferRequest transferRequest: listTransfers) {
			Transfer transfer = transferService.createTransferForBatch(sourceAccountNumber,
					transferRequest.getDestinationAccountNumber(),
					transferRequest.getAmount(),
					LocalDate.now(),
					description);
			transfer.setBatch_id(batch);
			batch.addTransfer(transfer);
			transferRepository.save(transfer);
			batchTransferRepository.save(batch);
		}
		
		batch.setState(State.CLOSED.getNom());
		
		batchTransferRepository.save(batch);
	}
	
	private int countBatchTransfers(LocalDate date) {
		return batchTransferRepository.countBatchTransfers(date);
	}
	
	public String generateRefLot() {
		return LocalDate.now().toString() + "-" + (countBatchTransfers(LocalDate.now()) + 1);
	}

}
