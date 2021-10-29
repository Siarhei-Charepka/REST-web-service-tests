import jdk.jfr.Description;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.IsEqual.equalTo;

public class ApiTest {

    private static final String URL = "http://jsonplaceholder.typicode.com/albums";

    @Description("The test checks that status code of the obtained response is 200 OK")
    @Test
    public void verifyHttpStatusCodeTest() {
        given()
                .baseUri(URL)
                .when().get()
                .then()
                .assertThat()
                .statusCode(200);
    }

    @Description("The test checks that the content-type header exists in the obtained response" +
            "and the value of the content-type header is application/json; charset=utf-8")
    @Test
    public void verifyHttpResponseHeaderTest() {
        given()
                .baseUri(URL)
                .when().get()
                .then()
                .assertThat()
                .statusCode(200)
                .header("Content-Type", "application/json; charset=utf-8");
    }

    @Description("The test checks that the content of the response body is the array of 100 albums")
    @Test
    public void verifyHttpResponseBodyTest() {
        given()
                .baseUri(URL)
                .when().get()
                .then()
                .assertThat()
                .body("albums", hasSize(100));
    }

    @Description("The test checks that the content of the response body is the one album " +
            "and it contains fields: userId, id, title.")
    @Test
    public void getInformationAboutAlbumsByIdTest() {
        given()
                .baseUri(URL)
                .when().get("/{id}", 1)
                .then()
                .assertThat()
                .body(containsString("userId"),
                        containsString("id"),
                        containsString("title"))
                .body("id", equalTo(1));
    }

    @Description("The test checks that a new post has been created")
    @Test
    public void createNewPostTest() {
        Map<String, Object> someMap = new HashMap<>();
        someMap.put("userId", 1);
        someMap.put("title", "test album");

        given()
                .contentType("application/json")
                .body(someMap)
                .when().post(URL)
                .then()
                .assertThat()
                .statusCode(201);
    }

}
