# Food Delivery Application

A Spring Boot-based food delivery application that allows customers to order food from restaurants, restaurants to manage their menus and orders, and delivery agents to handle deliveries.

## Design Patterns Used

### 1. MVC (Model-View-Controller) Pattern
- **Controllers**: Handle HTTP requests and responses (e.g., `CustomerController`, `RestaurantPortalController`)
- **Models**: Represent data entities (e.g., `User`, `Restaurant`, `Order`, `MenuItem`)
- **Views**: Thymeleaf templates for rendering HTML (e.g., `dashboard.html`, `orders.html`)

### 2. Repository Pattern
- Used for data access abstraction
- Implemented through Spring Data JPA repositories
- Examples: `UserRepository`, `RestaurantRepository`, `OrderRepository`

### 3. Service Layer Pattern
- Business logic encapsulation
- Transaction management
- Examples: `UserService`, `OrderService`, `PaymentService`

### 4. Factory Pattern
- Used in `UserFactoryManager` for creating different types of users
- Encapsulates user creation logic based on role

### 5. Observer Pattern
- Implemented for notifications
- `NotificationService` observes order events and notifies relevant users

### 6. Strategy Pattern
- Used in recommendation system
- Different recommendation strategies based on user preferences and item popularity

### 7. Session Management Pattern
- User authentication and authorization
- Storing user context in HTTP session

### 8. Command Pattern
- Used for order processing
- Encapsulates order creation and payment processing

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