package com.HRSystem.Project.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.HRSystem.Project.DTO.emailRequest;
import com.HRSystem.Project.Service.emailService;

import jakarta.mail.MessagingException;

@RestController
@RequestMapping("/api/email")
public class emailController {

    @Autowired
    private emailService emailService;

    @PostMapping("/send")
    public ResponseEntity<String> sendMail(@RequestBody emailRequest request) throws MessagingException {
        emailService.sendEmail(request.getTo(), request.getSubject(), request.getBody());
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Email was sent to "+request.getTo());
    }
    
    @PostMapping("/send/all")
    public ResponseEntity<String> sendMailToAll(@RequestBody emailRequest request) throws MessagingException {
        emailService.sendToAdmins(request.getSubject(), request.getBody());
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Email was sent to All");
    }
}

