package product;

import base.TestBase;
import io.restassured.response.Response;
import models.Product;
import models.User;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class PostProductTest extends TestBase {

    private static User validUser;
    private static Product validProduct;

    private static String authToken;

    @BeforeClass
    public void generateTestData(){
        validUser = new User("teste2", "teste2@gmail.com", "123abc", "true");
        validUser.registerUserRequest();

        validProduct = new Product("Logitech", 350, "Teclado", 10);
    }

    @AfterClass
    public void removeTestData(){
        validUser.deleteUserRequest(SPEC);
    }

    //Token ausente, inválido ou expirado
    @Test()
    public void shouldPostProductErrorMesageAuthTokenAndStatus401() {
        Response postProductResponse = validProduct.postProductRequest(SPEC, validUser.getUserCredentials());
        postProductResponse.
        then().
                assertThat().
                statusCode(401).
                body("message", equalTo("Token de acesso ausente, inválido, expirado ou usuário do token não existe mais")).log().body();
    }

    //Cadastro com sucesso
    @Test()
    public void shouldPostProductReturnSucessMesageStatus201() {
        Response postProductResponse = validProduct.postProductRequest(SPEC, validUser.getUserCredentials());
        postProductResponse.
                then().
                assertThat().
                statusCode(201).
                body("message", equalTo("Cadastro realizado com sucesso")).
                body("_id", notNullValue()).log().body();
    }
}
