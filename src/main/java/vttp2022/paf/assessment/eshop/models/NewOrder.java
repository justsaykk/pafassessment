package vttp2022.paf.assessment.eshop.models;

import java.util.List;

import lombok.Data;

@Data
public class NewOrder {
    private String name;
    private List<LineItem> lineItems;
}
