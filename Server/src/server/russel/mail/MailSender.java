package server.russel.mail;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class MailSender {

    //private MailService service;
    private String mailPropertiesFilePath;
    private String emailFrom; //"an020500@gmail.com"
    private String emailTo; //"www.andrewcool@yandex.ru"
    private String password;

    public MailSender(String mailPropertiesFilePath, String emailFrom, String emailTo, String password){
        this.mailPropertiesFilePath = mailPropertiesFilePath;
        this.emailFrom = emailFrom;
        this.emailTo = emailTo;
        this.password = password;
    }

    public MailSender(String emailFrom, String emailTo, String password){
        this("properties/mail.properties", emailFrom, emailTo, password);
    }

    public void sendMessage(String theme, String message) {
        Properties prop = new Properties();
        try (InputStream in = Files.newInputStream(Paths.get(mailPropertiesFilePath))){
            prop.load(in);
        } catch (Exception e) {
            System.out.println("Не удалось отправить сообщение.");
        }

        Session sess = Session.getDefaultInstance(prop);
        try (Transport tr = sess.getTransport()){
            MimeMessage msg = new MimeMessage(sess);
            msg.setFrom(new InternetAddress(emailFrom));
            msg.addRecipient(Message.RecipientType.TO, new InternetAddress(emailTo));
            msg.setSubject(theme);
            msg.setText(message);
            tr.connect(null, password);
            tr.sendMessage(msg, msg.getAllRecipients());
            System.out.println("Письмо отправлено!");
        } catch (MessagingException me) {
            System.out.println("Не удалось отправить сообщение.");
        }
    }
}
