package br.com.geradormassa.generators;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

@Getter
public final class FakerApiData {

	// ===== MUDANÇA 1: URL ATUALIZADA =====
	// Trocamos 'streetAddress' por 'streetName' e adicionamos 'buildingNumber'.
	private static final String FAKER_API_URL = "https://fakerapi.it/api/v1/custom?_quantity=1" +
			"&firstName=firstName" +
			"&lastName=lastName" +
			"&phoneNumber=phone" +
			"&password=password" +
			"&fullName=name" +
			"&streetName=streetName" +         // <-- MUDOU DE streetAddress
			"&buildingNumber=buildingNumber" + // <-- CAMPO NOVO
			"&city=city" +
			"&stateRegion=state" +
			"&country=country" +
			"&cardNumber=card_number" +
			"&expirationDate=card_expiration" +
			"&articleTitle=sentence" +
			"&articleContent=paragraphs";

	// Campos de usuário, endereço e artigo
	private String firstName;
	private String lastName;
	private String phoneNumber;
	private String password;
	private String fullName;
	private String addressLine; // Continuará sendo o nome da rua
	private String buildingNumber; // <-- CAMPO NOVO
	private String city;
	private String stateRegion;
	private String zipCode;
	private String country;
	private String cardNumber;
	private String cardFullName;
	private String expiryDate;
	private String cvv;
	private String articleTitle;
	private String articleContent;

	private FakerApiData() {}

	public static FakerApiData gerarDados() {
		FakerApiData data = new FakerApiData();

		try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
			HttpGet request = new HttpGet(FAKER_API_URL);
			String jsonResponse = httpClient.execute(request, response ->
					EntityUtils.toString(response.getEntity(), java.nio.charset.StandardCharsets.UTF_8));

			ObjectMapper mapper = new ObjectMapper();
			JsonNode rootNode = mapper.readTree(jsonResponse);
			JsonNode dataNode = rootNode.path("data").path(0);

			// ===== MUDANÇA 2: Mapeamento dos novos dados =====
			data.firstName = dataNode.path("firstName").asText();
			data.lastName = dataNode.path("lastName").asText();
			data.phoneNumber = dataNode.path("phoneNumber").asText();
			data.password = dataNode.path("password").asText();
			data.fullName = dataNode.path("fullName").asText();
			data.addressLine = dataNode.path("streetName").asText(); // <-- MUDOU DE addressLine1
			data.buildingNumber = dataNode.path("buildingNumber").asText(); // <-- CAMPO NOVO
			data.city = dataNode.path("city").asText();
			data.stateRegion = dataNode.path("stateRegion").asText();
			data.zipCode = dataNode.path("zipCode").asText();
			data.country = dataNode.path("country").asText();
			data.cardNumber = dataNode.path("cardNumber").asText();
			data.cardFullName = data.fullName;
			data.expiryDate = dataNode.path("expirationDate").asText();
			data.cvv = "123";
			data.articleTitle = dataNode.path("articleTitle").asText();

			JsonNode articleContentNode = dataNode.path("articleContent");
			if (articleContentNode.isArray() && !articleContentNode.isEmpty()) {
				StringBuilder contentBuilder = new StringBuilder();
				for (JsonNode paragraph : articleContentNode) {
					contentBuilder.append(paragraph.asText()).append("\n\n");
				}
				data.articleContent = contentBuilder.toString().trim();
			} else {
				data.articleContent = "Conteúdo do artigo não disponível.";
			}

		} catch (IOException e) {
			throw new IllegalStateException("Falha ao gerar dados da FakerAPI: " + e.getMessage(), e);
		}

		return data;
	}
}