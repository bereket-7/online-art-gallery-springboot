package com.project.oag.service;

import java.util.Random;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.project.oag.entity.Customer;
import com.project.oag.exceptions.UserAlreadyExistException;
import com.project.oag.repository.CustomerRepository;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JavaMailSender mailSender;
    
    @Value("${email.throttle.delay}")
    private long emailThrottleDelay;
    
     @Value("${spring.mail.username}")
     private String senderEmail;

    public void registerUser(Customer user) {
        user.setEnabled(false);
        customerRepository.save(user);

        //String token = UUID.randomUUID().toString();
        sendConfirmationEmail(user.getEmail());
        //user.setToken(token);
        customerRepository.save(user);
    }
    private Customer emailExists(final String email) {
        return customerRepository.findByEmail(email);
    }
  /*  
    public void confirmRegistration(String email, String token) {
        Customer user =  customerRepository.findByEmail(email);
        if(user == null){
            new IllegalArgumentException("Invalid email");
        }
        else{
            
        if (!user.getToken().equals(token)) {
            throw new IllegalArgumentException("Invalid token");
        }
        user.setEnabled(true);
        user.setToken(null); // Remove the token after successful confirmation
        customerRepository.save(user);
    }
}*/
    
    /*
    
    private void sendConfirmationEmail(String email, String token) {
        SimpleMailMessage message = new SimpleMailMessage();
        
  // Set the sender and recipient email addresses
  message.setFrom(senderEmail);
  message.setTo(email);
  message.setSubject("Confirm your registration");
  String confirmationUrl = "http://localhost:8081/customer/confirm?email=" + email + "&token=" + token;
  message.setText("Please click the following link to confirm your registration: " + confirmationUrl);
  mailSender.send(message);
  
 }*/
    private void sendConfirmationEmail(String email) {
    	Customer user =  customerRepository.findByEmail(email);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(senderEmail);
        message.setTo(email);
        message.setSubject("Confirm your registration");
        Random random = new Random();
    String confirmationCode = String.format("%06d", random.nextInt(1000000));
        user.setToken(confirmationCode);
        customerRepository.save(user);
        
        message.setText("Please enter the following confirmation code on our website to confirm your registration: " + confirmationCode);
        mailSender.send(message);
        
    }
    
    public void confirmRegistration(String email, String confirmationCode) {
    Customer user = customerRepository.findByEmail(email);
    if(user == null){
        throw new IllegalArgumentException("Invalid email");
    }
    else{
        if (!user.getToken().equals(confirmationCode)) {
            throw new IllegalArgumentException("Invalid confirmation code");
        }
        user.setEnabled(true);
        user.setToken(null); // Remove the confirmation code after successful confirmation
        customerRepository.save(user);
    }
}
/*

@Autowired
private SendinblueClient sendinblueClient;

public void sendConfirmationEmail(String email, String token) {
    TransactionalEmailsApi apiInstance = sendinblueClient.getTransactionalEmailsApi();

    SendSmtpEmail emailObject = new SendSmtpEmail();
    emailObject.setTo(Arrays.asList(new SendSmtpEmailTo(email, null)));
    emailObject.setTemplateId(1L);
    emailObject.setParams(Collections.singletonMap("confirmationUrl", "http://localhost:8081/customer/confirm?email=" + email + "&token=" + token));

    try {
        apiInstance.sendTransacEmail(emailObject);
    } catch (ApiException e) {
        System.err.println("Exception when calling TransactionalEmailsApi#sendTransacEmail");
        e.printStackTrace();
    }
}

*/  
}
