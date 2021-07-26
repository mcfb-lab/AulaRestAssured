package models;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.simple.JSONObject;

import static io.restassured.RestAssured.given;

public class User {
    public String nome;
    public String email;
    public String password;
    public String isAdmin;
    public String authToken;
    public String userID;

    public User(String nome, String email, String password, String isAdmin) {
        this.nome = nome;
        this.email = email;
        this.password = password;
        this.isAdmin = isAdmin;
    }

    public void setUserID(String userID){
        this.userID = userID;
    }

    public void setUserAuthToken(String authToken){
        this.authToken = authToken;
    }

    public String getUserCredentials(){
        JSONObject userJsonRepresentation = new JSONObject();
        userJsonRepresentation.put("email", this.email);
        userJsonRepresentation.put("password", this.password);

        return userJsonRepresentation.toJSONString();
    }

    public Response authenticateUser(){
        Response loginResponse =
                given().
                        header("accept", "application/json").
                        header("Content-Type", "application/json").
                        and().
                        body(getUserCredentials()).
                        when().post("http://localhost:3000/login");

        JsonPath jsonPathEvaluator = loginResponse.jsonPath();
        String authToken = jsonPathEvaluator.get("authorization");
        setUserAuthToken(authToken);

        return loginResponse;
    }

    public Response registerUserRequest(){

        JSONObject userJsonRepresentation = new JSONObject();
        userJsonRepresentation.put("nome", this.nome);
        userJsonRepresentation.put("email", this.email);
        userJsonRepresentation.put("password", this.password);
        userJsonRepresentation.put("administrador", this.isAdmin);

        Response registerUserResult =
                given().
                        header("accept", "application/json").
                        header("Content-Type", "application/json").
                        and().
                        body(userJsonRepresentation.toJSONString()).
                when().
                        post("http://localhost:3000/usuarios");

        JsonPath jsonPathEvaluator = registerUserResult.jsonPath();
        String userID = jsonPathEvaluator.get("_id");
        setUserID(userID);

        return registerUserResult;
    }

    public Response deleteUserRequest(RequestSpecification spec){
        Response userRequestResponse =
                given().
                        spec(spec).
                        header("Content-Type", "application/json").
                when().
                        delete("usuarios/"+this.userID);
        return userRequestResponse;
    }

    public Response postUserRequest(RequestSpecification spec){
        Response userRequestResponse =
                given().
                        spec(spec).
                        header("Content-Type", "application/json").

                when().
                        post("usuarios/"+this.userID);
        return userRequestResponse;
    }
}
