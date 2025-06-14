package com.HRSystem.Project.Service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.HRSystem.Project.Model.User;
import com.HRSystem.Project.Repository.UserRepository;

import java.security.Key;
import java.util.Date;
import java.util.Optional;

@Service
public class JwtService {
	@Autowired
	private UserRepository userRepo;

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    private Key key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String generateToken(String username) {
    	Optional<User> userOpt = userRepo.findByUsername(username);
    	if(userOpt.isEmpty()) {
    		throw new UsernameNotFoundException(username+" user not found");
    	}
    	User user = userOpt.get();
    	
        return Jwts.builder()
                .setSubject(username)
                .claim("role", user.getRole())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(key)
                .compact();
    }

    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean isTokenValid(String token, String username) {
        return extractUsername(token).equals(username) && !isTokenExpired(token);
    }
    
    public String isRole(String token) {
    	Claims claims = Jwts.parserBuilder()
    	        .setSigningKey(key)
    	        .build()
    	        .parseClaimsJws(token)
    	        .getBody();

    	String role = claims.get("role", String.class);
    	return role;
    }

    public boolean isTokenExpired(String token) {
        Date expiration = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
        return expiration.before(new Date());
    }
}
