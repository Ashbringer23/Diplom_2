package order;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;
import pojo.OrderData;
import pojo.UserData;
import request.OrderRequest;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static request.UserRequest.*;

public class GetOrderTest {

    String email = "archilol@yandex.ru";
    String password = "123456";
    String name = "archilol";
    String token;
    int orderNumber;

    @Test
    @DisplayName("Проверка получения заказа авторизованного пользователя")
    public void checkAuthorizedUserOrderTest() {
        UserData userData = new UserData(email, password, name);
        registrationUser(userData).then().statusCode(200);
        Response authorization = authorizationUser(new UserData(email, password));
        token = authorization.then().extract().path("accessToken");
        authorization.then().statusCode(200);
        orderNumber = OrderRequest.createOrder(new OrderData(List.of("7e3dfb7325e270118e6f0d9f2fc51ce8a30af95dd7cae4b9d620a0c533f227ab840491f785088969")), token).then().extract().path("order.number");

        Response response = OrderRequest.getOrderFromUser(token);
        response.then()
                .assertThat()
                .body("success", equalTo(true))
                .body("orders[0].ingredients[0]", equalTo("7e3dfb7325e270118e6f0d9f2fc51ce8a30af95dd7cae4b9d620a0c533f227ab840491f785088969"))
                .body("orders[0].number", equalTo(orderNumber))
                .statusCode(200);
    }

    @Test
    @DisplayName("Проверка получения заказа неавторизованного пользователя")
    public void checkUnauthorizedUserOrderTest() {
        UserData userData = new UserData(email, password, name);
        registrationUser(userData).then().statusCode(200);
        Response authorization = authorizationUser(new UserData(email, password));
        token = authorization.then().extract().path("accessToken");
        authorization.then().statusCode(200);
        orderNumber = OrderRequest.createOrder(new OrderData(List.of("7e3dfb7325e270118e6f0d9f2fc51ce8a30af95dd7cae4b9d620a0c533f227ab840491f785088969")), token).then().extract().path("order.number");

        Response response = OrderRequest.getOrderFromUser();
        response.then()
                .assertThat()
                .body("success", equalTo(false))
                .body("message", equalTo("You should be authorised"))
                .statusCode(401);
    }

    @After
    public void deleteUserData() {
        if (token != null) {
            deleteUser(token);
        }
    }
}
