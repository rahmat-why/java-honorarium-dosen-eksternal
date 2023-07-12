package REPORT;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.util.Random;

public class EmailSender {
    // SMTP server configuration
    private String username = "rahmatwhy00@gmail.com";
    private String password = "drqzryswhlsgflsl";

    String receiver;
    String subject;
    String body;

    public EmailSender(String receiver, String subject, String body) {
        this.receiver = receiver;
        this.subject = subject;
        this.body = body;
    }

    public void sendEmail(String nama_dosen) {
        // Create properties object and set SMTP server configuration
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        // Create authenticator object to authenticate the username and password
        Authenticator auth = new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        };

        // Create session with the properties and authenticator
        Session session = Session.getInstance(props, auth);

        try {
            // Create message and set sender, recipient, subject, and content
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(this.receiver));
            message.setSubject(this.subject);
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText(this.body);

            // random
            Random random = new Random();

            // Create the message body part for the file attachment
            BodyPart attachmentBodyPart = new MimeBodyPart();
            String filename = System.getProperty("user.dir")+"\\src\\REPORT\\"+nama_dosen+".pdf";
            File file = new File(filename);
            FileDataSource dataSource = new FileDataSource(file);
            attachmentBodyPart.setDataHandler(new DataHandler(dataSource));
            attachmentBodyPart.setFileName(file.getName());

            // Create a multipart message to combine the text content and file attachment
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);
            multipart.addBodyPart(attachmentBodyPart);

            // Set the multipart message as the content of the email
            message.setContent(multipart);

            // Send the message
            Transport.send(message);

            System.out.println("Email sent successfully.");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}

