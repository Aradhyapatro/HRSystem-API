package com.HRSystem.Project.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DepartmentDTO {
	private String dname;
	private Long soeid;
	private Long departmentId;
	
	public DepartmentDTO(String name,Long id){
		this.dname=name;
		this.soeid=id;
	}
}
