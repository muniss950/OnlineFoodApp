package com.fooddelivery.notifications;
import com.fooddelivery.service.NotificationService;

import com.fooddelivery.model.Order;

public class OrderEvent {
    private final Order order;
    private final NotificationService notificationService;
    
    public OrderEvent(Order order, NotificationService notificationService) {
        this.order = order;
        this.notificationService = notificationService;
    }
    
    public void notifyObservers(EventType eventType) {
        switch (eventType) {
            case ORDER_PLACED:
                notificationService.notifyOrderPlaced(order);
                break;
            case ORDER_ASSIGNED:
                notificationService.notifyOrderAssigned(order);
                break;
            case ORDER_ACCEPTED:
                notificationService.notifyOrderAccepted(order);
                break;
            case ORDER_REJECTED:
                notificationService.notifyOrderRejected(order);
                break;
            case ORDER_DELIVERED:
                notificationService.notifyOrderDelivered(order);
                break;
            // Add more notification methods for other event types as needed
        }
    }
}



// package com.fooddelivery.notifications;
// import com.fooddelivery.model.Order;
// import com.fooddelivery.model.User;
// import com.fooddelivery.model.Notification;
// import com.fooddelivery.repository.NotificationRepository;
// import java.util.List;
// public class OrderEvent implements Subject {
//     private Order order;
//     private NotificationRepository notificationRepository;
//     public OrderEvent(Order order, NotificationRepository notificationRepository){
//         this.order = order;
//         this.notificationRepository = notificationRepository;
//     }
//     public void notifyObservers(EventType type){
//         User user = order.getUser();
//         if(type == EventType.ORDER_PLACED) {
//             Notification customerNotification = new Notification(EventType.ORDER_PLACED,
//             user, order);
//                 customerNotification.setTitle("Congragulations your order is placed!");
//                 customerNotification.setMessage("Your order with order ID " + order.getId() + " has been placed ")  ;
            

//             Notification adminNotification= new Notification(EventType.ORDER_PLACED,
//             user, order);
//                 adminNotification.setTitle("New Order arrived ");
//                 adminNotification.setMessage("order with order ID " + order.getId() + " has arrived from "
//                 + order.getUser().getUsername() + " assign a delivery agent soon");
//             notificationRepository.saveAll(List.of(customerNotification, adminNotification));
            
//         }
//         else if(type == EventType.ORDER_ASSIGNED){
//             Notification customerNotification = new Notification(EventType.ORDER_PLACED,
//             user, order);
//                 customerNotification.setDeliveryAgent(order.getDeliveryAgent());
//                 customerNotification.setTitle("Order " + order.getId() + " is assigned!");
//                 customerNotification.setMessage("your order has been assigned to " + order.getDeliveryAgent().getName() + "wait for the agent to accept the order! ")  ;

//             Notification adminNotification = new Notification(type, user, order);
//                 adminNotification.setDeliveryAgent(order.getDeliveryAgent());
//                 adminNotification.setTitle("Order " + order.getId() + " has been Assigned");
//                 adminNotification.setMessage("The order has been assigned to " + order.getDeliveryAgent().getName() + " look for changes ");
//             Notification agentNotification = new Notification(type, user, order);
//                 agentNotification.setDeliveryAgent(order.getDeliveryAgent());
//                 agentNotification.setTitle("New Order Assigned");
//                 agentNotification.setMessage("New Order has been assigned with order Id " + order.getId() + " please chose an action based on your preference");
//                 notificationRepository.saveAll(List.of(customerNotification, adminNotification, agentNotification));


//         }
//         else if(type == EventType.ORDER_REJECTED){
//             Notification customerNotification = new Notification(EventType.ORDER_PLACED,
//             user, order);
//                 customerNotification.setDeliveryAgent(null);
//                 customerNotification.setTitle("Order has been rejected");
//                 customerNotification.setMessage("your order has been rejected by our delivery agent . Please wait till we assign a new Delivery agent for your order . Thank you for your patience")  ;

//             Notification adminNotification = new Notification(type, user, order);
//                 adminNotification.setDeliveryAgent(null);
//                 adminNotification.setTitle("Order " + order.getId() + "Rejected ");
//                 adminNotification.setMessage("Order rejected by the agent assign a new agent soon");
//             Notification agentNotification = new Notification(type, user, order);
//                 agentNotification.setDeliveryAgent(null);
//                 agentNotification.setTitle("Rejected the order ");
//                 agentNotification.setMessage("You rejected the order "  + order.getId() +  " kindly wait for new orders" );
//                 notificationRepository.saveAll(List.of(customerNotification, adminNotification, agentNotification));
//         }
//         else if(type == EventType.ORDER_ACCEPTED) {
//             Notification customerNotification = new Notification(EventType.ORDER_PLACED,
//             user, order);
//                 customerNotification.setDeliveryAgent(order.getDeliveryAgent());
//                 customerNotification.setTitle("Order Accepted");
//                 customerNotification.setMessage("your order has been accepted by our delivery agent "+ order.getDeliveryAgent().getName() +  " Be ready to recieve the order " + "The payment would be " + order.getTotalDiscountedPrice())  ;

//             Notification adminNotification = new Notification(type, user, order);
//                 adminNotification.setDeliveryAgent(order.getDeliveryAgent());
//                 adminNotification.setTitle("Order " + order.getId() + "Accepted");
//                 adminNotification.setMessage("Order" + order.getId()   + "accepted by the agent " + order.getDeliveryAgent().getName() );
//             Notification agentNotification = new Notification(type, user, order);
//                 agentNotification.setDeliveryAgent(null);
//                 agentNotification.setTitle("You accepted the order  ");
//                 agentNotification.setMessage("You accepted the order "  + order.getId() +  " Deliver in time for to get the maximum benefits " );
//                 notificationRepository.saveAll(List.of(customerNotification, adminNotification, agentNotification));

//         }
//         else if(type == EventType.ORDER_DELIVERED){
//             Notification customerNotification = new Notification(EventType.ORDER_PLACED,
//             user, order);
//                 customerNotification.setDeliveryAgent(order.getDeliveryAgent());
//                 customerNotification.setTitle("Order Delivered");
//                 customerNotification.setMessage("your order has been delivered by our delivery agent "+ order.getDeliveryAgent().getName() +  " Confirm once if the order is delivered  " + "The amount to pay " + order.getTotalDiscountedPrice())  ;

//             Notification adminNotification = new Notification(type, user, order);
//                 adminNotification.setDeliveryAgent(order.getDeliveryAgent());
//                 adminNotification.setTitle("Order " + order.getId() + "Delivered");
//                 adminNotification.setMessage("Order" + order.getId()   + "delivered by the agent " + order.getDeliveryAgent().getName() + "agent is available to take further orders ");
//             Notification agentNotification = new Notification(type, user, order);
//                 agentNotification.setDeliveryAgent(null);
//                 agentNotification.setTitle(" Order delivered ");
//                 agentNotification.setMessage("Congratulations you Delivered the order  "  + order.getId() +  " We hope you are all set for future deliveries " );
//                 notificationRepository.saveAll(List.of(customerNotification, adminNotification, agentNotification));
//         }

//     }


// }
