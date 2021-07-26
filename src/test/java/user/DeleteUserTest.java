package user;

import base.TestBase;
import io.restassured.response.Response;
import models.User;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.equalTo;

public class DeleteUserTest extends TestBase {
    private static User validUser1;

    @BeforeClass
    public static void generateTestData(){
        validUser1 = new User("Mirela3", "mirela3@gmail.com", "123abc", "true");
        validUser1.registerUserRequest();
    }

    @Test
    public void shouldRemoveUSerReturnSuccessMessageAndStatus200(){
        Response deleteUserResponse = validUser1.deleteUserRequest(SPEC);
        deleteUserResponse.
        then().
                assertThat().
                statusCode(200).
                body("message", equalTo("Registro excluído com sucesso")).log().body();
    }


}
