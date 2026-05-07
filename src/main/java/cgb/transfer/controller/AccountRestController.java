package cgb.transfer.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import cgb.transfer.dto.AccountRequest;
import cgb.transfer.entity.Account;
import cgb.transfer.service.AccountService;
import cgb.transfer.exception.*;

/**
 * Classe de gestion de la route pour la gestion des comptes.
 */
@RestController
@RequestMapping("/api/accounts")
public class AccountRestController {

	@Autowired
	/**
	 * Lien au Service de gestion des comptes.
	 */
	private AccountService accountService;

	@PostMapping
	/**
	 * Fonction de gestion de la création d'un compte.
	 * 
	 * @param accountRequest  L'objet JSON envoyé dans le corps de la requête POST
	 * 
	 * @return  L'objet JSON correspondant au transfer s'il est valide.
	 * 			Une réponse 'BAD_REQUEST' si une RuntimeException est rencontrée.
	 * 
	 * @throws AccountExistingException 
	 * @throws IOException 
	 * @throws InvalidUnCheckableIbanException 
	 * @throws InvalidIbanFormatException 
	 */
	public ResponseEntity<?> createAccount(@RequestBody AccountRequest accountRequest) throws InvalidIbanFormatException, InvalidUnCheckableIbanException, IOException, AccountExistingException {
		try {
			Account account = accountService.createAccount(
					accountRequest.getAccountNumber()
					);
			return ResponseEntity.ok(account);
		}catch (RuntimeException e) {
			AccountResponse errorResponse = new AccountResponse("FAILURE", e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
		}        
	}  

	@DeleteMapping
	/**
	 * Fonction de suppression d'un compte dans la BDD.
	 * 
	 * @param accountNumber IBAN du compte à supprimer
	 * 
	 * @return  L'objet supprimé si l'opération a réussi.
	 * 			Une réponse 'BAD_REQUEST' si une RuntimeException 
	 * 			ou une DeleteTransferException est rencontrée.
	 */
	public ResponseEntity<?> deleteAccount(@RequestBody String accountNumber) throws IOException {
		try {
			Account a = accountService.deleteTransfer(accountNumber);
			AccountResponse succesResponse = new AccountResponse("SUCCESS", a.toString());
			return ResponseEntity.ok(succesResponse);

		}catch (RuntimeException | DeleteAccountException e) {
			AccountResponse errorResponse = new AccountResponse("FAILURE", e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
		}        
	}
	
	@GetMapping("/test/{accNumber}")
	public int testhasAccountSentTransfers(@PathVariable String accNumber) {
		return accountService.testHasAccountSentTransfers(accNumber);
	}
	
}

/**
 * Classe de modélisation d'un message d'erreur personnalisable.
 */
class AccountResponse {
	/**
	 * Le status Http présent dans le message d'erreur.
	 */
	private String status;
	/**
	 * Le contenu du message d'erreur.
	 */
	private String message;

	// Constructeur
	public AccountResponse(String status, String message) {
		this.status = status;
		this.message = message;
	}

	// Getters et Setters
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
