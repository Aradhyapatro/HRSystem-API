package com.HRSystem.Project.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.HRSystem.Project.DTO.LoginDTO;
import com.HRSystem.Project.DTO.RegisterDTO;
import com.HRSystem.Project.Service.JwtService;
import com.HRSystem.Project.Service.authService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	@Autowired
	private authService auth;
	@Autowired
	private JwtService jwt;
	
	@GetMapping("/")
	public String get() {
		return "Congratulations!!";
	}

	
	@PostMapping("/Register")
	public  ResponseEntity<String> register(@RequestBody RegisterDTO register) throws Exception {
		ResponseEntity<String> res= auth.regitrationService(register);		
		
		if(res.getStatusCode().is2xxSuccessful()) {
			String token = jwt.generateToken(register.getUsername());
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(token);
		}
		
		throw new Exception("Error in registration");
	}

	@PostMapping("/Login")
	public  ResponseEntity<?> login(@RequestBody LoginDTO Login) throws Exception {
		
		ResponseEntity<String> res = auth.loginService(Login);
		
		if(res.getStatusCode().is2xxSuccessful()) {
			String token = jwt.generateToken(Login.getUsername());
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(token);
		}
		
		throw new Exception("Error in Login");
	}
	
	@GetMapping("/getRole")
	public ResponseEntity<?> getRole(@RequestHeader("Authorization") String token){
		String res = jwt.isRole(token.substring(7));
		System.out.println("Aradhya here");
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(res);
	}
	
	@GetMapping("/isExpired")
	public ResponseEntity<Boolean> isExpired(@RequestHeader("Authorization") String token) {
		boolean res = jwt.isTokenExpired(token.substring(7));
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(res);
	}
	
	@PostMapping("/Onboarding")
	public ResponseEntity<?> onboardingEmp(@RequestBody RegisterDTO item){
		auth.onboarding(item);
		return ResponseEntity.ok("Employee Onboarded Department to be assigned");
	}
}
