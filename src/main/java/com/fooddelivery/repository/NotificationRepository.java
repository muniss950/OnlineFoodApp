package com.fooddelivery.repository;

import com.fooddelivery.model.DeliveryAgent;
import com.fooddelivery.model.Notification;
import com.fooddelivery.model.User;
import com.fooddelivery.notifications.ObserverType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUserAndObserverType(User user, ObserverType observerType);
    List<Notification> findByObserverType(ObserverType observerType);
    List<Notification> findByDeliveryAgentAndObserverType(DeliveryAgent agent, ObserverType observerType);
    int countByUserAndObserverTypeAndReadFalse(User user, ObserverType observerType);
    int countByObserverTypeAndReadFalse(ObserverType observerType);
    int countByDeliveryAgentAndObserverTypeAndReadFalse(DeliveryAgent agent, ObserverType observerType);

    List<Notification> findByUser(User user);
    List<Notification> findByDeliveryAgent(DeliveryAgent agent);
    @Query("SELECT n FROM Notification n WHERE n.user = :user AND n.observerType = com.fooddelivery.notifications.ObserverType.CUSTOMER ORDER BY n.createdAt DESC")
    List<Notification> findByUserOrderByCreatedAtDesc(@Param("user") User user);

    @Query("SELECT n FROM Notification n WHERE n.observerType = com.fooddelivery.notifications.ObserverType.ADMIN ORDER BY n.createdAt DESC")
    List<Notification> findByAdminOrderByCreatedAtDesc(@Param("user") User user);
    
    @Query("SELECT n FROM Notification n WHERE n.user = :user AND n.read = false  AND n.observerType = com.fooddelivery.notifications.ObserverType.CUSTOMER ORDER BY n.createdAt DESC")
    List<Notification> findByUserAndReadFalseOrderByCreatedAtDesc(@Param("user") User user);
    
    @Modifying
    @Query("UPDATE Notification n SET n.read = true WHERE n.id = :id  AND n.observerType = com.fooddelivery.notifications.ObserverType.CUSTOMER ")
    void markAsRead(@Param("id") Long id);
    @Modifying
    @Query("UPDATE Notification n SET n.read = true WHERE n.id = :id  AND n.observerType = com.fooddelivery.notifications.ObserverType.ADMIN ")
    void markAsReadAdmin(@Param("id") Long id);

    @Modifying
    @Query("UPDATE Notification n SET n.read = true WHERE n.id = :id  AND n.observerType = com.fooddelivery.notifications.ObserverType.DELIVERY_AGENT ")
    void markAsReadforAgent(@Param("id") Long id);
    @Modifying
    @Query("UPDATE Notification n SET n.read = true WHERE n.user = :user  AND n.observerType = com.fooddelivery.notifications.ObserverType.CUSTOMER")
    void markAllAsRead(@Param("user") User user);

    @Modifying
    @Query("UPDATE Notification n SET n.read = true WHERE n.observerType = com.fooddelivery.notifications.ObserverType.ADMIN")
    void markAllAsReadAdmin(@Param("user") User user);
    
    @Query("SELECT n FROM Notification n WHERE n.deliveryAgent = :agent AND n.observerType = com.fooddelivery.notifications.ObserverType.DELIVERY_AGENT ORDER BY n.createdAt DESC")
    List<Notification> findByDeliveryAgentOrderByCreatedAtDesc(@Param("agent") DeliveryAgent agent);

    @Query("SELECT n FROM Notification n WHERE n.deliveryAgent = :agent AND n.read = false AND n.observerType = com.fooddelivery.notifications.ObserverType.DELIVERY_AGENT ORDER BY n.createdAt DESC")
    List<Notification> findByDeliveryAgentAndReadFalseOrderByCreatedAtDesc(@Param("agent") DeliveryAgent agent);

    @Query("SELECT n FROM Notification n WHERE n.read = false  AND n.observerType = com.fooddelivery.notifications.ObserverType.ADMIN ORDER BY n.createdAt DESC")
    List<Notification> findByAdminAndReadFalseOrderByCreatedAtDesc(@Param("user") User user);
    
    @Modifying
    @Query("UPDATE Notification n SET n.read = true WHERE n.id = :id AND n.observerType = com.fooddelivery.notifications.ObserverType.DELIVERY_AGENT")
    void markAsReadForAgent(@Param("id") Long id);
    
    @Modifying
    @Query("UPDATE Notification n SET n.read = true WHERE n.deliveryAgent = :agent AND n.observerType = com.fooddelivery.notifications.ObserverType.DELIVERY_AGENT")
    void markAllAsReadForAgent(@Param("agent") DeliveryAgent agent);
}
