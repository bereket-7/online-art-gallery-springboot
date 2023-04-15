package com.project.oag.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.project.oag.entity.Customer;
import com.project.oag.repository.CustomerRepository;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private JavaMailSender mailSender;

    public void registerUser(Customer user) {
        user.setEnabled(false);
        customerRepository.save(user);

        String token = UUID.randomUUID().toString();
        sendConfirmationEmail(user.getEmail(), token);

        // Save the token in the database for later verification
        // You can use a separate table or add a field in the User entity
        // For simplicity, I'm adding it as a field in the User entity here
        user.setToken(token);
        customerRepository.save(user);
    }
    


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
    }}

    private void sendConfirmationEmail(String email, String token) {
        SimpleMailMessage message = new SimpleMailMessage();
        
  // Set the sender and recipient email addresses
  message.setFrom("sheba.oag.web@gmail.com");
  message.setTo(email);
  message.setSubject("Confirm your registration");
  String confirmationUrl = "http://localhost:8081/customer/confirm?email=" + email + "&token=" + token;
  message.setText("Please click the following link to confirm your registration: " + confirmationUrl);
  mailSender.send(message);
    }
    
}
