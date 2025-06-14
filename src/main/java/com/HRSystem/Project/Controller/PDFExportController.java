package com.HRSystem.Project.Controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.HRSystem.Project.Service.pdfService;
import com.HRSystem.Project.Service.timeStampService;


@Controller
@RequestMapping("/api/pdf")
public class PDFExportController {
	@Autowired
	private pdfService pdf;
	@Autowired
	private timeStampService time;
	
	 @GetMapping("/departments")
	public ResponseEntity<byte[]> generateDepartmentPdf() {
		ByteArrayOutputStream out = pdf.departmentPDF();
	    
	    ByteArrayInputStream bis = new ByteArrayInputStream(out.toByteArray());
	    HttpHeaders headers = new HttpHeaders();
	    headers.add("Content-Disposition", "attachment; filename=departments_"+time.timeStampDDMMYYYYHHMMSS()+".pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(bis.readAllBytes());
	}

	 @GetMapping("/users")
		public ResponseEntity<byte[]> generateUsersPdf() {
			ByteArrayOutputStream out = pdf.usersPDF();
		    
		    ByteArrayInputStream bis = new ByteArrayInputStream(out.toByteArray());
		    HttpHeaders headers = new HttpHeaders();
	        headers.add("Content-Disposition", "inline; filename=users_"+time.timeStampDDMMYYYYHHMMSS()+".pdf");

	        return ResponseEntity.ok()
	                .headers(headers)
	                .contentType(MediaType.APPLICATION_PDF)
	                .body(bis.readAllBytes());
		}
}
