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
| BXGY         | {"buy_products":[{"product_id":1,"quantity":3},         	   | Buy 3 Product 1, 3 Product 2        | Eligible repetitions 2 -> discount = 2 ×      |
|			   |	{"product_id":2,"quantity":3}],							   |								     |		price of Product 3 						 |
|              |  "get_products":[{"product_id":3,"quantity":1}],              |                                     |                           					 |
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


Request and Responses::
1.Coupon Creation
Endpoint url::http://localhost:8080/coupons
Method: POST
Request(cart wise)::
{
    "type": "CART_WISE",
    "details": {
        "threshold": 100,
        "discount": 10
    }
}        

Response::
Coupon created successfully::1

Request(product wise)::
{
    "type": "PRODUCT_WISE",
    "details": {
        "product_id": 1,
        "discount": 20
    }
}        

Response::
Coupon created successfully::2

Request(Bxgy)::
{
    "type": "BXGY",
    "details": {
        "buy_products": [
            {
                "product_id": 1,
                "quantity": 3
            },
            {
                "product_id": 2,
                "quantity": 3
            }
        ],
        "get_products": [
            {
                "product_id": 3,
                "quantity": 1
            }
        ],
        "repition_limit": 2
       
    }
}

Response::
Coupon created successfully::3

2.Applicable coupons
Endpoint url::http://localhost:8080/coupons/applicable-coupons
Method: POST
Request ::
{
    "cart": {
        "items": [
            {
                "product_id": 1,
                "quantity": 6,
                "price": 50
            },
            {
                "product_id": 2,
                "quantity": 3,
                "price": 30
            },
            {
                "product_id": 3,
                "quantity": 2,
                "price": 25
            }
        ]
    }
}

Response::
{
    "applicable_coupons": [
        {
            "coupon_id": 1,
            "discount": 44.0,
            "type": "CART_WISE"
        },
        {
            "coupon_id": 2,
            "discount": 27.0,
            "type": "PRODUCT_WISE"
        },
        {
            "coupon_id": 3,
            "discount": 50.0,
            "type": "BXGY"
        }
    ]
}

3.Apply coupon
Endpoint url::http://localhost:8080/coupons/apply-coupon/{coupon_id}
Method: POST
Request ::
{
    "cart": {
        "items": [
            {
                "product_id": 1,
                "quantity": 6,
                "price": 50
            },
            {
                "product_id": 2,
                "quantity": 3,
                "price": 30
            },
            {
                "product_id": 3,
                "quantity": 2,
                "price": 25
            }
        ]
    }
}

Response::
{
    "updated_cart": {
        "final_total_discount": 50.0,
        "total_price": 490.0,
        "items": [
            {
                "product_id": 1,
                "quantity": 6,
                "total_discount": 0.0,
                "price": 50.0
            },
            {
                "product_id": 2,
                "quantity": 3,
                "total_discount": 0.0,
                "price": 30.0
            },
            {
                "product_id": 3,
                "quantity": 4,
                "total_discount": 50.0,
                "price": 25.0
            }
        ],
        "final_price": 440.0
    }
}

4.Get all coupons
Endpoint url::http://localhost:8080/coupons
Method: GET

Response::
[
    {
        "id": 1,
        "type": "CART_WISE",
        "details": {
            "discount": 10,
            "threshold": 100
        }
    },
    {
        "id": 3,
        "type": "BXGY",
        "details": {
            "buy_products": [
                {
                    "quantity": 3,
                    "product_id": 1
                },
                {
                    "quantity": 3,
                    "product_id": 2
                }
            ],
            "get_products": [
                {
                    "quantity": 1,
                    "product_id": 3
                }
            ],
            "repition_limit": 2
        }
    },
    {
        "id": 2,
        "type": "PRODUCT_WISE",
        "details": {
            "discount": 30,
            "product_id": 2
        }
    }
]

5.Get coupon by id
Endpoint url::http://localhost:8080/coupons/1
Method: GET

Response:
{
    "id": 1,
    "type": "CART_WISE",
    "details": {
        "discount": 10,
        "threshold": 100
    }
}

6.Update coupon by id 
Endpoint url::http://localhost:8080/coupons/2
Method: PUT

Request::
{
    "type": "PRODUCT_WISE",
    "details": {
        "product_id": 2,
        "discount": 20
    }
}    

Response::
{
    "id": 2,
    "type": "PRODUCT_WISE",
    "details": {
        "product_id": 2,
        "discount": 20
    }
}

7.Delete Coupon by id
Endpoint url::http://localhost:8080/coupons/3
Method: DELETE

Response::
Coupon deleted successfully:3