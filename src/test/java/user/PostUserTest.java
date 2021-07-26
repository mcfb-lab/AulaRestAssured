package user;

import base.TestBase;
import io.restassured.response.Response;
import models.User;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class PostUserTest extends TestBase {

    private static User validUser;
    private static User invalidUser;

    @BeforeClass
    public static void generateTestData(){
        validUser = new User("Mirela", "mirela@gmail.com", "123abc", "true");
        invalidUser = new User("Mirela2", "mirela2@gmail.com", "123abc2", "true");
        invalidUser.registerUserRequest();
    }

    @AfterClass
    public void removeTestData(){
        validUser.deleteUserRequest(SPEC);
        invalidUser.deleteUserRequest(SPEC);
    }

    //Cadastro com sucesso
    @Test()
    public void shouldPostUserReturnSucessMesageStatus201() {
        Response postUserResponse = validUser.registerUserRequest();
        postUserResponse.
        then().
                assertThat().
                statusCode(201).
                body("message", equalTo("Cadastro realizado com sucesso")).
                body("_id", notNullValue()).log().body();
    }

    //E-mail já cadastrado
    @Test()
    public void shouldPostEmailReturnErrorMesageStatus400() {
        Response postUserResponse = invalidUser.registerUserRequest();
        postUserResponse.
        then().
                assertThat().
                statusCode(400).
                body("message", equalTo("Este email já está sendo usado")).log().body();
    }

}
