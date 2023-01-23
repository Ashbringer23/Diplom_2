package user;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;
import pojo.UserData;

import static org.hamcrest.CoreMatchers.equalTo;
import static request.UserRequest.*;

public class UpdateUserDataTest {

    String email = "archilol@yandex.ru";
    String password = "123456";
    String name = "archilol";
    String newEmail = "newArchilol@yandex.ru";
    String newPassword = "new123456";
    String newName = "newArchilol";
    String token;

    @Test
    @DisplayName("Проверка измененния данных авторизованного пользователя")
    public void updateUserWithAuthorizationTest() {
        UserData userData = new UserData(email, password, name);
        registrationUser(userData).then().statusCode(200);
        token = authorizationUser(new UserData(email, password)).then().extract().path("accessToken");

        Response response = updateUserData(new UserData(newEmail, newPassword, newName), token);
        response.then()
                .assertThat()
                .body("success", equalTo(true))
                .statusCode(200);

        authorizationUser(new UserData(newEmail, newPassword)).then()
                .assertThat()
                .body("success", equalTo(true))
                .statusCode(200);
    }
    @Test
    @DisplayName("Проверка измененния данных неавторизиронного пользователя")
    public void updateUserWithoutAuthorizationTest() {

        Response response = updateUserData(new UserData(newEmail, newPassword, newName));
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