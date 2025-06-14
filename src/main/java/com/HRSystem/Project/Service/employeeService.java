package com.HRSystem.Project.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.HRSystem.Project.DTO.RegisterDTO;
import com.HRSystem.Project.Exception.EmployeeAlreadyHeadException;
import com.HRSystem.Project.Exception.EmployeeNotFoundException;
import com.HRSystem.Project.Model.Department;
import com.HRSystem.Project.Model.Employee;
import com.HRSystem.Project.Model.User;
import com.HRSystem.Project.Repository.UserRepository;
import com.HRSystem.Project.Repository.departmentRepository;
import com.HRSystem.Project.Repository.employeeRepository;

@Service
public class employeeService {
	@Autowired
	private UserRepository userRepo;
	@Autowired
	private departmentRepository departmentRepo;
	@Autowired
	private employeeRepository employeeRepo;
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	public ResponseEntity<List<Employee>> getAll(){
		List<Employee> emps = employeeRepo.findAll();
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(emps);
	}
	
	public ResponseEntity<List<Employee>> getAllAdmins(){
		List<Employee> emps = employeeRepo.findAll();
		List<Employee> empAdmins = new ArrayList<Employee>();
		for(Employee emp:emps) {
			if(emp.getUser().getRole().equalsIgnoreCase("ADMIN")) {
				empAdmins.add(emp);
			}
		}
		
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(empAdmins);
	}
	
	public ResponseEntity<Employee> getById(Long id){
		Optional<Employee> emp = employeeRepo.findById(id);
		if(emp.isEmpty()) {
			throw new EmployeeNotFoundException("Did not find the Employee ith id = "+id);
		}
		
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(emp.get());
	}
	
	public void postAll(List<RegisterDTO> list) {
		for(RegisterDTO item:list) {
			post(item);
		}
	}
	
	public  void post(RegisterDTO register) {
		Optional<Department> department_id = departmentRepo.findById(register.getDepartmentId());
		if(department_id.isEmpty()) {
			return;
		}
		
		Optional<User> user_found = userRepo.findByUsername(register.getUsername());
		if(!user_found.isEmpty()) {
			return;
		}
		
		Employee emp = new Employee(register.getFname(),register.getLname(),register.getPhone(),register.getAddress());
		emp.setDepartment(department_id.get());
		Employee employ = employeeRepo.save(emp);
		
		User person = new User();
		person.setSoeid(employ.getSoeid());
		person.setUsername(register.getUsername());
		person.setPassword(register.getPassword());
		person.setRole("USER");
		
		userRepo.save(person);
	}
	
	public void deleteAll() {
		userRepo.deleteAll();
		departmentRepo.deleteAll();
		employeeRepo.deleteAll();

	}
	
	public ResponseEntity<String> deleteById(Long id) {
		boolean b = departmentRepo.isDepartmentHead(id);
		
		if(b) {
			throw new EmployeeAlreadyHeadException("Employee is department head so cannot be deleted");
		}
		
		Optional<Employee> empOld = employeeRepo.findById(id);
		if(!empOld.isEmpty()) {
			Employee e = empOld.get();
			e.setUser(null);
			e.setDepartment(null);
			employeeRepo.delete(e);
			
			Optional<User> user = userRepo.findById(id);
			user.ifPresent(u -> userRepo.delete(u));

			return ResponseEntity.status(HttpStatus.ACCEPTED).body("Successfull Deletion of the id = "+id);
		}
		throw new EmployeeNotFoundException("Failed to find the Employee with the given id");
	}
	
	public ResponseEntity<Employee> updateById(Employee emp, Long id){
		Optional<Employee> empOld = employeeRepo.findById(id);
		if(!empOld.isEmpty()) {
			Employee e=empOld.get();
			e.setFname(emp.getFname()!=""?emp.getFname():e.getFname());
			e.setLname(emp.getLname()!=""?emp.getLname():e.getLname());
			e.setAddress(emp.getAddress()!=""?emp.getAddress():e.getAddress());
			e.setPhone(emp.getPhone()!=""?emp.getPhone():e.getPhone());
			
			employeeRepo.save(e);
			
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(e);
		}
		
		throw new EmployeeNotFoundException("Failed to find the Employee with the given id");
	}
	
	public Long getId(String name) {
		Optional<User> e = userRepo.findByUsername(name);
		if(e.isEmpty()) {
			throw new EmployeeNotFoundException(name+" name not found");
		}
		return e.get().getSoeid();
	}

	public ResponseEntity<?> updateAll(RegisterDTO emp, Long id) {
		System.out.println("ID =>"+id);
		System.out.println(emp);
		Optional<Employee> empOld = employeeRepo.findById(id);
		Optional<User> userOld = userRepo.findById(id);
		
		if(!empOld.isEmpty()) {
			Employee e=empOld.get();
			User u =userOld.get();
			e.setFname(emp.getFname()!=""?emp.getFname():e.getFname());
			e.setLname(emp.getLname()!=""?emp.getLname():e.getLname());
			e.setAddress(emp.getAddress()!=""?emp.getAddress():e.getAddress());
			e.setPhone(emp.getPhone()!=""?emp.getPhone():e.getPhone());
			u.setUsername(emp.getUsername()!=""?emp.getUsername():u.getUsername());
			u.setPassword(emp.getPassword()!=""? passwordEncoder.encode(emp.getPassword()):u.getPassword());
			u.setRole(emp.getRole()!=""?emp.getRole():u.getRole());
			
			if(emp.getDepartmentId()!=null) {
				Optional<Department> d = departmentRepo.findById(emp.getDepartmentId());
				if(!d.isEmpty()) {		
					e.setDepartment(d.get());
				}
			}
			
			employeeRepo.save(e);
			userRepo.save(u);
			
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(e);
		}
		
		throw new EmployeeNotFoundException("Failed to find the Employee with the given id");
	}
}
