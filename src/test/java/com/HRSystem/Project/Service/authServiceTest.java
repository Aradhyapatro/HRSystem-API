package com.HRSystem.Project.Service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.argThat;



import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.HRSystem.Project.DTO.LoginDTO;
import com.HRSystem.Project.DTO.RegisterDTO;
import com.HRSystem.Project.Exception.DepartmentNotFoundException;
import com.HRSystem.Project.Exception.PasswordIncorrectException;
import com.HRSystem.Project.Exception.UserExistsException;
import com.HRSystem.Project.Model.Department;
import com.HRSystem.Project.Model.Employee;
import com.HRSystem.Project.Model.User;
import com.HRSystem.Project.Repository.UserRepository;
import com.HRSystem.Project.Repository.departmentRepository;
import com.HRSystem.Project.Repository.employeeRepository;

@ExtendWith(MockitoExtension.class)
public class authServiceTest {
	
    @Mock
    private UserRepository userRepo;

    @Mock
    private departmentRepository departmentRepo;

    @Mock
    private employeeRepository employeeRepo;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private authService authService;
    
    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }
	
	
    @Test
    public void testSuccessfulRegistration() {
        RegisterDTO dto = RegisterDTO.builder()
                .username("john123")
                .password("password")
                .role("USER")
                .fname("John")
                .lname("Doe")
                .phone("1234567890")
                .address("New York")
                .departmentId(1L)
                .build();

        Department dept = new Department();
        Employee emp = new Employee("John", "Doe", "1234567890", "New York");
        emp.setSoeid(101L); // assuming soeid is set

        when(departmentRepo.findById(1L)).thenReturn(Optional.of(dept));
        when(userRepo.findByUsername("john123")).thenReturn(Optional.empty());
        when(employeeRepo.save(any(Employee.class))).thenReturn(emp);
        when(passwordEncoder.encode("password")).thenReturn("hashed_password");

        ResponseEntity<String> response = authService.regitrationService(dto);

        assertEquals("Registration Successfull", response.getBody());
        
        verify(userRepo).save(any(User.class));
    }
    
    @Test
    void testRegistrationFailsWhenDepartmentNotFound() {
        RegisterDTO dto = RegisterDTO.builder()
                .username("john123")
                .departmentId(99L)
                .build();

        when(departmentRepo.findById(99L)).thenReturn(Optional.empty());

        assertThrows(DepartmentNotFoundException.class, () -> {
            authService.regitrationService(dto);
        });
    }

    @Test
    void testRegistrationFailsWhenUserAlreadyExists() {
        RegisterDTO dto = RegisterDTO.builder()
                .username("john123")
                .departmentId(1L)
                .build();

        Department dept = new Department();
        User existingUser = new User();

        when(departmentRepo.findById(1L)).thenReturn(Optional.of(dept));
        when(userRepo.findByUsername("john123")).thenReturn(Optional.of(existingUser));

        assertThrows(UserExistsException.class, () -> {
            authService.regitrationService(dto);
        });
    }
    
    @Test
    void testLoginSuccess() {
        // Arrange
        LoginDTO loginDTO = LoginDTO.builder()
                .username("user1")
                .password("password123")
                .build();

        User user = new User();
        user.setUsername("user1");
        user.setPassword("encodedPassword");

        when(userRepo.findByUsername("user1")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password123", "encodedPassword")).thenReturn(true);

        // Act
        ResponseEntity<String> response = authService.loginService(loginDTO);

        // Assert
        assertEquals("Login Successfull", response.getBody());
    }

    @Test
    void testLoginInvalidPassword() {
        LoginDTO loginDTO = LoginDTO.builder()
                .username("user1")
                .password("wrongPassword")
                .build();

        User user = new User();
        user.setUsername("user1");
        user.setPassword("encodedPassword");

        when(userRepo.findByUsername("user1")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongPassword", "encodedPassword")).thenReturn(false);

        assertThrows(PasswordIncorrectException.class, () -> {
            authService.loginService(loginDTO);
        });
    }

    @Test
    void testLoginUserNotFound() {
        LoginDTO loginDTO = LoginDTO.builder()
                .username("nonexistent")
                .password("any")
                .build();

        when(userRepo.findByUsername("nonexistent")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> {
            authService.loginService(loginDTO);
        });
    }
    
    @Test
    void testOnboarding_Successful() {
        // Arrange: mock input RegisterDTO
        RegisterDTO registerDTO = RegisterDTO.builder()
                .fname("Alice")
                .lname("Smith")
                .phone("9876543210")
                .address("221B Baker Street")
                .username("alice")
                .password("mypassword")
                .build();

        // User does not exist
        when(userRepo.findByUsername("alice")).thenReturn(Optional.empty());

        // Mock password encoding
        when(passwordEncoder.encode("mypassword")).thenReturn("encodedPass");

        // Mock saved employee with known Soeid
        Employee savedEmployee = new Employee("Alice", "Smith", "9876543210", "221B Baker Street");
        savedEmployee.setSoeid(10L);
        when(employeeRepo.save(any(Employee.class))).thenReturn(savedEmployee);

        // Act
        authService.onboarding(registerDTO);

        // Assert: verify employeeRepo.save called with expected data
        verify(employeeRepo).save(argThat(emp ->
                emp.getFname().equals("Alice") &&
                emp.getLname().equals("Smith") &&
                emp.getPhone().equals("9876543210") &&
                emp.getAddress().equals("221B Baker Street")
        ));

        // Assert: verify userRepo.save called with expected User object
        verify(userRepo).save(argThat((User user) ->
                user.getUsername().equals("alice") &&
                user.getPassword().equals("encodedPass") &&
                user.getSoeid().equals(10L) &&
                user.getRole().equals("USER")
        ));
    }




}
