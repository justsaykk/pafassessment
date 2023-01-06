package vttp2022.paf.assessment.eshop.models;

import java.util.List;

import lombok.ToString;

@ToString
public class NewOrder {
    private String name;
    private List<LineItem> lineItems;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<LineItem> getLineItems() {
        return lineItems;
    }

    public void setLineItems(List<LineItem> lineItems) {
        this.lineItems = lineItems;
    }

}
