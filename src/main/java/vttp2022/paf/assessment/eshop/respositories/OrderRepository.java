package vttp2022.paf.assessment.eshop.respositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import vttp2022.paf.assessment.eshop.models.LineItem;
import vttp2022.paf.assessment.eshop.models.Order;
import vttp2022.paf.assessment.eshop.models.OrderStatus;
import vttp2022.paf.assessment.eshop.services.WarehouseService;

import static vttp2022.paf.assessment.eshop.respositories.Queries.*;

import java.util.Date;

@Repository
@Transactional
public class OrderRepository {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private WarehouseService warehouseService;

	public OrderStatus saveOrder(Order order) {
		System.out.printf(">> Number of Line Items in order: %d\n", order.getLineItems().size());
		// Save Order
		String orderid = order.getOrderId();
		try {
			jdbcTemplate.update(SQL_SAVE_ORDER, orderid, order.getName(), order.getAddress(),
					order.getEmail(), order.getOrderDate());

			for (LineItem lineItem : order.getLineItems()) {
				System.out.printf("Adding %s into SQL_SAVE_LINEITEMS\n", lineItem.getItem());
				jdbcTemplate.update(SQL_SAVE_LINEITEMS, orderid, lineItem.getItem(), lineItem.getQuantity());
			}

		} catch (Exception e) {
			System.err.println(e.getMessage());
		}

		// Dispatch order
		OrderStatus orderStatus = warehouseService.dispatch(order);
		updateOrderStatus(orderStatus);
		return orderStatus;
	}

	private void updateOrderStatus(OrderStatus orderStatus) {
		System.out.println(">> Updating Order Status");
		String orderId = orderStatus.getOrderId();

		if (orderStatus.getStatus().equals("pending")) {
			jdbcTemplate.update(SQL_UNSUCCESSFUL_DISPATCH, orderId, orderStatus.getStatus(), new Date());
		} else {
			jdbcTemplate.update(SQL_SUCCESSFUL_DISPATCH, orderId, orderStatus.getDeliveryId(), orderStatus.getStatus(),
					new Date());
		}
	}
}
