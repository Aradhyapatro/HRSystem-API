package com.HRSystem.Project.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Service;

@Service
public class timeStampService {
	public String timeStampDDMMYYYYHHMMSS() {
		LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HHmmss");
        String formattedTimestamp = now.format(formatter);
        
        return formattedTimestamp;
	}
}
