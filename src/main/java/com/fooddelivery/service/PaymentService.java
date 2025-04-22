package com.fooddelivery.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.fooddelivery.model.Order;
import com.fooddelivery.model.Payment;
import com.fooddelivery.repository.PaymentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.math.BigDecimal;

@Service
public class PaymentService {
    private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);
    
    private final PaymentRepository paymentRepository;
    
    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }
    
    @Transactional
    public Payment processPayment(Order order, String paymentMethodId) {
        logger.info("Processing payment for order: {}", order.getId());
        
        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setAmount(BigDecimal.valueOf(order.getTotalAmount()));
        payment.setPaymentMethodId(paymentMethodId);
        payment.setStatus("COMPLETED");
        
        // Here you would integrate with a payment gateway
        // For now, we'll simulate a successful payment
        try {
            // Simulate payment processing
            Thread.sleep(1000);
            logger.info("Payment processed successfully for order: {}", order.getId());
        } catch (Exception e) {
            payment.setStatus("FAILED");
            logger.error("Payment processing failed for order: {}", order.getId(), e);
        }
        
        return paymentRepository.save(payment);
    }
    
    public Payment getPaymentByOrderId(Long orderId) {
        return paymentRepository.findByOrderId(orderId)
            .orElseThrow(() -> new RuntimeException("Payment not found for order: " + orderId));
    }
} 