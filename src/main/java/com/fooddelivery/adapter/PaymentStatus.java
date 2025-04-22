package com.fooddelivery.adapter;

/**
 * Represents the status of a payment
 */
public enum PaymentStatus {
    PENDING,
    PROCESSING,
    COMPLETED,
    FAILED,
    REFUNDED,
    PARTIALLY_REFUNDED,
    CANCELLED,
    UNKNOWN
} 