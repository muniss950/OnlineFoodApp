# Food Delivery Application

A Spring Boot-based food delivery application that allows customers to order food from restaurants, restaurants to manage their menus and orders, and delivery agents to handle deliveries.

## Object-Oriented Design Patterns

### 1. Factory Pattern
- **Implementation**: `UserFactoryManager` class
- **Purpose**: Creates different types of users based on their role
- **Usage**: 
  ```java
  // Creates a Customer user
  User customer = userFactoryManager.createUser(UserRole.CUSTOMER, username, password, email);
  // Creates a Restaurant user
  User restaurant = userFactoryManager.createUser(UserRole.RESTAURANT, username, password, email);
  ```

### 2. Builder Pattern
- **Implementation**: `OrderBuilder` class
- **Purpose**: Constructs Order objects with multiple optional parameters
- **Usage**:
  ```java
  Order order = new OrderBuilder()
      .setCustomer(customer)
      .setRestaurant(restaurant)
      .setOrderItems(orderItems)
      .setPaymentMethod(paymentMethod)
      .build();
  ```

### 3. Facade Pattern
- **Implementation**: `OrderProcessingFacade` class
- **Purpose**: Provides a simplified interface to the complex order processing subsystem
- **Usage**:
  ```java
  // Handles the entire order process in one call
  orderProcessingFacade.processOrder(customer, restaurant, items, paymentMethod);
  ```

### 4. Adapter Pattern
- **Implementation**: `PaymentGatewayAdapter` class
- **Purpose**: Adapts different payment gateway interfaces to a common interface
- **Usage**:
  ```java
  // Adapts different payment gateways to a common interface
  PaymentGatewayAdapter adapter = new PaymentGatewayAdapter(paymentGateway);
  PaymentResult result = adapter.processPayment(order);
  ```

## SOLID Principles Implementation

### 1. Single Responsibility Principle (SRP)
- `UserFactoryManager`: Solely responsible for user creation
- `OrderService`: Focused on order-related operations
- `PaymentService`: Dedicated to payment processing
- `NotificationService`: Handles only notification management

### 2. Open/Closed Principle (OCP)
- `UserFactory`: Extensible for new user types without modification
- `RecommendationStrategy`: Interface allowing new recommendation algorithms
- `PaymentGatewayAdapter`: Extensible for new payment gateways

### 3. Liskov Substitution Principle (LSP)
- User types (Customer, Restaurant, DeliveryAgent) are substitutable
- Recommendation strategies are interchangeable
- Payment methods can be substituted through adapter pattern

### 4. Interface Segregation Principle (ISP)
- `RecommendationStrategy`: Focused interface for recommendations
- `PaymentGateway`: Specific interface for payment processing
- `UserFactory`: Minimal interface for user creation

### 5. Dependency Inversion Principle (DIP)
- Services depend on abstractions (interfaces)
- Controllers depend on service interfaces
- Factory pattern implementation follows DIP

## GRASP Principles Implementation

### 1. Information Expert
- `OrderService`: Expert in order processing
- `UserService`: Expert in user management
- `PaymentService`: Expert in payment processing

### 2. Creator
- `UserFactoryManager`: Creates User objects
- `OrderService`: Creates Order objects
- `PaymentService`: Creates Payment objects

### 3. Controller
- Controllers handle HTTP requests and delegate to services
- `OrderController`: Manages order-related requests
- `CustomerController`: Handles customer operations

### 4. Low Coupling
- Services are loosely coupled through interfaces
- Factory pattern reduces coupling in user creation
- Adapter pattern decouples payment gateways

### 5. High Cohesion
- `UserFactoryManager`: High cohesion in user creation
- `OrderService`: High cohesion in order operations
- `PaymentService`: High cohesion in payment processing

### 6. Polymorphism
- User types polymorphism
- Recommendation strategies polymorphism
- Payment gateway adapter polymorphism

### 7. Pure Fabrication
- `UserFactoryManager` as pure fabrication
- Service classes as pure fabrications
- Adapter classes as pure fabrications

### 8. Indirection
- Service layer provides indirection
- Factory pattern provides creation indirection
- Adapter pattern provides external system indirection

### 9. Protected Variations
- Factory pattern protects user creation variations
- Strategy pattern protects recommendation variations
- Adapter pattern protects payment gateway variations

## Class Diagram
```
+----------------+       +----------------+       +----------------+
|     User       |       |   Restaurant   |       |    Order       |
+----------------+       +----------------+       +----------------+
| -id: Long      |       | -id: Long      |       | -id: Long      |
| -username: String|     | -name: String  |       | -status: String|
| -password: String|     | -address: String|      | -totalAmount: Double|
| -email: String |       | -phone: String |       | -orderDate: Date|
| -role: UserRole|       | -isActive: Boolean|    | -customer: User|
+----------------+       +----------------+       | -restaurant: Restaurant|
       |                        |                 | -orderItems: List<OrderItem>|
       |                        |                 +----------------+
       |                        |                         |
       |                        |                         |
       v                        v                         v
+----------------+       +----------------+       +----------------+
|  UserFactory   |       |   MenuItem     |       |  OrderItem     |
+----------------+       +----------------+       +----------------+
| +createUser()  |       | -id: Long      |       | -id: Long      |
+----------------+       | -name: String  |       | -quantity: Integer|
                        | -price: Double |       | -menuItem: MenuItem|
                        | -description: String|   | -order: Order   |
                        +----------------+       +----------------+
                               |
                               |
                               v
                        +----------------+
                        |   Payment      |
                        +----------------+
                        | -id: Long      |
                        | -amount: Double|
                        | -status: String|
                        | -order: Order  |
                        +----------------+

Relationships:
- User 1 --- * Order (One user can have many orders)
- Restaurant 1 --- * MenuItem (One restaurant can have many menu items)
- Order 1 --- * OrderItem (One order can have many order items)
- MenuItem 1 --- * OrderItem (One menu item can be in many order items)
- Order 1 --- 1 Payment (One order has one payment)
```

## Architecture

### Database Schema
- Users (customers, restaurant owners, delivery agents, admins)
- Restaurants
- Menu Items
- Orders
- Order Items
- Payments
- Notifications

### Key Features
1. **User Management**
   - Multi-role authentication (Customer, Restaurant, Delivery Agent, Admin)
   - Profile management

2. **Restaurant Management**
   - Menu management
   - Order processing
   - Operating hours

3. **Order Processing**
   - Cart functionality
   - Payment processing
   - Order tracking

4. **Delivery Management**
   - Order assignment
   - Delivery tracking
   - Status updates

5. **Recommendation System**
   - Basic filtering (popular items, discounts)
   - User-based recommendations

## Technologies Used
- Spring Boot
- Spring Data JPA
- Thymeleaf
- MySQL
- Maven
- Bootstrap (for UI)

## Getting Started

1. Clone the repository
2. Configure database connection in `application.properties`
3. Run `mvn spring-boot:run`
4. Access the application at `http://localhost:8080`

## Project Structure
```
src/main/java/com/fooddelivery/
├── config/           # Configuration classes
├── controller/       # MVC controllers
├── model/           # Entity classes
├── repository/      # JPA repositories
├── service/         # Business logic
└── notifications/   # Event handling
```

## Future Enhancements
1. Real-time order tracking
2. Advanced analytics
3. Mobile application
4. Integration with payment gateways
5. Enhanced recommendation system 