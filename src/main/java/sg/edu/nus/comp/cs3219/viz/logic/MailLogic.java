package sg.edu.nus.comp.cs3219.viz.logic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import sg.edu.nus.comp.cs3219.viz.common.entity.Mail;
import sg.edu.nus.comp.cs3219.viz.common.JavaMailWrapper;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.logging.Logger;

@Component
public class MailLogic {

    private static final Logger log = Logger.getLogger(MailLogic.class.getSimpleName());

    @Value("${smtpconfiguration.mail.mailaddress}")
    private String smtpMailAddress;

    private JavaMailWrapper javaMailWrapper;

    @Autowired
    public MailLogic(JavaMailWrapper javaMailWrapper) {
        this.javaMailWrapper = javaMailWrapper;
    }

    public void sendMessage(Mail mailRequest) {
        try {
            Message message = new MimeMessage(this.javaMailWrapper.getJavaMailSession());
            message.setFrom(new InternetAddress(smtpMailAddress));
            message.setRecipients(Message.RecipientType.TO, mailRequest.getMailToAsInternetAddress());
            message.setSubject(mailRequest.getMailSubject());
            message.setText(mailRequest.getMailContent());
            Transport.send(message);
        } catch (AddressException ex) {
            log.info("Error Sending Email: " + ex.getMessage());
        } catch (MessagingException ex) {
            log.info("Error Sending Email: " + ex.getMessage());
        }
    }
}
