package vttp2022.paf.assessment.eshop.services;

import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import vttp2022.paf.assessment.eshop.models.LineItem;
import vttp2022.paf.assessment.eshop.models.Order;
import vttp2022.paf.assessment.eshop.models.OrderStatus;

@Service
public class WarehouseService {

	// You cannot change the method's signature
	// You may add one or more checked exceptions
	public OrderStatus dispatch(Order order) throws URISyntaxException {
		String orderId = order.getOrderId();
		String urlString = "http://paf.chuklee.com/dispatch/%s".formatted(orderId);

		System.out.println("URL String >> " + urlString);

		// Create JSON Body
		JsonObjectBuilder lineItemJob = Json.createObjectBuilder();
		JsonArrayBuilder lineItemArr = Json.createArrayBuilder();

		for (LineItem lineItem : order.getLineItems()) {
			lineItemArr.add(lineItemJob
					.add("item", lineItem.getItem())
					.add("quantity", lineItem.getQuantity()));
		}

		JsonObject payload = Json.createObjectBuilder()
				.add("orderId", orderId)
				.add("name", order.getName())
				.add("address", order.getAddress())
				.add("email", order.getEmail())
				.add("lineItems", lineItemArr)
				.add("createdBy", "Woo Kai Kein")
				.build();

		// Call Server
		ResponseEntity<JsonObject> responseObject = fetch(urlString, payload);

		// Setting Order Status
		OrderStatus orderStatus = new OrderStatus();
		orderStatus.setOrderId(orderId);
		if (null == responseObject || !responseObject.getStatusCode().equals(HttpStatus.OK)) {
			return orderStatus;
		}
		orderStatus.setDeliveryId(responseObject.getBody().getString("deliveryId"));
		orderStatus.setStatus("dispatched");
		return orderStatus;
	}

	private ResponseEntity<JsonObject> fetch(String url, JsonObject payload) throws URISyntaxException {
		RestTemplate template = new RestTemplate();
		URI uri = new URI(url);
		RequestEntity<JsonObject> req = RequestEntity.post(uri).body(payload);
		try {
			ResponseEntity<JsonObject> res = template.exchange(req, JsonObject.class);
			return res;
		} catch (Exception e) {
			System.out.println("Helper Method Error >>" + e.getMessage());
			return null;
		}
	}
}
