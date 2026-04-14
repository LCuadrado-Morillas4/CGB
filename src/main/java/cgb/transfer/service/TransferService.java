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

	/*
	 * Rappel du cours sur les transactions... Tout ou rien
	 */
	@Transactional
	public Transfer createTransfer(String sourceAccountNumber, String destinationAccountNumber, Double amount,
			LocalDate transferDate, String description) throws InvalidAccountException, NegativeTransferAmountException,
			DateTransferException, InsufficientFundsException {
		Transfer transfer = new Transfer();
		transfer.setSourceAccountNumber(sourceAccountNumber);
		transfer.setDestinationAccountNumber(destinationAccountNumber);
		transfer.setAmount(amount);
		transfer.setTransferDate(transferDate);
		transfer.setDescription(description);
		transfer.setState(State.WAITING.getNom());
		transferRepository.save(transfer);

		Optional<Account> sourceAccount = accountRepository.findById(sourceAccountNumber);

		if (sourceAccount == null) {
			transfer.setState(State.FAILURE.getNom());
			transferRepository.save(transfer);
			throw new InvalidAccountException("Source");
		}

		Optional<Account> destinationAccount = accountRepository.findById(destinationAccountNumber);

		if (destinationAccount == null) {
			transfer.setState(State.FAILURE.getNom());
			transferRepository.save(transfer);
			throw new InvalidAccountException("Destination");
		}

		if (amount < 0) {
			transfer.setState(State.FAILURE.getNom());
			transferRepository.save(transfer);
			throw new NegativeTransferAmountException();
		} else if (transferDate.isBefore(LocalDate.now())) {
			transfer.setState(State.FAILURE.getNom());
			transferRepository.save(transfer);
			throw new DateTransferException();
		} else if (sourceAccount.get().getSolde().compareTo(amount) < 0) {
			transfer.setState(State.CANCELLED.getNom());
			transferRepository.save(transfer);
			throw new InsufficientFundsException();
		} else {
			sourceAccount.get().setSolde(sourceAccount.get().getSolde() - (amount));
			destinationAccount.get().setSolde(destinationAccount.get().getSolde() + (amount));

			accountRepository.save(sourceAccount.get());
			accountRepository.save(destinationAccount.get());

			transfer.setState(State.SUCCESS.getNom());

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
