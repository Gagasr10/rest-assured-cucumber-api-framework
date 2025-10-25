package com.example.api.models.orders;

/**
 * DTO used for partial updates (PATCH) on /orders/{orderId}.
 * Any non-null field will be sent to the server.
 */
public class OrderUpdateRequest {

    private String status;
    private String customerName;

    public OrderUpdateRequest() {
    }

    public OrderUpdateRequest(String status, String customerName) {
        this.status = status;
        this.customerName = customerName;
    }

    public static OrderUpdateRequest ofStatus(String status) {
        return new OrderUpdateRequest(status, null);
    }

    public static OrderUpdateRequest ofCustomerName(String customerName) {
        return new OrderUpdateRequest(null, customerName);
    }

    // Getters / Setters

    public String getStatus() {
        return status;
    }

    public OrderUpdateRequest setStatus(String status) {
        this.status = status;
        return this;
    }

    public String getCustomerName() {
        return customerName;
    }

    public OrderUpdateRequest setCustomerName(String customerName) {
        this.customerName = customerName;
        return this;
    }
}
