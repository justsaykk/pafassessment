package vttp2022.paf.assessment.eshop.controllers;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import vttp2022.paf.assessment.eshop.models.Customer;
import vttp2022.paf.assessment.eshop.models.CustomerOrders;
import vttp2022.paf.assessment.eshop.models.NewOrder;
import vttp2022.paf.assessment.eshop.models.Order;
import vttp2022.paf.assessment.eshop.models.OrderStatus;
import vttp2022.paf.assessment.eshop.respositories.CustomerRepository;
import vttp2022.paf.assessment.eshop.respositories.OrderRepository;

@RestController
@RequestMapping(path = "/api")
public class OrderController {

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private OrderRepository orderRepository;

	@PostMapping(path = "/order")
	public ResponseEntity<String> postOrder(
			@RequestBody NewOrder newOrder) {

		Optional<Customer> customerOpt = customerRepository.findCustomerByName(newOrder.getName());

		// Check if Customer Name exists in DB
		if (customerOpt.isEmpty()) {
			JsonObject jo = Json.createObjectBuilder()
					.add("error", "Customer %s not found".formatted(newOrder.getName()))
					.build();
			return new ResponseEntity<String>(jo.toString(), HttpStatus.NOT_FOUND);
		}

		// Instantiate new Order class to hold details
		Order order = new Order();
		order.setOrderId(UUID.randomUUID()
				.toString()
				.replace("-", "")
				.substring(0, 8));
		order.setOrderDate(new Date());
		order.setLineItems(newOrder.getLineItems());
		order.setCustomer(customerOpt.get());

		OrderStatus saveOrder = orderRepository.saveOrder(order);

		if (saveOrder.getStatus().equals("dispatched")) {
			JsonObject joResponse = Json.createObjectBuilder()
					.add("orderId", saveOrder.getOrderId())
					.add("deliveryId", saveOrder.getDeliveryId())
					.add("status", saveOrder.getStatus())
					.build();
			return new ResponseEntity<String>(joResponse.toString(), HttpStatus.OK);
		} else {
			JsonObject jo = Json.createObjectBuilder()
					.add("orderId", saveOrder.getOrderId())
					.add("status", saveOrder.getStatus())
					.build();
			return new ResponseEntity<String>(jo.toString(), HttpStatus.OK);
		}
	}

	@GetMapping(path = "/order/{name}/status")
	public ResponseEntity<String> getCustomerOrders(
			@PathVariable(name = "name") String name) {
		Optional<Customer> customerOpt = customerRepository.findCustomerByName(name);
		// Check if Customer Name exists in DB
		if (customerOpt.isEmpty()) {
			JsonObject jo = Json.createObjectBuilder()
					.add("error", "Customer %s not found".formatted(name))
					.build();
			return new ResponseEntity<String>(jo.toString(), HttpStatus.NOT_FOUND);
		}
		CustomerOrders customerOrders = customerRepository.getCustomerOrders(name);
		JsonObject jo = Json.createObjectBuilder()
				.add("name", customerOrders.getName())
				.add("dispatched", customerOrders.getDispatched())
				.add("pending", customerOrders.getPending())
				.build();
		return new ResponseEntity<String>(jo.toString(), HttpStatus.OK);
	}
}
