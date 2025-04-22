package com.fooddelivery.adapter.impl;

import com.fooddelivery.adapter.PaymentAdapter;
import com.fooddelivery.adapter.PaymentResult;
import com.fooddelivery.adapter.PaymentStatus;
import com.fooddelivery.model.Order;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

/**
 * Adapter pattern - Concrete Adapter
 * This class adapts the Stripe payment API to our PaymentAdapter interface
 */
@Component
public class StripePaymentAdapter implements PaymentAdapter {
    
    // In a real implementation, this would be a client for the Stripe API
    private final StripeClient stripeClient;
    
    public StripePaymentAdapter() {
        // In a real implementation, this would be injected or configured
        this.stripeClient = new StripeClient();
    }
    
    @Override
    public PaymentResult processPayment(Order order, BigDecimal amount) {
        try {
            // Convert our order to Stripe's format
            StripePaymentRequest request = new StripePaymentRequest(
                order.getCustomer().getEmail(),
                amount.doubleValue(),
                "Order #" + order.getId(),
                order.getCustomer().getPaymentMethodId()
            );
            
            // Call Stripe's API
            StripePaymentResponse response = stripeClient.createPayment(request);
            
            // Convert Stripe's response to our format
            if (response.isSuccess()) {
                return PaymentResult.success(response.getTransactionId());
            } else {
                return PaymentResult.failure(response.getErrorMessage());
            }
        } catch (Exception e) {
            return PaymentResult.failure("Payment processing error: " + e.getMessage());
        }
    }
    
    @Override
    public PaymentResult refundPayment(String transactionId, BigDecimal amount) {
        try {
            // Call Stripe's refund API
            StripeRefundResponse response = stripeClient.createRefund(
                transactionId, 
                amount.doubleValue()
            );
            
            // Convert Stripe's response to our format
            if (response.isSuccess()) {
                return new PaymentResult(
                    true, 
                    response.getRefundId(), 
                    "Refund processed successfully", 
                    PaymentStatus.REFUNDED
                );
            } else {
                return PaymentResult.failure(response.getErrorMessage());
            }
        } catch (Exception e) {
            return PaymentResult.failure("Refund processing error: " + e.getMessage());
        }
    }
    
    @Override
    public PaymentStatus getPaymentStatus(String transactionId) {
        try {
            // Call Stripe's API to get payment status
            StripePaymentStatus stripeStatus = stripeClient.getPaymentStatus(transactionId);
            
            // Map Stripe's status to our status
            switch (stripeStatus) {
                case PENDING:
                    return PaymentStatus.PENDING;
                case PROCESSING:
                    return PaymentStatus.PROCESSING;
                case SUCCEEDED:
                    return PaymentStatus.COMPLETED;
                case FAILED:
                    return PaymentStatus.FAILED;
                case REFUNDED:
                    return PaymentStatus.REFUNDED;
                case PARTIALLY_REFUNDED:
                    return PaymentStatus.PARTIALLY_REFUNDED;
                case CANCELED:
                    return PaymentStatus.CANCELLED;
                default:
                    return PaymentStatus.UNKNOWN;
            }
        } catch (Exception e) {
            // Log the error and return a default status
            return PaymentStatus.UNKNOWN;
        }
    }
    
    // Mock classes for demonstration purposes
    private static class StripeClient {
        public StripePaymentResponse createPayment(StripePaymentRequest request) {
            // In a real implementation, this would call the Stripe API
            return new StripePaymentResponse(true, "txn_123456789", null);
        }
        
        public StripeRefundResponse createRefund(String transactionId, double amount) {
            // In a real implementation, this would call the Stripe API
            return new StripeRefundResponse(true, "ref_123456789", null);
        }
        
        public StripePaymentStatus getPaymentStatus(String transactionId) {
            // In a real implementation, this would call the Stripe API
            return StripePaymentStatus.SUCCEEDED;
        }
    }
    
    private static class StripePaymentRequest {
        private final String email;
        private final double amount;
        private final String description;
        private final String paymentMethodId;
        
        public StripePaymentRequest(String email, double amount, String description, String paymentMethodId) {
            this.email = email;
            this.amount = amount;
            this.description = description;
            this.paymentMethodId = paymentMethodId;
        }
    }
    
    private static class StripePaymentResponse {
        private final boolean success;
        private final String transactionId;
        private final String errorMessage;
        
        public StripePaymentResponse(boolean success, String transactionId, String errorMessage) {
            this.success = success;
            this.transactionId = transactionId;
            this.errorMessage = errorMessage;
        }
        
        public boolean isSuccess() {
            return success;
        }
        
        public String getTransactionId() {
            return transactionId;
        }
        
        public String getErrorMessage() {
            return errorMessage;
        }
    }
    
    private static class StripeRefundResponse {
        private final boolean success;
        private final String refundId;
        private final String errorMessage;
        
        public StripeRefundResponse(boolean success, String refundId, String errorMessage) {
            this.success = success;
            this.refundId = refundId;
            this.errorMessage = errorMessage;
        }
        
        public boolean isSuccess() {
            return success;
        }
        
        public String getRefundId() {
            return refundId;
        }
        
        public String getErrorMessage() {
            return errorMessage;
        }
    }
    
    private enum StripePaymentStatus {
        PENDING,
        PROCESSING,
        SUCCEEDED,
        FAILED,
        REFUNDED,
        PARTIALLY_REFUNDED,
        CANCELED
    }
} 