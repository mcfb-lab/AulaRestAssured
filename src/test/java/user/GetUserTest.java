package user;

import base.TestBase;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import models.User;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;

public class GetUserTest extends TestBase {

    private static User validUser1;
    private static User validUser2;
    private static User validUser3;
    private static User invalidUser1;

    @BeforeClass
    public static void generateTestData(){
        validUser1 = new User("Ana", "ana@gmail.com", "123abc", "true");
        validUser1.registerUserRequest();
        validUser2 = new User("Chico", "chico@gmail.com", "123abc", "true");
        validUser2.registerUserRequest();
        validUser3 = new User("Maria", "mariasilva@gmail.com", "lalala", "false");
        validUser3.registerUserRequest();

        invalidUser1 = new User("Carlos", "carlos@gmail.com", "minha", "0");
        invalidUser1.registerUserRequest();
    }

    @AfterClass
    public void removeTestData(){
        validUser1.deleteUserRequest(SPEC);
        validUser2.deleteUserRequest(SPEC);
        validUser3.deleteUserRequest(SPEC);
    }

    //Chamadas básicas, para ser utilizados em todos os testes (reuso)
    public RequestSpecification spec = new RequestSpecBuilder()
            .addHeader("accept", "application/json")
            .setBaseUri("http://localhost:3000/").build();

    //chamada de dados. cada linha é um teste
    @DataProvider(name = "usersData")
    public Object[][] createTestData(){
        return new Object[][]{
                {"?nome=Fulano da Silva",1},
                {"?password=teste",1},
                {"?nome=Fulano da Silva&password=teste",1}
        };
    }

    //chamada de dados. cada linha é um teste
    @DataProvider(name = "usersData2")
    public Object[][] createTestData2(){
        return new Object[][]{
                {"?nome="+validUser1.nome,1},
                {"?password="+validUser2.password,2},
                {"?email="+validUser3.email,1},
                {"?nome="+invalidUser1.nome+"?email="+invalidUser1.email,0}
        };
    }

    @Test
    //formato nome do método e que vai retornar
    public void shouldReturnAllUsersAndStatus200() {
        given().
                header("accept", "application/json").
                when().
                get("http://localhost:3000/usuarios").
                then().
                log().body();
    }

    @Test
    //formato nome do método e que vai retornar
    public void shouldReturnUserAndStatus200() {
        given().
                header("accept", "application/json").

        when().
                get("http://localhost:3000/usuarios?nome=Fulano da Silva").
        then().
                assertThat().
                statusCode(200).
                body("quantidade", equalTo(1));
    }

    //Método principal utilizado pela professora
    //Utilizando uma classe base  e usersData (dados)
    @Test(dataProvider = "usersData")
    public void shouldReturnUsersAndStatus200BasicEData(String query, int totalUsers) {
        given().
                spec(spec).
        when().
                get("usuarios" + query).
        then().
                assertThat().
                statusCode(200).
                body("quantidade", equalTo(totalUsers));
    }

    //Método principal utilizado pela professora
    //Utilizando uma classe base  e usersData2 (dados)
    @Test(dataProvider = "usersData2")
    public void shouldReturnUsersAndStatus200BasicEData2(String query, int totalUsers) {
        given().
                spec(SPEC).
        when().
                get("usuarios" + query).
        then().
                assertThat().
                statusCode(200).
                body("quantidade", equalTo(totalUsers));
    }
}
