package cgb.transfer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cgb.transfer.dto.TransferRequest;
import cgb.transfer.entity.Account;
import cgb.transfer.entity.State;
import cgb.transfer.entity.Transfer;
import cgb.transfer.exception.*;
import cgb.transfer.exception.DeleteTransferException.FailureTransfert;
import cgb.transfer.repository.AccountRepository;
import cgb.transfer.repository.TransferRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
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

	/**
	 * Crée un transfert à partir des informations données
	 * 
	 * @param sourceAccountNumber       IBAN du compte source
	 * @param destinationAccountNumber  IBAN du compte destinataire
	 * @param amount					Montant du transfert
	 * @param transferDate				Date du transfert
	 * @param description				Description du transfert
	 * 
	 * @return Le transfert créé
	 * 
	 * @throws DateTransferException
	 * @throws NegativeTransferAmountException
	 * @throws InvalidAccountException
	 * @throws InsufficientFundsException
	 */
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

	/**
	 * Crée un transfert à partir des informations données.
	 * Différenciation d'un transfsert classique étant donné que les transfert venant d'un lot possède l'attribut d'état pour la traçabilité.
	 * 
	 * @param sourceAccountNumber       IBAN du compte source
	 * @param destinationAccountNumber  IBAN du compte destinataire
	 * @param amount					Montant du transfert
	 * @param transferDate				Date du transfert
	 * @param description				Description du transfert
	 * 
	 * @return Le transfert créé
	 */
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

	/**
	 * Supprime le transfert à l'aide de son identifiant
	 * 
	 * @param id Identifiant du transfert
	 * 
	 * @return Transfert supprimé
	 * 
	 * @throws DeleteTransferException
	 */
	@Transactional
	public Transfer deleteTransfer(Long id) throws DeleteTransferException {
		Optional<Transfer> otranfer = transferRepository.findById(id);
		transferRepository.deleteById(id);
		if (otranfer.isEmpty())
			throw new DeleteTransferException(FailureTransfert.OBJECT_NOT_FOUND);
		return otranfer.orElse(null);
	}

	/**
	 * Renvoie les transferts d'un lot précis
	 * 
	 * @param refLot Numéro unique du lot 
	 * 
	 * @return Liste des transferts
	 */
	public List<Transfer> getTransferFromBatch(String refLot) {
		return transferRepository.findTransferByBatch(refLot);
	}

	/**
	 * Renvoie les transferts en échec d'un lot précis
	 * 
	 * @param refLot Numéro unique du lot 
	 * 
	 * @return Liste des transferts
	 */
	public List<Transfer> getFailedTransferByBatch(String refLot) {
		return transferRepository.findByRefLotAndNotSuccess(refLot);
	}

	/**
	 * Renvoie les transferts en échec sur une intervalle de dates
	 * 
	 * @param start Date de début de l'intervalle
	 * @param end   Date de fin de l'intervalle
	 * 
	 * @return Liste des transferts
	 */
	public List<Transfer> getFailedTransferByDateInterval(LocalDate start, LocalDate end) {
		return transferRepository.findByDateIntervalAndNotSuccess(start, end);
	}

	/**
	 * Renvoie les transferts en échec pour un compte destinataire
	 * 
	 * @param destAccountNumber IBAN du compte destinataire
	 * 
	 * @return Liste des transferts
	 */
	public List<Transfer> getFailedTransferByDestAccount(String destAccountNumber) {
		return transferRepository.findByDestAccountAndNotSuccess(destAccountNumber);
	}
	
	public List<TransferRequest> findCancelledTransferFromBatch(String refLot) {
		List<TransferRequest> trq = new ArrayList<TransferRequest>();
		List<Transfer> list = transferRepository.findCancelledTransferFromBatch(refLot);
		for (Transfer t: list) {
			TransferRequest temp = new TransferRequest();
			temp.setDestinationAccountNumber(t.getDestinationAccountNumber());
			temp.setAmount(t.getAmount());
			temp.setDescription(t.getDescription());
			trq.add(temp);
		}
		return trq;
	}
	
}
