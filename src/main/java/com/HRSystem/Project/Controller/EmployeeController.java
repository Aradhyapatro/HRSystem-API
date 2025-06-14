package com.HRSystem.Project.Controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.HRSystem.Project.DTO.RegisterDTO;
import com.HRSystem.Project.Model.Employee;
import com.HRSystem.Project.Service.JwtService;
import com.HRSystem.Project.Service.employeeService;

@RestController
@RequestMapping("/api/emp")
public class EmployeeController {
	@Autowired
	private employeeService emp;
	@Autowired
	private JwtService jwt;
	
	@GetMapping("/")
	public ResponseEntity<List<Employee>> getEmployees(){
		return emp.getAll();
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> getEmployeeById(@PathVariable Long id){
		return emp.getById(id);
	}
	
	@PutMapping("/update/{id}")
	public ResponseEntity<?> updateEmployee(@RequestBody Employee e,@PathVariable Long id){
		return emp.updateById(e, id);
	}
	
	@PutMapping("/updateAll/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> updateAllData(@PathVariable Long id,@RequestBody RegisterDTO update){
		return emp.updateAll(update,id);
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteId(@PathVariable Long id){
		return emp.deleteById(id);
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/all")
	public ResponseEntity<?> deleteAll(){
		emp.deleteAll();
		return ResponseEntity.status(HttpStatus.ACCEPTED).body("Deleted all Data");
	}
	
	@PostMapping("/registerAll")
	public ResponseEntity<?> postAllEmp(@RequestBody List<RegisterDTO> list) {
		emp.postAll(list);
		return ResponseEntity.ok("Regitered all data");
	}
	
	@PostMapping("/Register")
	public ResponseEntity<?> postEmp(@RequestBody RegisterDTO item) {
		emp.post(item);
		return ResponseEntity.ok("Regitered the data");
	}
	

	@GetMapping("/id")
	public ResponseEntity<?> getMyId(@RequestHeader("Authorization") String token){
		String name = jwt.extractUsername(token.substring(7));
		Long id = emp.getId(name);
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(id);
	}
}
