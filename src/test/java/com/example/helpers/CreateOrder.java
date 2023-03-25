package com.example.helpers;

import com.example.TestBase;
import com.example.models.store.OrderRequest;
import com.example.models.store.OrderResponse;
import io.qameta.allure.Step;

import static com.example.data.DataGenerator.createValidOrder;
import static io.restassured.RestAssured.given;

public class CreateOrder extends TestBase {


    @Step("Create order")
    public static OrderResponse addOrder() {
        OrderRequest orderRequest = createValidOrder();

        return given()
                .baseUri(URL)
                .body(orderRequest)
                .when()
                .post("store/order")
                .then()
                .log().all()
                .extract().as(OrderResponse.class);
    }

}
