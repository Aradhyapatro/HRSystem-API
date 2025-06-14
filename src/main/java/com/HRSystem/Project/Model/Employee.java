package com.HRSystem.Project.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Employee")
@Data
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Employee {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)	
	private long Soeid;
	
	@Column
	private String Fname;
	@Column
	private String Lname;
	@Column
	private String Phone;
	@Column
	private String Address;

	@ManyToOne
	@JoinColumn(name = "Department_id")
	private Department department;
	
	@OneToOne
	@JoinColumn(name = "Soeid")
	private User user;

	public Employee(String fname, String lname, String phone, String address) {
		super();
		Fname = fname;
		Lname = lname;
		Phone = phone;
		Address = address;
	}
}
