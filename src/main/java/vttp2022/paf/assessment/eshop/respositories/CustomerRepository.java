package vttp2022.paf.assessment.eshop.respositories;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import vttp2022.paf.assessment.eshop.models.Customer;
import vttp2022.paf.assessment.eshop.models.CustomerOrders;

import static vttp2022.paf.assessment.eshop.respositories.Queries.*;

@Repository
public class CustomerRepository {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	// You cannot change the method's signature
	public Optional<Customer> findCustomerByName(String name) {
		SqlRowSet rs = jdbcTemplate.queryForRowSet(SQL_FIND_CUSTOMER_BY_NAME, name);

		if (!rs.next()) {
			return Optional.empty();
		}

		Customer customer = new Customer();
		customer.setName(rs.getString("name"));
		customer.setAddress(rs.getString("address"));
		customer.setEmail(rs.getString("email"));

		return Optional.of(customer);
	}

	public CustomerOrders getCustomerOrders(String name) {
		SqlRowSet rs = jdbcTemplate.queryForRowSet(SQL_FIND_ORDERS_BY_NAME, name);
		CustomerOrders customerOrders = new CustomerOrders();
		customerOrders.setName(name);

		if (!rs.next()) {
			customerOrders.setDispatched(0);
			customerOrders.setPending(0);
			return customerOrders;
		}

		int pendingOrders = 0;
		int dispatchedOrders = 0;
		if (rs.getString("status").equals("pending")) {
			pendingOrders += 1;
		} else {
			dispatchedOrders += 1;
		}

		while (rs.next()) {
			if (rs.getString("status").equals("pending")) {
				pendingOrders += 1;
			} else {
				dispatchedOrders += 1;
			}
		}
		customerOrders.setPending(pendingOrders);
		customerOrders.setDispatched(dispatchedOrders);
		return customerOrders;
	}
}
