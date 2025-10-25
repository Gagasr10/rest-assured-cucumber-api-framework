package com.example.api.models.orders;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderCreateRequest {
    private int toolId;
    private String customerName;

    
    public OrderCreateRequest(int toolId, String customerName) {
        this.toolId = toolId;
        this.customerName = customerName;
    }
}
