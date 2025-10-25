package com.example.api.models.orders;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/** DTO for a single order returned by the API. */
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderResponse {
    private String orderId;
    private Integer toolId;
    private String customerName;
    private String status;     // may be absent/null on some responses
    private String createdAt;  // optional

    public OrderResponse() {}

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Integer getToolId() {
        return toolId;
    }

    public void setToolId(Integer toolId) {
        this.toolId = toolId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
