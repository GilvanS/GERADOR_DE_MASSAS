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

	// ===== URL SIMPLIFICADA =====
	// Removemos os campos de produto, que agora são gerados localmente.
	private static final String FAKER_API_URL = "https://fakerapi.it/api/v1/custom?_quantity=1" +
			"&firstName=firstName" +
			"&lastName=lastName" +
			"&phoneNumber=phone" +
			"&password=password" +
			"&fullName=name" +
			"&addressLine1=streetAddress" +
			"&city=city" +
			"&stateRegion=state" +
			// "&zipCode=number" + // <-- LINHA REMOVIDA
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
	private String addressLine;
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
					EntityUtils.toString(response.getEntity()));

			ObjectMapper mapper = new ObjectMapper();
			JsonNode rootNode = mapper.readTree(jsonResponse);
			JsonNode dataNode = rootNode.path("data").path(0);

			// Mapeamento dos dados
			data.firstName = dataNode.path("firstName").asText();
			data.lastName = dataNode.path("lastName").asText();
			data.phoneNumber = dataNode.path("phoneNumber").asText();
			data.password = dataNode.path("password").asText();
			data.fullName = dataNode.path("fullName").asText();
			data.addressLine = dataNode.path("addressLine1").asText();
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
			if (articleContentNode.isArray() && articleContentNode.size() > 0) {
				data.articleContent = articleContentNode.get(0).asText();
			} else {
				data.articleContent = "Conteúdo do artigo não disponível.";
			}

		} catch (IOException e) {
			throw new IllegalStateException("Falha ao gerar dados da FakerAPI: " + e.getMessage(), e);
		}

		return data;
	}
}