package com.HRSystem.Project.Repository;

import org.springframework.stereotype.Repository;
import com.HRSystem.Project.Model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface employeeRepository extends JpaRepository<Employee,Long>{

}
