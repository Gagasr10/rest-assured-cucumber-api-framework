package com.example.api.support;

/** Simple static holder for cross-step scenario state. */
public final class OrderState {
    private static final ThreadLocal<String> LAST_CREATED_ORDER_ID = new ThreadLocal<>();

    private OrderState() {}

    public static void setLastCreatedOrderId(String id) {
        LAST_CREATED_ORDER_ID.set(id);
    }

    public static String getLastCreatedOrderId() {
        return LAST_CREATED_ORDER_ID.get();
    }

    public static void clear() {
        LAST_CREATED_ORDER_ID.remove();
    }
}
