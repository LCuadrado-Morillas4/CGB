package cgb.transfer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import cgb.transfer.dto.TransferRequest;
import cgb.transfer.entity.BatchTransfer;
import cgb.transfer.entity.State;
import cgb.transfer.entity.Transfer;
import cgb.transfer.exception.*;
import cgb.transfer.exception.DeleteTransferException.FailureTransfert;
import cgb.transfer.repository.AccountRepository;
import cgb.transfer.repository.BatchTransferRepository;
import cgb.transfer.repository.TransferRepository;
import cgb.utils.Logger;
import jakarta.transaction.Transactional;

import java.io.IOException;
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
	
	@Autowired
	private TransferService transferService;
	
	@Autowired
	private MailService mail;
	
	private Logger logger = Logger.getInstance();
	
	@Async
	@Transactional
	public void createBatchTransfer(String sourceAccountNumber, String description, List<TransferRequest> listTransfers) throws InvalidAccountException, NegativeTransferAmountException, DateTransferException, InsufficientFundsException, IOException {
		BatchTransfer batch = new BatchTransfer();
		batch.setRefLot(generateRefLot());
		batch.setSourceAccountNumber(sourceAccountNumber);
		batch.setDescription(description);
		batch.setDate(LocalDate.now());
		batch.setState(State.RECEIVED.getNom());
		batchTransferRepository.save(batch);
		logger.log("Batch ref: " + batch.getRefLot() + " | Creating batch succeeded");
		
		if (!accountRepository.findById(sourceAccountNumber).isPresent()) {
			throw new InvalidAccountException("Source");
		}
		
		int successCount = 0;
        int failureCount = 0;
		
		for (TransferRequest transferRequest: listTransfers) {
			Transfer transfer = transferService.createTransferForBatch(sourceAccountNumber,
					transferRequest.getDestinationAccountNumber(),
					transferRequest.getAmount(),
					LocalDate.now(),
					description);
			
			transfer.setBatch_id(batch);
			batch.addTransfer(transfer);
			logger.log(formatTransfer(transfer));
			transferRepository.save(transfer);
			batchTransferRepository.save(batch);
			
			if (transfer.getState() == "success") {
                successCount += 1;
            } else {
                failureCount += 1;
            }
		}
		
		batch.setState(State.CLOSED.getNom());
		logger.log("Batch completed\n==================================================================");
		
		try {
            mail.sendBatchReport("comptable@gsb.fr", batch.getRefLot(), batch.getDate(), successCount, failureCount);
            logger.log("Batch reference: " + batch.getRefLot() + " | Notification email sent successfully");
        } catch (Exception e) {
            logger.log("WARNING: Notification email failed for batch " + batch.getRefLot() + ". Error: " + e.getMessage());
        }
		
		batchTransferRepository.save(batch);
	}
	
	/**
	 * Compte le nombre de transferts par lot fait dans une journée.
	 * 
	 * @param date
	 * 
	 * @return int
	 */
	private int countBatchTransfers(LocalDate date) {
		return batchTransferRepository.countBatchTransfers(date);
	}
	
	/**
	 * Génère la valeur de refLot pour un transfert par lot.
	 * 
	 * @return String
	 */
	public String generateRefLot() {
		return LocalDate.now().toString() + "-" + (countBatchTransfers(LocalDate.now()) + 1);
	}
	
	@Transactional
	public BatchTransfer deleteBatchTransfer(long id) throws DeleteTransferException {
		Optional<BatchTransfer> oBatch= batchTransferRepository.findById(id);
		transferRepository.deleteById(id);
		if (oBatch.isEmpty())
			throw new DeleteTransferException(FailureTransfert.OBJECT_NOT_FOUND);
		return oBatch.orElse(null);
	}
	
	public String formatTransfer(Transfer transfer) {
		String message = "Transfer from " + transfer.getSourceAccountNumber() + " to "
				+ transfer.getDestinationAccountNumber() 
				+ " | Amount: " + transfer.getAmount() 
				+ " | Date: " + transfer.getTransferDate() 
				+ " | State: " + transfer.getState();

		if (transfer.getReason() != null) {
			message += " | Reason: " + transfer.getReason();
		}
		
		return message;
	}

	public BatchTransfer findBatchByRefLot(String refLot) {
		return batchTransferRepository.findBatchByRefLot(refLot);
	}

}
