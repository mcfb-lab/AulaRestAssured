package user;

import base.TestBase;
import io.restassured.response.Response;
import models.User;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class PostLoginTest extends TestBase {
    private static User validUser1;
    private static User invalidUser1;
    private static User invalidUser2;

    @BeforeClass
    public static void generateTestData(){
        validUser1 = new User("Mi", "mi@gmail.com", "123abc", "true");
        validUser1.registerUserRequest();
        invalidUser1 = new User("Mi", "mi@gmail.com", "lalala", "false");
        invalidUser2 = new User("Mi", "mi@gmail.com", "", "false");
    }

    //Login com sucesso
    @Test()
    public void shouldReturnSucessMesageAuthTokenAndStatus200() {
        given().
                spec(SPEC).
                header("Content-Type", "application/json").
        and().
                body(validUser1.getUserCredentials()).
        when().
                post("login").
        then().
                assertThat().
                statusCode(200).
                body("message", equalTo("Login realizado com sucesso")).
                body("authorization", notNullValue()).log().body();
    }

    //Login com sucesso pegando o autentication
    @Test()
    public void shouldReturnSucessMesageAuthTokenAndStatus200_2() {
        Response loginResponse = validUser1.authenticateUser();
        loginResponse.
                then().
                assertThat().
                statusCode(200).
                body("message", equalTo("Login realizado com sucesso")).log().body();
    }

    //Login com Erro 401
    @Test()
    public void shouldReturnErrorMesageAuthTokenAndStatus401() {
        given().
                spec(SPEC).
                header("Content-Type", "application/json").
                and().
                body(invalidUser1.getUserCredentials()).
                when().
                post("login").
                then().
                assertThat().
                statusCode(401).
                body("message", equalTo("Email e/ou senha inválidos")).log().body();
    }

    //Login com Erro 400
    @Test()
    public void shouldReturnErrorMesageAuthTokenAndStatus400() {
        given().
                spec(SPEC).
                header("Content-Type", "application/json").
                and().
                body(invalidUser2.getUserCredentials()).
                when().
                post("login").
                then().
                assertThat().
                statusCode(400).
                body("message", equalTo("password não pode ficar em branco")).log().body();
    }
}
