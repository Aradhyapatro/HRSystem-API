package com.HRSystem.Project.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.HRSystem.Project.Model.Employee;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class emailService {

    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private pdfService pdf;
    @Autowired
    private excelService excel;
    @Autowired
    private employeeService emp;
    
    public void addPdf(MimeMessageHelper helper) throws MessagingException {
    	// PDF Attachments
        helper.addAttachment("Departments.pdf", new ByteArrayResource(pdf.departmentPDF().toByteArray()));
        helper.addAttachment("Users.pdf", new ByteArrayResource(pdf.usersPDF().toByteArray()));
    }
    
    public void addExcel(MimeMessageHelper helper)throws MessagingException{
    	// Excel Attachments
        helper.addAttachment("Departments.xlsx", new ByteArrayResource(excel.getDepartmentExcel().readAllBytes()));
        helper.addAttachment("Users.xlsx", new ByteArrayResource(excel.getUserExcel().readAllBytes()));
    }
    
    public void sendToAdmins(String subject,String body) throws MessagingException {
    	List<Employee> emps = emp.getAllAdmins().getBody();
    	for(Employee e:emps) {
    		sendEmail(e.getUser().getUsername(),subject,body);
    	}
    }

    public void sendEmail(String toEmail, String subject, String body) throws MessagingException {
    	MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true); // 'true' enables multipart

        helper.setTo(toEmail);
        helper.setSubject(subject);
        helper.setText(body);
        
        ClassPathResource image = new ClassPathResource("static/hrsystem.jpg");
        helper.addInline("logoImage", image);
        String htmlbody = "<html><body>"
                + "<img src='cid:logoImage' alt='Logo' style='width:100%; max-width:600px; display:block; margin:0 auto;'><br>"
                + "<p>" + body + "</p>"
                + "</body></html>";
        helper.setText(htmlbody,true);

        addPdf(helper);
        addExcel(helper);

        mailSender.send(message);
    }
}
