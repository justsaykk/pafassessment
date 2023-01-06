package vttp2022.paf.assessment.eshop.models;

import lombok.Data;

@Data
public class CustomerOrders {
    private String name;
    private int dispatched;
    private int pending;
}
