package cgb.transfer.service;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@Service
public class MailService {
	
	@Autowired
    private JavaMailSender mailSender;

	/**
	 * Méthode d'envoi d'email de rapport à la création d'un lot de virements
	 * 
	 * @param to            Destinataire du mail
	 * @param refLot		Référence unique du lot de virements
	 * @param date          Date de création du lot de virements
	 * @param successCount  Nombre de transferts réussis
	 * @param failureCount  Nombre de transferts en échec
	 */
    public void sendBatchReport(String to, String refLot, LocalDate date, int successCount, int failureCount) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(to);
        message.setSubject("Rapport de traitement du lot : " + refLot);

        String content = "Hello,\n\n" +
                         "Batch transfer N°" + refLot + " from " + date + " is closed.\n" +
                         "Summary of transactions :\n" +
                         "- Successful : " + successCount + "\n" +
                         "- In failure : " + failureCount + "\n\n" +
                         "Sincerely, CGB";

        message.setText(content);
        mailSender.send(message);
    }

}
