package com.HRSystem.Project.Controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.HRSystem.Project.DTO.DepartmentDTO;
import com.HRSystem.Project.Model.Department;
import com.HRSystem.Project.Service.departmentService;

@RestController
@RequestMapping("/api/dep")
public class DepartmentController {
	@Autowired
	private departmentService dep;
	
	@GetMapping("/")
	public List<DepartmentDTO> getDep(){
		return  dep.getDepartment();
	}
	
	@PostMapping("/create")
	public Department createDep(@RequestBody DepartmentDTO department) {
		return dep.createDepartment(department);
	}
	
	@GetMapping("/{id}")
	public Department getDepartment(@PathVariable Long id) {
		return dep.getDepartmentById(id);
	}
}
