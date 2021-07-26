package models;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.simple.JSONObject;

import static io.restassured.RestAssured.given;

public class Product {
    public String nome;
    public int preco;
    public String descricao;
    public int quantidade;

    //public String authToken;
    public String productID;

    public Product(String nome, int preco, String descricao, int quantidade) {
        this.nome = nome;
        this.preco = preco;
        this.descricao = descricao;
        this.quantidade = quantidade;
    }

    public void setProductID(String productID){
        this.productID = productID;
    }

    public Response postProductRequest(RequestSpecification spec, String userCredentials){

        JSONObject productJsonRepresentation = new JSONObject();
        productJsonRepresentation.put("nome", this.nome);
        productJsonRepresentation.put("preco", this.preco);
        productJsonRepresentation.put("descricao", this.descricao);
        productJsonRepresentation.put("quantidade", this.quantidade);

        Response registerProductResult =
                given().
                        spec(spec).
                        header("Content-Type", "application/json").
                and().
                        body(productJsonRepresentation.toJSONString()).
                        body(userCredentials).
                when().
                        post("produtos");

        JsonPath jsonPathEvaluator = registerProductResult.jsonPath();
        String productID = jsonPathEvaluator.get("_id");
        setProductID(productID);

        return registerProductResult;
    }
}
