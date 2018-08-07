package com.mobimeo.verspaetung.resource;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class LineResourceTest {

    @BeforeClass
    public static void load() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = Integer.getInteger("port", 3000);
    }

    @Test
    public void search_badRequestNoQuery() {
        when()
            .get("/lines")
            .then()
            .statusCode(400)
            .body(is("query parameter is required"));
    }

    @Test
    public void search_badRequestNoXParam() {
        given()
            .queryParam("query", "10:02:00")
            .when()
            .get("/lines")
            .then()
            .statusCode(400)
            .body(is("x parameter is required"));
    }

    @Test
    public void search_badRequestNoYParam() {
        given()
            .queryParam("query", "10:02:00")
            .queryParam("x", "1")
            .when()
            .get("/lines")
            .then()
            .statusCode(400)
            .body(is("y parameter is required"));
    }

    @Test
    public void search_success() {
        JsonPath jsonPath = given()
            .queryParam("query", "10:02:00")
            .queryParam("x", "1")
            .queryParam("y", "4")
            .when()
            .get("/lines")
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .extract()
            .jsonPath();

        assertThat(jsonPath.getList("$"), hasSize(1));
        assertThat(jsonPath.get("[0].id"), is(0));
        assertThat(jsonPath.get("[0].name"), is("M4"));
        assertThat(jsonPath.get("[0].stops"), hasSize(5));
        assertThat(jsonPath.get("[0].delays"), hasSize(1));
    }

    @Test
    public void byName_notFound() {
        when()
            .get("/lines/notFound")
            .then()
            .statusCode(404)
            .body(is("Line not found for name: notFound"));
    }

    @Test
    public void byName_success() {
        JsonPath jsonPath = when()
            .get("/lines/M4")
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .extract()
            .jsonPath();

        assertThat(jsonPath.get("id"), is(0));
        assertThat(jsonPath.get("name"), is("M4"));
        assertThat(jsonPath.get("stops"), hasSize(5));
        assertThat(jsonPath.get("delays"), hasSize(1));
    }

    @AfterClass
    public static void unload() {
        RestAssured.reset();
    }
}
