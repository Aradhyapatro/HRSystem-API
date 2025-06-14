package com.HRSystem.Project.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.HRSystem.Project.Model.Department;
import com.HRSystem.Project.Model.Employee;
import com.HRSystem.Project.Repository.departmentRepository;
import com.HRSystem.Project.Repository.employeeRepository;


@Service
public class excelService {

    @Autowired
    private departmentRepository dep;
    @Autowired
    private employeeRepository emp;

    public ByteArrayInputStream getDepartmentExcel() {
        List<Department> d = dep.findAll();

        String[] headers = {"Department_id", "Name", "Head"};
        String sheetName = "DepartmentList";

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet(sheetName);

            Row headerRow = sheet.createRow(0);
            
            // Create bold font
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            // Create cell style and set the bold font
            CellStyle headerCellStyle = workbook.createCellStyle();
            headerCellStyle.setFont(headerFont);
            
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerCellStyle);
            }

            for (int i = 0; i < d.size(); i++) {
                Row row = sheet.createRow(i + 1);
                Department dept = d.get(i);
                row.createCell(0).setCellValue(dept.getDepartment_id());
                row.createCell(1).setCellValue(dept.getDname());
                row.createCell(2).setCellValue(dept.getDhead().getFname()+" "+dept.getDhead().getLname()+"("+dept.getDhead().getSoeid()+")");
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            e.printStackTrace(); // use proper logging
            return null;
        }
    }
    
    public ByteArrayInputStream getUserExcel() {
        List<Employee> e = emp.findAll();

        String[] headers = {"Soeid_id", "Name", "Phone","Address","Username","department"};
        String sheetName = "EmployeeList";

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet(sheetName);

            Row headerRow = sheet.createRow(0);
            
            // Create bold font
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            // Create cell style and set the bold font
            CellStyle headerCellStyle = workbook.createCellStyle();
            headerCellStyle.setFont(headerFont);
            
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerCellStyle);
            }

            for (int i = 0; i < e.size(); i++) {
                Row row = sheet.createRow(i + 1);
                Employee emp = e.get(i);
                row.createCell(0).setCellValue(emp.getSoeid());
                row.createCell(1).setCellValue(emp.getFname()+" "+emp.getLname());
                row.createCell(2).setCellValue(emp.getPhone());
                row.createCell(3).setCellValue(emp.getAddress());
                row.createCell(4).setCellValue(emp.getUser().getUsername());
                row.createCell(5).setCellValue(emp.getDepartment().getDname());
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException ex) {
            ex.printStackTrace(); // use proper logging
            return null;
        }
    }
}