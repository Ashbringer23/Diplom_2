package request;

import io.restassured.response.Response;
import pojo.OrderData;

import static io.restassured.RestAssured.given;
import static utils.Constants.BASE_URL;

public class OrderRequest {

    public static Response getOrderFromUser(String token) {
        return given()
                .header("Authorization", token)
                .get(BASE_URL + "/api/orders");
    }

    public static Response getOrderFromUser() {
        return given()
                .get(BASE_URL + "/api/orders");
    }

    public static Response createOrder(OrderData orderData, String token) {
        return given()
                .header("Content-type", "application/json")
                .header("Authorization", token)
                .body(orderData)
                .post(BASE_URL + "/api/orders");
    }

    public static Response createOrder(OrderData orderData) {
        return given()
                .header("Content-type", "application/json")
                .body(orderData)
                .post(BASE_URL + "/api/orders");
    }
}
