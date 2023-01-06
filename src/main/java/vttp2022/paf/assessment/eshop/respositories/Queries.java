package vttp2022.paf.assessment.eshop.respositories;

public class Queries {
        public static final String SQL_SAVE_ORDER = """
                        INSERT INTO orders(orderid, name, address, email, orderdate) values
                        (?,?,?,?,?)
                        """;
        public static final String SQL_SAVE_LINEITEMS = """
                        INSERT INTO order_details(orderid, item, quantity) values
                        (?,?,?)
                        """;
        public static final String SQL_FIND_CUSTOMER_BY_NAME = """
                        SELECT * FROM customers WHERE name LIKE ?
                        """;

        public static final String SQL_SUCCESSFUL_DISPATCH = """
                        INSERT INTO order_status(orderid, deliver_id, status, status_update) values
                        (?, ?, ?, ?)
                        """;
        public static final String SQL_UNSUCCESSFUL_DISPATCH = """
                        INSERT INTO order_status(orderid, status, status_update) values
                        (?, ?, ?)
                        """;
}
