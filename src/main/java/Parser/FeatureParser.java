package Parser;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import io.cucumber.messages.internal.com.google.common.io.Files;

public class FeatureParser {

	private MongoDatabase database;
	private MongoCollection<Document> collection;
	private static String server_base_url = "http://localhost:5000/test-data";

	// Helper method to connect to the MongoDb database.
	public void connectToDatabase() {
		MongoClientURI uri = new MongoClientURI(
				"mongodb+srv://vineet:OWuoi4YyHlCLfXnR@testdata.0iza9.mongodb.net/cucumber-test-data?retryWrites=true&w=majority");

		MongoClient mongoClient = new MongoClient(uri);
		database = mongoClient.getDatabase("cucumber-test-data");
		collection = database.getCollection("testData");
	}

	// Replaces all annotations that begin with a #! followed by scenario name with
	// the required data table.
	public void replaceMarkersInFeatureFile(String fileLocation) throws IOException {

		String fileContent = Files.asCharSource(new File(fileLocation), StandardCharsets.UTF_8).read();

		Pattern pattern = Pattern.compile("#!(.*)\\n*");
		Matcher matcher = pattern.matcher(fileContent);
		FileWriter writer = new FileWriter(fileLocation, false);

		while (matcher.find()) {
			String text = matcher.group(1);
			String dataTable = getDataTable(text);
			if (!dataTable.equals("|")) {
				fileContent = fileContent.replaceAll("#!" + text, "#!" + text + "\n" + "Examples:\n" + dataTable);
				System.out.println("Replaced \"#!" + text + "\" with data table successfully");
				try {
					setScenarioToUsed(text);
				} catch (UnirestException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				fileContent = fileContent.replaceAll("#!" + text, "#Data table not found");
			}
		}

		writer.write(fileContent);
		writer.close();

	}

	private void setScenarioToUsed(String scenario_name) throws UnirestException {
		// TODO Auto-generated method stub
		HttpResponse<JsonNode> jsonResponse = Unirest.patch(server_base_url + "/update/" + scenario_name)
				.header("Content-Type", "application/json").body("{ \"is_used\" : true}").asJson();
		System.out.println("Http response status : " + jsonResponse.getStatusText());

	}

	public void revertDataTablesToMarkers(String fileLocation) throws IOException {
		String fileContent = Files.asCharSource(new File(fileLocation), StandardCharsets.UTF_8).read();
		FileWriter writer = new FileWriter(fileLocation, false);

		fileContent = fileContent.replaceAll("(Examples\\:[\s]*\n(\\|.*\\|\n)*)", "");
		fileContent = fileContent.replaceAll("\n\n", "\n");
		System.out.println("Reverted");

		writer.write(fileContent);
		writer.close();
	}

	// Helper method that constructs a data table, given the Scenario name
	private String getDataTable(String text) {
		Document document = new Document("scenario_name", text);
		document.append("is_used", false);

		FindIterable<Document> res = collection.find(document);
		StringBuilder dataTableString = new StringBuilder("|");
		ArrayList<String> columns = null;
		ArrayList<Document> DbDataTable = null;
		for (Document d : res) {
			columns = (ArrayList<String>) d.get("columns");

			for (String column : columns) {
				dataTableString.append(column + "|");
			}
			dataTableString.append("\n");

			DbDataTable = (ArrayList<Document>) d.get("example_table");

			// For each row of the data table.
			for (Document row : DbDataTable) {

				dataTableString.append("|");
				// This loop is to maintain the order of columns.
				for (int i = 0; i < columns.size(); i++) {
					dataTableString.append(row.get(columns.get(i)) + "|");
				}

				dataTableString.append("\n");
			}
		}

		return dataTableString.toString();
	}

	public static void main(String[] args) {
		FeatureParser parser = new FeatureParser();
		parser.connectToDatabase();
		String filepath = "src/test/resources/Features/login.feature";
		try {
			parser.replaceMarkersInFeatureFile(filepath);
			parser.revertDataTablesToMarkers(filepath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
