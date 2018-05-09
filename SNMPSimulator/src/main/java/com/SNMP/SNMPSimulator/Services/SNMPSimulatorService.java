package com.SNMP.SNMPSimulator.Services;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Service
public class SNMPSimulatorService {

	@Value("${JSONFileDirecotry}")
	private String jsonFileDirectory;

	/****
	 * functions involving Excel file reader and JSON file modification designed
	 * below
	 * 
	 * @throws IOException
	 * @throws JSONException 
	 */
	// Update JSON file after deleting object
	public void updateJSON(String fileName) throws IOException, JSONException {
		ObjectMapper mapper = new ObjectMapper();

		// System.out.println(file.getAbsolutePath());
		// Read from file

		JSONObject root = mapper.readValue(new File(jsonFileDirectory + "jsonFile.json"), JSONObject.class);

		JSONArray jsonArray = new JSONArray(root.toString());

		int count = jsonArray.length(); // get totalCount of all jsonObjects
		for (int i = 0; i < count; i++) {
			// iterate through jsonArray
			JSONObject jsonObject = jsonArray.getJSONObject(i);

			if (jsonObject.get("fileName").equals(fileName)) {
				jsonArray.remove(i);
				break;
			}

		}

		// Write into the file
		try (FileWriter fileW = new FileWriter(jsonFileDirectory + "jsonFile.json")) {
			fileW.write(root.toString());
			System.out.println("Successfully updated json object to file...!!");
		}

	}

	// add object data to JSON file
	public void addDataToJSON(String fileName, String fileDesc) throws JSONException {
		File jsonFile = new File(jsonFileDirectory + "jsonFile.json");

		// Commons-IO
		String jsonString = null;
		try {
			jsonString = FileUtils.readFileToString(jsonFile);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Guava
		// String jsonString = Files.toString(jsonFile, Charsets.UTF_8);

		JsonElement jelement = new JsonParser().parse(jsonString);
		JsonArray jArrayObject = new JsonArray();
		if(jelement.isJsonNull())
		{
			jArrayObject = new JsonArray();
			JsonObject jo = new JsonObject();
			jo.addProperty("fileName", fileName);
			jo.addProperty("fileDesc", fileDesc);
			jArrayObject.add(jo);
		}
		else
		{
			jArrayObject = jelement.getAsJsonArray();
			JsonObject jo = new JsonObject();
			jo.addProperty("fileName", fileName);
			jo.addProperty("fileDesc", fileDesc);
			jArrayObject.add(jo);
		}
		
		
		Gson gson = new Gson();

		String resultingJson = gson.toJson(jArrayObject);
		// Commons-IO
		try {
			if(resultingJson!=null)
			   FileUtils.writeStringToFile(jsonFile, resultingJson);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Guava
		// Files.write(resultingJson, jsonFile, Charsets.UTF_8);
	}

	// To fetch all JSON file data for datatable
	public String fetchJsonFileData() throws IOException {
		File jsonFile = new File(jsonFileDirectory + "jsonFile.json");
		String jsonString = FileUtils.readFileToString(jsonFile);
		return jsonString;
	}

}
