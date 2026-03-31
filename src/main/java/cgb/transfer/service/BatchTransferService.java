package cgb.transfer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import cgb.transfer.dto.TransferRequest;
import cgb.transfer.entity.Account;
import cgb.transfer.entity.BatchTransfer;
import cgb.transfer.entity.Transfer;
import cgb.transfer.exception.*;
import cgb.transfer.exception.DeleteTransferException.FailureTransfert;
import cgb.transfer.repository.AccountRepository;
import cgb.transfer.repository.BatchTransferRepository;
import cgb.transfer.repository.TransferRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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
	
	@Async
	public BatchTransfer createBatchTransfer(String refLot, String sourceAccountNumber, String description, List<TransferRequest> listTransfers) throws InvalidAccountException {
		if (!accountRepository.findById(sourceAccountNumber).isPresent()) {
			throw new InvalidAccountException("Source");
		}

		BatchTransfer batch = new BatchTransfer();
		batch.setRefLot(refLot);
		batch.setSourceAccountNumber(sourceAccountNumber);
		batch.setDescription(description);
		batch.setDate(LocalDate.now());
		
		for (TransferRequest transferRequest: listTransfers) {
			Transfer transfer = new Transfer();
			transfer.setSourceAccountNumber(sourceAccountNumber);
			transfer.setDestinationAccountNumber(transferRequest.getDestinationAccountNumber());
			transfer.setAmount(transferRequest.getAmount());
			transfer.setTransferDate(LocalDate.now());
			transfer.setDescription(transferRequest.getDescription());
			batch.addTransfer(transfer);
			transferRepository.save(transfer);
		}
		
		return batchTransferRepository.save(batch);
	}

}
