package com.HRSystem.Project.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterDTO {
	private Long departmentId;
	private String username;
	private String password;
	private String fname;
	private String lname;
	private String phone;
	private String address;
	private String role;
}
