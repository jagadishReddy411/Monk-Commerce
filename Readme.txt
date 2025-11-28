Tech Stack::
Java
Spring Boot
PostgreSQL
Spring Data JPA
Lombok

Configure PostgreSQL:::
CREATE DATABASE Coupon-Management;

spring.datasource.url=jdbc:postgresql://localhost:5432/Coupon-Management
spring.datasource.username=postgres
spring.datasource.password=root

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true


Assumptions
Each coupon has a type field (CART_WISE, PRODUCT_WISE, BXGY).
Coupon details JSON contains necessary fields for type:
Cart-wise -> threshold, discount
Product-wise -> product_id, discount
BXGY -> buy_products, get_products, repition_limit
Quantities are positive integers; prices are positive doubles.
Multiple coupons are independent; discount per coupon is calculated separately.
Cart items have unique product IDs.

+--------------+--------------------------------------------------------------+--------------------------------------+-----------------------------------------------+
| Coupon Type  | Details JSON                                                 | Scenario                             | Expected Discount                             |
+--------------+--------------------------------------------------------------+--------------------------------------+-----------------------------------------------+
| CART_WISE    | {"threshold":100,"discount":10}                               | Cart total = $120                   | $12                                           |
| CART_WISE    | {"threshold":200,"discount":15}                               | Cart total = $150                   | $0 (threshold not met)                        |
| PRODUCT_WISE | {"product_id":1,"discount":20}                                | 2 items of Product 1 at $50 each    | $20                                           |
| PRODUCT_WISE | {"product_id":2,"discount":10}                                | Product 2 not in cart               | $0                                            |
| BXGY         | {"buy_products":[{"product_id":1,"quantity":2}],              | Buy 4 Product 1, 2 Product 2        | Eligible repetitions 2 → discount = 2 ×      |
|              |  "get_products":[{"product_id":2,"quantity":1}],              |                                     | price of Product 2                            |
|              |  "repition_limit":2}                                          |                                     |                                               |
+--------------+--------------------------------------------------------------+--------------------------------------+-----------------------------------------------+



Use Cases Covered

Category	              Cases
Cart-wise	              Threshold-based, percentage discount
Product-wise	          Specific product discount
BxGy	                  Multiple buy items, multiple free items, repetition limit

Best coupon	Applies user-chosen coupon after checking discount values


Unimplemented::
Case												Reason
Auto-choose best coupon without user selection	    Out of scope for assignment flow
Coupon exclusion rules between coupon types	        Could be added later


Future Enhancements::
Redis caching for applied coupon history
Admin analytics dashboards
Role-based authentication for coupon management
Multicurrency price support



Project Structure::
controller          REST endpoints
service and Impl    Business logic
Calculator          Coupon engine (Strategy Pattern)
Entity              DB Entities
repository          JPA interfaces
dto                 Response models
Exception			Exception handling