package com.HRSystem.Project.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.HRSystem.Project.Model.Department;

@Repository
public interface departmentRepository extends JpaRepository<Department, Long> {

	@Query("SELECT CASE WHEN COUNT(d) > 0 THEN true ELSE false END FROM Department d WHERE d.dhead.Soeid = :id")
	boolean isDepartmentHead(@Param("id") long soeid);
	
}
