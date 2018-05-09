package com.SNMP.SNMPSimulator.Services;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileUploadService {

	@Value("${fileupload-destination}")
	private String fileDestination;

	public void store(MultipartFile file) {
		try {
			Path rootLocation = Paths.get(fileDestination);
			
			
			// Get the file and save it somewhere
            /*byte[] bytes = file.getBytes();
            Path path = Paths.get(fileDestination + file.getOriginalFilename());
            Files.write(path, bytes);*/
			Files.copy(file.getInputStream(), rootLocation.resolve(file.getOriginalFilename()));
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

}
