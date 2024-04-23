package apiauto;

import static io.restassured.RestAssured.*;
import static org.hamcrest.core.IsEqual.*;

import java.util.HashMap;

import org.hamcrest.Matchers;
import org.json.JSONObject;
import org.testng.annotations.Test;

import io.restassured.RestAssured;

public class TestReqres {
@Test
    public void testGetListUsers () {
    given().when()
            .get("https://reqres.in/api/users?page=2")
            .then().log().all()
            .assertThat()
            .statusCode(200)
            .body("page", equalTo(2))
            .body("per_page", equalTo(6))
            .body("total", equalTo(12));
}
@Test
public void testGetListUserNotFound(){
    RestAssured.given().when()
            .get("https://reqres.in/api/users/23")
            .then().log().all()
            .assertThat().statusCode(404);
}

@Test
public void testPostCreateUser () {

    String valueName = "prstio";
    String valueJob = "QA";

    JSONObject bodyObj = new JSONObject();

    bodyObj.put("name", valueName);             
    bodyObj.put("job", valueJob);

    RestAssured.given()
            .header("Content-Type", "application/json")
            .header("Accept", "application/json")
            .body(bodyObj.toString())
            .when()
            .post("https://reqres.in/api/users").then().log().all()
            .assertThat().statusCode(201)
            .assertThat().body("name", Matchers.equalTo(valueName));
}
@Test
    public void testPutUser () {

    //Define baseURI
    RestAssured.baseURI = "https://reqres.in/";
    //Data to update
    int userId = 2;
    String newName = "updateUser";
    //Test PUT user id 2 -> update user name
    //First, get the atributes of user id 2
    String fname = given().when().get("api/users/"+userId).getBody().jsonPath().get("data.firstname");
    String lname = given().when().get("api/users/"+userId).getBody().jsonPath().get("data.lasttname");
    String avatar = given().when().get("api/users/"+userId).getBody().jsonPath().get("data.avatar");
    String email = given().when().get("api/users/"+userId).getBody().jsonPath().get("data.email");
    System.out.println("name before = "+fname);

    //Change the first name to "updateUser"
    //Create body request with HashMap and convert it to json
    HashMap<String, Object> bodyMap = new HashMap<>();
    bodyMap.put("id", userId);
    bodyMap.put("email", email);
    bodyMap.put("first_name", newName);
    bodyMap.put("last_name", lname);
    bodyMap.put("avatar", avatar);
    JSONObject  jsonObject = new JSONObject(bodyMap);


    given().log().all()
            .header("Content-Type", "application/json") //set the header to accept json
            .body(jsonObject.toString()) //convert jsonObject to string format
            .put("api/users"+userId)
            .then().log().all() //log().all() here is used to print entire response to console (optional)
            .assertThat().statusCode(200)
            .assertThat().body("first_name", Matchers.equalTo(newName)); //assert the update name
}
@Test
    public void testPatchUser() {
    RestAssured.baseURI = "https://reqres.in/";
    String userId = String.valueOf(3);
    String newName = "updatedUser";
    String fname = given().when().get("api/users/"+userId)
            .getBody().jsonPath().get("data.first_name");
    System.out.println("name before = "+fname);
    //change the first name to "updatedUser"
    //create body request with Hashmap and convert it to json
    HashMap<String, String> bodyMap = new HashMap<>();
    bodyMap.put("first_name", newName);
    JSONObject jsonObject = new JSONObject(bodyMap);

    given().log().all()
            .header("Content-Type", "application/json") //set the header
            .body(jsonObject.toString()) //convert json to string
            .patch("api/users/" +userId)
            .then().log().all()
            .assertThat().statusCode(200)
            .assertThat().body("first_name", Matchers.equalTo(newName));
}
@Test
    public void testDeleteUser () {
    //Define baseURI
    RestAssured.baseURI = "https:reqres.in/";
    //Data to delete
    int userToDelete = 3;
    //Test Delete api/users/3
    given().log().all()
            .when().delete("api/users" + userToDelete)
            .then()
            .log().all() //log().all() is used to print the entire response to console (optional)
            .assertThat().statusCode(204); //assert That the status code 204




}
}
