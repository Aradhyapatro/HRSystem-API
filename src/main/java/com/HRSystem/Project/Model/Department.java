package com.HRSystem.Project.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "Department")
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Department {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long Department_id;
	
	@Column(unique = true,nullable = false)
	private String Dname;
	
	@OneToOne
    @JoinColumn(name = "soeid",nullable = true)
	@JsonIgnore
    private Employee dhead;
	
	public Department(String name) {
		this.Dname=name;
	}
	
	@Override
	public String toString() {
	    return "Department{" +
	            ", name='" + this.Dname + '\'' +
	            // Avoid printing the entire employee list
	            '}';
	}
}
