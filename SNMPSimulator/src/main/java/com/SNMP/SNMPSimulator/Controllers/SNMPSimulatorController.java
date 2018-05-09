package com.SNMP.SNMPSimulator.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.SNMP.SNMPSimulator.Services.FileUploadService;
import com.SNMP.SNMPSimulator.Services.SNMPSimulatorService;

@RestController
@RequestMapping("/")
@CrossOrigin(origins = { "*" })
public class SNMPSimulatorController {

	
	@Autowired
	private FileUploadService fileUploadService;
	
	@Autowired
	private SNMPSimulatorService snmpSimulatorService;
	
	
	// code for New feature of sending traps from Excel file

	@PostMapping(value = "/uploadExcel", produces = "application/json")
	public ResponseEntity<String> createFileList(@RequestParam("fileDesc") String fileDesc,
			@RequestParam(required = false, value = "file") MultipartFile file) throws Exception {
		String message = null;

		try {

			fileUploadService.store(file);
			message = "File " + file.getName() + " uploaded successfully!!";

		} catch (Exception e) {

			throw new Exception(e.getMessage());
		}

		snmpSimulatorService.addDataToJSON(file.getName(), fileDesc);
		return new ResponseEntity<String>(message, HttpStatus.CREATED);
	}
	
	 @DeleteMapping(value = "/deleteFile/{fileName}", produces = "application/json")
		public SnmpJSONOutputModel deleteExcelFile(@PathVariable("fileName") String fileName) throws SnmpException{

			try {
				snmpSimulatorService.deleteExcelFile(fileDestination, fileName);
				snmpSimulatorService.updateJSON(fileName);
			} catch (Exception ex) {
				throw new SnmpException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
			}
			return snmpSimulatorService.prepareResponse(HttpStatus.OK, "Success", null);
		}
	 
	 
	 @GetMapping(value = "/getJSONData", produces = "application/json")
		public SnmpJSONOutputModel getJSONData() throws JsonParseException, JsonMappingException, IOException, SnmpException {
			String result = snmpSimulatorService.fetchJsonFileData();
			return snmpSimulatorService.prepareResponse(HttpStatus.OK, "Success",
					result);
		}

}
