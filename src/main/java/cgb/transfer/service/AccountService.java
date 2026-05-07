package cgb.transfer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cgb.transfer.entity.Account;
import cgb.transfer.exception.*;
import cgb.transfer.exception.DeleteAccountException.FailureAccount;
import cgb.transfer.repository.AccountRepository;
import cgb.utils.CGBIbanValidator;
import cgb.utils.Logger;
import jakarta.transaction.Transactional;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;

/**
 * La classe de Service permettant le lien entre Repository et Controller.
 */
@Service
public class AccountService {

	@Autowired
	private AccountRepository accountRepository;
	
	private CGBIbanValidator IBANValidator = CGBIbanValidator.getInstanceValidator();
	private Logger logger = Logger.getInstance();

	/**
	 * Crée un nouveau compte à partir des informations données
	 * 
	 * @param accountNumber  IBAN du compte source
	 * 
	 * @throws InvalidIbanFormatException 
	 * @throws InvalidUnCheckableIbanException 
	 * @throws IOException 
	 * @throws AccountExistingException 
	 */
	@Transactional
	public Account createAccount(String accountNumber) throws InvalidIbanFormatException, InvalidUnCheckableIbanException, IOException, AccountExistingException {
		//Vérifications de la validité de l'IBAN
		IBANValidator.isIbanStructureValid(accountNumber);
		IBANValidator.isIbanValid(accountNumber);
		
		Account account = new Account();
		account.setAccountNumber(accountNumber);
		
		logger.log("logs_account.txt", LocalDate.now() + " - Création d'un nouveau compte : " + accountNumber);
				
		//Vérification de 
		if (!accountRepository.findById(accountNumber).isEmpty()) {
			logger.log("log_accounts.txt", "Exception : An account with this IBAN already exists in the database");
			throw new AccountExistingException();
		}
		
		return accountRepository.save(account);
	}

	/**
	 * Supprime le compte à l'aide de son identifiant
	 * 
	 * @param id Identifiant du compte
	 * 
	 * @return Compte supprimé
	 * @throws DeleteAccountException 
	 * @throws IOException 
	 */
	@Transactional
	public Account deleteTransfer(String accountNumber) throws DeleteAccountException, IOException {
		Optional<Account> oaccount = accountRepository.findById(accountNumber);
		
		String message = LocalDate.now() + " - Suppression du compte : " + accountNumber;
		
		//Vérifications 
		if (oaccount.isEmpty()) {
			message += " | Exception levée : Compte non trouvé dans la base de données";
			throw new DeleteAccountException(FailureAccount.OBJECT_NOT_FOUND);
		} else if (oaccount.get().getSolde() != 0) {
			message += " | Exception levée : solde du compte non nul";
			throw new DeleteAccountException(FailureAccount.BALANCE_NOT_NULL);			
		} else if (hasAccountSentTransfers(accountNumber)) {
			message += "| Exception levée : Compte impliqué dans des virements bancaires";
			throw new DeleteAccountException(FailureAccount.ACCOUNT_SENT_TRANSFERS);
		}
		
		accountRepository.deleteById(accountNumber);
		
		logger.log("logs_account.txt", message);

		if (oaccount.isEmpty())
			throw new DeleteAccountException(FailureAccount.OBJECT_NOT_FOUND);
		return oaccount.orElse(null);
	}
	
	public int testHasAccountSentTransfers(String accountNumber) {
		return accountRepository.hasAccountSentTransfers(accountNumber);
	}

	private boolean hasAccountSentTransfers(String accountNumber) {
		if (accountRepository.hasAccountSentTransfers(accountNumber) < 0) {
			return true;
		}		
		return false;
	}
	
}
