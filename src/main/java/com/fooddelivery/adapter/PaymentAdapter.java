package com.fooddelivery.adapter;

import com.fooddelivery.model.Order;
import java.math.BigDecimal;

/**
 * Adapter pattern - Target interface
 * This interface defines the contract for payment processing that our application will use
 */
public interface PaymentAdapter {
    
    /**
     * Process a payment for an order
     * @param order The order to process payment for
     * @param amount The amount to charge
     * @return A payment result containing success status and transaction ID
     */
    PaymentResult processPayment(Order order, BigDecimal amount);
    
    /**
     * Refund a payment
     * @param transactionId The transaction ID of the payment to refund
     * @param amount The amount to refund
     * @return A payment result containing success status and refund ID
     */
    PaymentResult refundPayment(String transactionId, BigDecimal amount);
    
    /**
     * Get payment status
     * @param transactionId The transaction ID to check
     * @return The status of the payment
     */
    PaymentStatus getPaymentStatus(String transactionId);
} 