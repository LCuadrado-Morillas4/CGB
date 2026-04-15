package cgb.transfer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cgb.transfer.entity.Account;
import cgb.transfer.entity.State;
import cgb.transfer.entity.Transfer;
import cgb.transfer.exception.*;
import cgb.transfer.exception.DeleteTransferException.FailureTransfert;
import cgb.transfer.repository.AccountRepository;
import cgb.transfer.repository.TransferRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.util.Optional;

/**
 * La classe de Service permettant le lien entre Repository et Controller.
 */
@Service
public class TransferService {

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private TransferRepository transferRepository;

	@Transactional
	public Transfer createTransfer(String sourceAccountNumber, String destinationAccountNumber, Double amount,
			LocalDate transferDate, String description) throws DateTransferException, NegativeTransferAmountException,
			InvalidAccountException, InsufficientFundsException {

		Transfer transfer = new Transfer();
		transfer.setSourceAccountNumber(sourceAccountNumber);
		transfer.setDestinationAccountNumber(destinationAccountNumber);
		transfer.setAmount(amount);
		transfer.setTransferDate(transferDate);
		transfer.setDescription(description);

		Optional<Account> sourceAccount = accountRepository.findById(sourceAccountNumber);
		Optional<Account> destinationAccount = accountRepository.findById(destinationAccountNumber);

		if (!sourceAccount.isPresent()) {
			throw new InvalidAccountException("Source");
		}

		if (!destinationAccount.isPresent()) {
			throw new InvalidAccountException("Destination");
		}

		if (transferDate.isBefore(LocalDate.now())) {
			throw new DateTransferException();
		} else if (amount <= 0) {
			throw new NegativeTransferAmountException();
		} else if (sourceAccount.get().getSolde().compareTo(amount) < 0) {
			throw new InsufficientFundsException();
		} else {
			sourceAccount.get().setSolde(sourceAccount.get().getSolde() - (amount));
			destinationAccount.get().setSolde(destinationAccount.get().getSolde() + (amount));

			accountRepository.save(sourceAccount.get());
			accountRepository.save(destinationAccount.get());

			return transferRepository.save(transfer);
		}

	}

	@Transactional
	public Transfer createTransferForBatch(String sourceAccountNumber, String destinationAccountNumber, Double amount,
			LocalDate transferDate, String description){
		Transfer transfer = new Transfer();
		transfer.setSourceAccountNumber(sourceAccountNumber);
		transfer.setDestinationAccountNumber(destinationAccountNumber);
		transfer.setAmount(amount);
		transfer.setTransferDate(transferDate);
		transfer.setDescription(description);
		transfer.setState(State.WAITING.getNom());
		transfer.setReason("Transfer sent");
		transferRepository.save(transfer);

		Optional<Account> sourceAccount = accountRepository.findById(sourceAccountNumber);

		if (!sourceAccount.isPresent()) {
			transfer.setState(State.FAILURE.getNom());
			transfer.setReason("Source account doesn't exist");
			return transferRepository.save(transfer);
		}

		Optional<Account> destinationAccount = accountRepository.findById(destinationAccountNumber);

		if (!destinationAccount.isPresent()) {
			transfer.setState(State.FAILURE.getNom());
			transfer.setReason("Destination account doesn't exist");
			return transferRepository.save(transfer);
		}

		if (transferDate.isBefore(LocalDate.now())) {
			transfer.setState(State.FAILURE.getNom());
			transfer.setReason("Transfer date is prior to today");
			return transferRepository.save(transfer);
		} else if (amount < 0) {
			transfer.setState(State.FAILURE.getNom());
			transfer.setReason("Transfer amount can't be negative");
			return transferRepository.save(transfer);
		} else if (sourceAccount.get().getSolde().compareTo(amount) < 0) {
			transfer.setState(State.CANCELLED.getNom());
			transfer.setReason("Insufficient funds for source account");
			return transferRepository.save(transfer);
		} else {
			sourceAccount.get().setSolde(sourceAccount.get().getSolde() - (amount));
			destinationAccount.get().setSolde(destinationAccount.get().getSolde() + (amount));

			accountRepository.save(sourceAccount.get());
			accountRepository.save(destinationAccount.get());

			transfer.setState(State.SUCCESS.getNom());
			transfer.setReason("Transfer succesfull");

			return transferRepository.save(transfer);
		}
	}

	@Transactional
	public Transfer deleteTransfer(Long id) throws DeleteTransferException {
		Optional<Transfer> otranfer = transferRepository.findById(id);
		transferRepository.deleteById(id);
		if (otranfer.isEmpty())
			throw new DeleteTransferException(FailureTransfert.OBJECT_NOT_FOUND);
		return otranfer.orElse(null);
	}
}
