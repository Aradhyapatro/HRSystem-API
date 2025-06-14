package com.HRSystem.Project.Service;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.HRSystem.Project.DTO.LoginDTO;
import com.HRSystem.Project.DTO.RegisterDTO;
import com.HRSystem.Project.Exception.DepartmentNotFoundException;
import com.HRSystem.Project.Exception.PasswordIncorrectException;
import com.HRSystem.Project.Exception.UserExistsException;
import com.HRSystem.Project.Model.Department;
import com.HRSystem.Project.Model.Employee;
import com.HRSystem.Project.Model.User;
import com.HRSystem.Project.Repository.UserRepository;
import com.HRSystem.Project.Repository.departmentRepository;
import com.HRSystem.Project.Repository.employeeRepository;

@Service
public class authService {
	@Autowired
	private UserRepository userRepo;
	@Autowired
	private departmentRepository departmentRepo;
	@Autowired
	private employeeRepository employeeRepo;
	@Autowired
	private PasswordEncoder passwordEncoder;

	public ResponseEntity<String> regitrationService(RegisterDTO register){
		Optional<Department> department_id = departmentRepo.findById(register.getDepartmentId());
		if(department_id.isEmpty()) {
			throw new DepartmentNotFoundException("Department does'nt Exist");
		}
		
		Optional<User> user_found = userRepo.findByUsername(register.getUsername());
		if(!user_found.isEmpty()) {
			throw new UserExistsException("Username Already Exists");
		}

		Employee emp = new Employee(register.getFname(),register.getLname(),register.getPhone(),register.getAddress());
		emp.setDepartment(department_id.get());
		Employee employ = employeeRepo.save(emp);
		
		User person = new User();
		person.setSoeid(employ.getSoeid());
		person.setUsername(register.getUsername());
		person.setPassword(passwordEncoder.encode(register.getPassword()));
		person.setRole(register.getRole().toUpperCase());
		userRepo.save(person);

		return ResponseEntity.ok("Registration Successfull");
	}
	
	public ResponseEntity<String> loginService(LoginDTO login){
		Optional<User> user_found = userRepo.findByUsername(login.getUsername());
		if(user_found.isEmpty()) {
			throw new UsernameNotFoundException("Username does'nt Exist");
		}
		
		User user = user_found.get();
		
		if(passwordEncoder.matches(login.getPassword(), user.getPassword())) {
			return ResponseEntity.ok("Login Successfull");
		}

		throw new PasswordIncorrectException("Wrong password");
	}
	
	public void onboarding(RegisterDTO register) {
		Optional<User> user_found = userRepo.findByUsername(register.getUsername());
		if(!user_found.isEmpty()) {
			return;
		}
		
		Employee emp = new Employee(register.getFname(),register.getLname(),register.getPhone(),register.getAddress());
		Employee employ = employeeRepo.save(emp);
		
		User person = new User();
		person.setSoeid(employ.getSoeid());
		person.setUsername(register.getUsername());
		person.setPassword(passwordEncoder.encode(register.getPassword()));
		person.setRole("USER");
		
		userRepo.save(person);
	}
}
