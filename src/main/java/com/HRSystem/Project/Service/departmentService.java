package com.HRSystem.Project.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.HRSystem.Project.DTO.DepartmentDTO;
import com.HRSystem.Project.Exception.DepartmentNotFoundException;
import com.HRSystem.Project.Exception.EmployeeAlreadyHeadException;
import com.HRSystem.Project.Exception.EmployeeNotFoundException;
import com.HRSystem.Project.Model.Department;
import com.HRSystem.Project.Model.Employee;
import com.HRSystem.Project.Repository.departmentRepository;
import com.HRSystem.Project.Repository.employeeRepository;

@Service
public class departmentService {
	@Autowired
	private departmentRepository dep;
	@Autowired
	private employeeRepository emp;
	
	public List<DepartmentDTO> getDepartment() {
		List<Department> d=dep.findAll();
		List<DepartmentDTO> dd=new ArrayList<DepartmentDTO>();
		for(Department dp:d) {
			DepartmentDTO temp = new DepartmentDTO(dp.getDname(),dp.getDhead().getSoeid(),dp.getDepartment_id());
			
			dd.add(temp);
		}
		
		return dd;
	}
	
	public Department getDepartmentById(Long id) {
		Optional<Department> d= dep.findById(id);
		if(d.isEmpty()) {
			throw new DepartmentNotFoundException("Did not find the department");
		}
		
		return d.get();
	}
	
	public Department createDepartment(DepartmentDTO department) {
		Department d=new Department(department.getDname());
		Optional<Employee> eo = emp.findById(department.getSoeid());
		if(eo.isEmpty()) {
			throw new EmployeeNotFoundException(department.getSoeid()+" id does'nt exist");
		}
		Employee e=eo.get();
		if(dep.isDepartmentHead(e.getSoeid())) {
			throw new EmployeeAlreadyHeadException("Employee is already the head of a department");
		}
		e.setDepartment(d);
		d.setDhead(e);
		
		dep.save(d);
		return d;
	}
}
