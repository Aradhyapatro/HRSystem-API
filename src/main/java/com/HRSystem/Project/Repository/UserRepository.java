package com.HRSystem.Project.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.HRSystem.Project.Model.Employee;
import com.HRSystem.Project.Model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByUsername(String username);

	void save(Employee emp);
}
