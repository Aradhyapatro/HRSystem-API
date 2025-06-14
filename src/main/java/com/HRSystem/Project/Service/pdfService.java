package com.HRSystem.Project.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.HRSystem.Project.DTO.DepartmentDTO;
import com.HRSystem.Project.Model.Employee;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

@Service
public class pdfService {
	@Autowired
	private departmentService dep;
	@Autowired
	private employeeService emp;
	
	public ByteArrayOutputStream departmentPDF() {
		Document document = new Document();
	    ByteArrayOutputStream out = new ByteArrayOutputStream();
        List<DepartmentDTO> list = dep.getDepartment();	    
	    
	    try {
	        PdfWriter.getInstance(document, out);
	        document.open();

	        // Title
	        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
	        Paragraph title = new Paragraph("Department List", titleFont);
	        title.setAlignment(Element.ALIGN_CENTER);
	        document.add(title);
	        document.add(Chunk.NEWLINE);

	        // Table with 3 columns
	        PdfPTable table = new PdfPTable(3);
	        table.setWidthPercentage(100);
	        table.setWidths(new float[]{2, 4, 4});

	        // Header row
	        Stream.of("Department ID", "Name", "Head SOEID").forEach(col -> {
	            PdfPCell header = new PdfPCell();
	            Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
	            header.setPhrase(new Phrase(col, headFont));
	            header.setHorizontalAlignment(Element.ALIGN_CENTER);
	            table.addCell(header);
	        });

	        // Data rows
	        for (DepartmentDTO dept : list) {
	            table.addCell(String.valueOf(dept.getDepartmentId()));
	            table.addCell(dept.getDname());
	            table.addCell(String.valueOf(dept.getSoeid()));
	        }

	        document.add(table);
	        document.close();

	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    
	    return out;
	}

	public ByteArrayOutputStream usersPDF() {
		Document document = new Document();
	    ByteArrayOutputStream out = new ByteArrayOutputStream();
        List<Employee> list = emp.getAll().getBody();	    
	    
	    try {
	        PdfWriter.getInstance(document, out);
	        document.open();

	        // Title
	        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
	        Paragraph title = new Paragraph("Users List", titleFont);
	        title.setAlignment(Element.ALIGN_CENTER);
	        document.add(title);
	        document.add(Chunk.NEWLINE);

	        // Table with 7 columns
	        PdfPTable table = new PdfPTable(7);
	        table.setWidthPercentage(100);
	        table.setWidths(new float[]{2, 4, 4,4,4,4,4});

	        // Header row
	        Stream.of("SoeID", "First Name", "Last Name","Phone","Addres","Username","Department").forEach(col -> {
	            PdfPCell header = new PdfPCell();
	            Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
	            header.setPhrase(new Phrase(col, headFont));
	            header.setHorizontalAlignment(Element.ALIGN_CENTER);
	            table.addCell(header);
	        });

	        // Data rows
	        for (Employee user : list) {
	            table.addCell(String.valueOf(user.getSoeid()));
	            table.addCell(user.getFname());
	            table.addCell(user.getLname());
	            table.addCell(user.getPhone());
	            table.addCell(user.getAddress());
	            table.addCell(user.getUser().getUsername());
	            table.addCell(user.getDepartment().getDname());
	        }

	        document.add(table);
	        document.close();

	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    
	    return out;
	}
}
