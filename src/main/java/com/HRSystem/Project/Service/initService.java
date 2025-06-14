package com.HRSystem.Project.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.HRSystem.Project.Model.Department;
import com.HRSystem.Project.Model.Employee;
import com.HRSystem.Project.Model.User;
import com.HRSystem.Project.Repository.UserRepository;
import com.HRSystem.Project.Repository.departmentRepository;
import com.HRSystem.Project.Repository.employeeRepository;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;

@Component
public class initService {
	@Autowired
	private departmentRepository dep;
	@Autowired
	private UserRepository user; 
	@Autowired
	private employeeRepository emp;
	
	
	@PostConstruct
	public void init() {
		 int l = emp.findAll().size();
		 
		 if(l==0) {
			// Step 1: Create department (without dhead initially)
	            Department department = new Department();
	            department.setDname("Engineering");
	            department.setDhead(null);
	            department = dep.save(department); // Save and get generated ID
	            System.out.println(department);
	            
	         // Step 3: Create employee and associate with department + user
	            Employee employee = new Employee();
	            employee.setAddress("Bijiput");
	            employee.setDepartment(null);;
	            employee.setFname("Aradhya");
	            employee.setLname("Patro");
	            employee.setPhone("123456789");
	            employee.setSoeid(0); // Reuse soeid for both user and employee
	            employee.setUser(null);
	            employee = emp.save(employee);
	            System.out.println(employee);
	            
	            User u = new User();
	            u.setSoeid(employee.getSoeid());
	            u.setPassword("$2a$10$FEGJqBFnrKRaBlH1.qofU.5xNvY7mtqTRzjvPqHF6W4KM/eQ0U0tq");
	            u.setRole("ADMIN");
	            u.setUsername("aradhyapatro14@gmail.com");
	            u=user.save(u);

	            employee.setUser(u);
	            employee.setDepartment(department);
	            emp.save(employee);

	            department.setDhead(employee);
	            dep.save(department); // update with foreign key now resolved
		 }
	}
}
