package request;

import io.restassured.response.Response;
import pojo.UserData;

import static io.restassured.RestAssured.given;
import static utils.Constants.BASE_URL;

public class UserRequest {

    public static Response registrationUser(UserData userData) {
        return given()
                .header("Content-type", "application/json")
                .body(userData)
                .post(BASE_URL + "/api/auth/register");
    }

    public static Response authorizationUser(UserData userData) {
        return given()
                .header("Content-type", "application/json")
                .body(userData)
                .when()
                .post(BASE_URL + "/api/auth/login");
    }

    public static Response updateUserData(UserData userUpdateData, String token) {
        return given()
                .header("Content-type", "application/json")
                .header("Authorization", token)
                .body(userUpdateData)
                .when()
                .patch(BASE_URL + "/api/auth/user");
    }

    public static Response updateUserData(UserData userUpdateData) {
        return given()
                .header("Content-type", "application/json")
                .body(userUpdateData)
                .when()
                .patch(BASE_URL + "/api/auth/user");
    }

    public static void deleteUser(String token) {
        given()
                .header("Authorization", token)
                .when()
                .delete(BASE_URL + "/api/auth/user")
                .then()
                .statusCode(202);
    }

}
