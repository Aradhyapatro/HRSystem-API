package com.HRSystem.Project.Controller;

import java.io.ByteArrayInputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.HRSystem.Project.Service.excelService;
import com.HRSystem.Project.Service.timeStampService;

@Controller
@RequestMapping("/api/excel")
public class ExcelExportController {
	@Autowired
	private excelService excel;
	@Autowired
	private timeStampService time;
	
	@GetMapping("/Users")
	public ResponseEntity<InputStreamResource> downloadUserssExcel() {
        ByteArrayInputStream in = excel.getUserExcel();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=users_"+time.timeStampDDMMYYYYHHMMSS()+".xlsx");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(new InputStreamResource(in));
    }
	
	@GetMapping("/Departments")
	public ResponseEntity<InputStreamResource> downloadDepartmentsExcel() {
        ByteArrayInputStream in = excel.getDepartmentExcel();
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "Departments_"+time.timeStampDDMMYYYYHHMMSS()+".xlsx");


        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(new InputStreamResource(in));
    }
}
