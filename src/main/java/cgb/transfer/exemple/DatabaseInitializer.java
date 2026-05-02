package cgb.transfer.exemple;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cgb.transfer.entity.Account;
import cgb.transfer.entity.Customer;
import cgb.transfer.entity.Role;
import cgb.transfer.entity.UserCGB;
import cgb.transfer.repository.AccountRepository;
import cgb.transfer.repository.CustomerRepository;
import cgb.transfer.repository.UserCGBRepository;
import cgb.utils.IbanGenerator;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;

/**
 * Classe responsable de l'initialisation des données des comptes dans la base
 * H2.
 */
@Component
public class DatabaseInitializer {

	/*
	 * @Autowired private final AccountRepository accountRepository;
	 * 
	 * Possibilité de faire une injection par l'attribut, mais il est recommander de
	 * la faire par constructeur comme présenté ci-dessous
	 * 
	 */
	private final AccountRepository accountRepository;
	private final CustomerRepository customerRepository;
	private final UserCGBRepository userCGBRepository;

	@Autowired
	public DatabaseInitializer(AccountRepository accountRepository, CustomerRepository customerRepository, UserCGBRepository userCGBRepository) {
		// super();
		this.accountRepository = accountRepository;
		this.customerRepository = customerRepository;
		this.userCGBRepository = userCGBRepository;
	}

	@PostConstruct
	public void init() {
		// Vérifiez si la base de données est vide avant d'insérer des données
		if (accountRepository.count() == 0) {
			insertSampleData(accountRepository, customerRepository, userCGBRepository);
		}
	}

	/**
	 * Fonction de valorisation de la base appellée si cette dernière est vide.
	 * 
	 * @param accountRepository L'instance de Repository actuellement utilisée.
	 */
	public static void insertSampleData(AccountRepository accountRepository, CustomerRepository customerRepository, UserCGBRepository userRepository) {

		Customer customerBMW = new Customer();
		customerBMW.setName("BMW");
		customerBMW.setAddress("1 Avenue du port");
		customerBMW.setLEI("W763E4TVIZKNBL8ZC7X7");
		customerRepository.save(customerBMW);
		
		Customer customerNBA = new Customer();
		customerNBA.setName("NBA");
		customerNBA.setAddress("23 Rue de la chèvre");
		customerNBA.setLEI("23LBJTH3G04TL4K3RS1N5");
		customerRepository.save(customerNBA);
		
		Customer customerUFC = new Customer();
		customerUFC.setName("UFC");
		customerUFC.setAddress("10 Impasse Ngannou");
		customerUFC.setLEI("UJK7SV5LEE7JOTAVBIYJ");
		customerRepository.save(customerUFC);
		
		UserCGB adminBMW = new UserCGB();
		adminBMW.setUsername("adminBMW");
		adminBMW.setPassword("Pwd_BMW_4dmin!");
		adminBMW.setEmail("admin@bmw.de");
		adminBMW.setRole(Role.ADMIN);
		adminBMW.setCompany(customerBMW);
		
		UserCGB adminNBA = new UserCGB();
		adminNBA.setUsername("adminNBA");
		adminNBA.setPassword("Pwd_NBA_4dmin!");
		adminNBA.setEmail("admin@nba.us");
		adminNBA.setRole(Role.ADMIN);
		adminNBA.setCompany(customerNBA);
		
		UserCGB adminUFC = new UserCGB();
		adminUFC.setUsername("adminUFC");
		adminUFC.setPassword("Pwd_UFC_4dmin!");
		adminUFC.setEmail("admin@ufc.us");
		adminUFC.setRole(Role.ADMIN);
		adminUFC.setCompany(customerUFC);

        Account account1 = new Account();
        account1.setAccountNumber(IbanGenerator.generateValidIban());
        account1.setSolde(300.00);
        account1.setCompany(customerBMW);
        account1.addRecipientAccount(customerNBA);
        account1.addRecipientAccount(customerUFC);
        accountRepository.save(account1);

        Account account2 = new Account();
        account2.setAccountNumber(IbanGenerator.generateValidIban());
        account2.setSolde(500.00);
        account2.setCompany(customerBMW);
        account2.addRecipientAccount(customerNBA);
        accountRepository.save(account2);

        Account account3 = new Account();
        account3.setAccountNumber(IbanGenerator.generateValidIban());
        account3.setSolde(2000.00);
        account3.setCompany(customerBMW);
        account3.addRecipientAccount(customerUFC);
        accountRepository.save(account3);
        
        Account account4 = new Account();
        account4.setAccountNumber(IbanGenerator.generateValidIban());
        account4.setSolde(300.00);
        account4.setCompany(customerNBA);
        account4.addRecipientAccount(customerBMW);
        account4.addRecipientAccount(customerUFC);
        accountRepository.save(account4);
        
        Account account5 = new Account();
        account5.setAccountNumber(IbanGenerator.generateValidIban());
        account5.setSolde(600.00);
        account5.setCompany(customerNBA);
        account5.addRecipientAccount(customerBMW);
        accountRepository.save(account5);
        
        Account account6 = new Account();
        account6.setAccountNumber(IbanGenerator.generateValidIban());
        account6.setSolde(8000.00);
        account6.setCompany(customerNBA);
        account6.addRecipientAccount(customerUFC);
        accountRepository.save(account6);
        
        Account account7 = new Account();
        account7.setAccountNumber(IbanGenerator.generateValidIban());
        account7.setSolde(2100.00);
        account7.setCompany(customerUFC);
        account7.addRecipientAccount(customerNBA);
        accountRepository.save(account7);
        
        Account account8 = new Account();
        account8.setAccountNumber(IbanGenerator.generateValidIban());
        account8.setSolde(1300.00);
        account8.setCompany(customerUFC);
        account8.addRecipientAccount(customerBMW);
        accountRepository.save(account8);
	}

}
