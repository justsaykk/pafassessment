package vttp2022.paf.assessment.eshop.models;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import lombok.Data;

@Data
public class ServerRes {
    private String orderId;
    private String deliveryId;

    public JsonObject toJson() {
        return Json.createObjectBuilder()
                .add("orderId", this.orderId)
                .add("deliveryId", this.deliveryId)
                .build();
    }
}
