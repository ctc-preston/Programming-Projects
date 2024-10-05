
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;

public class SendMessage {
	
	public static void main(String[] args) {
		
		// change accordingly
        final String username = "pkai+loats.org";  
         
        // change accordingly
        final String password = "********";
         
        // or IP address
        final String host = "mail.loats.org";
 
        // Get system properties
        Properties props = new Properties();            
         
        // enable authentication
        props.put("mail.smtp.auth", "true");              
         
        // enable STARTTLS
        props.put("mail.smtp.starttls.enable", "true");   
         
        // Setup mail server
        props.put("mail.smtp.host", "mail.loats.org");    
         
        // TLS Port
        props.put("mail.smtp.port", "587");
		
        // creating Session instance referenced to
        // Authenticator object to pass in
        // Session.getInstance argument
        Session session = Session.getInstance(props,
          new javax.mail.Authenticator() {
            
            //override the getPasswordAuthentication method
            protected PasswordAuthentication
                           getPasswordAuthentication() {
                                        
                return new PasswordAuthentication(username,
                                                 password);
            }
          });
        
        
        String files = readFile();
        System.out.println(files);
        
        try {
            
            // compose the message
            // javax.mail.internet.MimeMessage class is
            // mostly used for abstraction.
            Message message = new MimeMessage(session);   
             
            // header field of the header.
            message.setFrom(new InternetAddress("JacePicture@PuppyDog.org"));
//            message.setFrom(new InternetAddress("BirdDog@TotallyReal.org"));
             
//            message.setRecipients(Message.RecipientType.TO,
//                InternetAddress.parse("5122101505@tmomail.net"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(files));
//            	message.setRecipients(Message.RecipientType.TO,
//                  InternetAddress.parse("5122184772@tmomail.net"));
            message.setSubject("Jace");
//            message.setText("Jace is the Best!");     
            
            String filePath = selectPicture("C:\\Users\\Preston\\Desktop\\TextProgram\\Data\\Pictures");
            System.out.println(filePath);
            if (filePath.isEmpty()) {
            	// todo send notification to self (3 pictures left)
            	
            	filePath = selectPicture("C:\\Users\\Preston\\Desktop\\TextProgram\\Data\\Used Pictures");
                System.out.println(filePath);
                  
            }
            
        	// This mail has 2 part, the BODY and the embedded image
            MimeMultipart multipart = new MimeMultipart("related");

            // first part (the html)
            BodyPart messageBodyPart = new MimeBodyPart();
            String htmlText = "<H1></H1><img src=\"cid:image\">"; // Can add Text between H1s
            messageBodyPart.setContent(htmlText, "text/html");
            // add it
            multipart.addBodyPart(messageBodyPart);

            // second part (the image)
            messageBodyPart = new MimeBodyPart();
            DataSource fds = new FileDataSource(filePath);

            messageBodyPart.setDataHandler(new DataHandler(fds));
            messageBodyPart.setHeader("Content-ID", "<image>");

            // add image to the multipart
            multipart.addBodyPart(messageBodyPart);

            // put everything together
            message.setContent(multipart);
        
        	Transport.send(message);         //send Message
        	
//          movePicture(filePath);
 
            System.out.println("Done");
 
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        
	}
	
	private static String readFile() {
		BufferedReader br = null;
		String files = "";
		try {
		// File path is passed as parameter
        File file = new File(
            "C:\\Users\\Preston\\Desktop\\TextProgram\\Data\\Numbers.txt");
 
        // Note:  Double backquote is to avoid compiler
        // interpret words
        // like \test as \t (ie. as a escape sequence)
 
        // Creating an object of BufferedReader class
			br = new BufferedReader(new FileReader(file));
 
        // Declaring a string variable
        String st;
        // Condition holds true till
        // there is character in a string
        while ((st = br.readLine()) != null)
        	
            files += (files.length() == 0 ? "" : ",") + st;
        
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			
		} catch (IOException e) {
			e.printStackTrace();
			
		} finally {
			try {
				if (br != null) {
					br.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return files;
	}
	
	private static String selectPicture(String dir) {
		Random random = new Random();
		String result = "";
		
		File f = new File(dir);
		File[] files = f.listFiles();
	
		if (files.length > 0) {	
			int number = random.nextInt(files.length);
			result = files[number].getAbsolutePath();
		}
		
		return result;
		
	}
	
	private static void movePicture(String filePath) {
		try {
			File f = new File(filePath);
			
			String fileName = f.getName();
			String target = "C:\\Users\\Preston\\Desktop\\TextProgram\\Data\\Used Pictures\\" + fileName;
			Files.move(Paths.get(filePath), Paths.get(target));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
