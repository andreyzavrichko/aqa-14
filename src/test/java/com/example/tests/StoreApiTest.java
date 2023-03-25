package com.example.tests;

import com.example.TestBase;
import com.example.models.store.*;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.example.data.DataGenerator.createValidOrder;
import static com.example.helpers.CreateOrder.addOrder;
import static com.example.spec.Specifications.*;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DisplayName("Store")
public class StoreApiTest extends TestBase {
    @BeforeAll
    public static void setUp() {
        installRequestSpecification(requestSpec(URL));
    }

    @Test
    @Feature("Store")
    @Story("Order")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Place an order for a pet")
    void createValidOrderTest() {
        installResponseSpecification(responseSpecOK200());

        OrderRequest orderRequest = createValidOrder();

        OrderResponse orderResponse = given()
                .body(orderRequest)
                .when()
                .post("store/order")
                .then()
                .extract().as(OrderResponse.class);

        assertThat(orderResponse.getId()).isEqualTo(orderRequest.getId());
        assertThat(orderResponse.getStatus()).isEqualTo(orderRequest.getStatus());
        assertThat(orderResponse.getShipDate()).isEqualTo(orderRequest.getShipDate() + "+0000");
        assertThat(orderResponse.getQuantity()).isEqualTo(orderRequest.getQuantity());
        assertThat(orderResponse.getPetId()).isEqualTo(orderRequest.getPetId());
        assertThat(orderResponse.getComplete()).isEqualTo(orderRequest.getComplete());

    }


    @Test
    @Feature("Store")
    @Story("Order")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Find purchase order by ID")
    void findOrderByIdTest() {
        installResponseSpecification(responseSpecOK200());

        OrderResponse orderRequest = addOrder();

        OrderResponse orderResponse = given()
                .pathParam("orderId", orderRequest.getId())
                .when()
                .get("store/order/{orderId}")
                .then()
                .extract().as(OrderResponse.class);

        assertThat(orderResponse.getId()).isEqualTo(orderRequest.getId());
        assertThat(orderResponse.getStatus()).isEqualTo(orderRequest.getStatus());
        assertThat(orderResponse.getShipDate()).isEqualTo(orderRequest.getShipDate());
        assertThat(orderResponse.getQuantity()).isEqualTo(orderRequest.getQuantity());
        assertThat(orderResponse.getPetId()).isEqualTo(orderRequest.getPetId());
        assertThat(orderResponse.getComplete()).isEqualTo(orderRequest.getComplete());
    }

    @Test
    @Feature("Store")
    @Story("Order")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Find purchase order by ID negative")
    void findOrderByIdNegativeTest() {
        installResponseSpecification(responseSpecNotFound404());

        OrderError orderError = given()
                .pathParam("orderId", "10")
                .when()
                .get("store/order/{orderId}")
                .then()
                .extract().as(OrderError.class);

        assertThat(orderError.getCode()).isEqualTo(1);
        assertThat(orderError.getType()).isEqualTo("error");
        assertThat(orderError.getMessage()).isEqualTo("Order not found");

    }

    @Test
    @Feature("Store")
    @Story("Order")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Delete purchase order by ID")
    void deleteOrderByIdTest() {
        installResponseSpecification(responseSpecOK200());

        OrderResponse orderRequest = addOrder();

        DeleteOrderResponse deleteOrderResponse = given()
                .pathParam("orderId", orderRequest.getId())
                .when()
                .delete("store/order/{orderId}")
                .then()
                .extract().as(DeleteOrderResponse.class);

        assertThat(deleteOrderResponse.getCode()).isEqualTo(200);
        assertThat(deleteOrderResponse.getType()).isEqualTo("unknown");
        assertThat(deleteOrderResponse.getMessage()).isEqualTo(orderRequest.getId().toString());
    }

    @Test
    @Feature("Store")
    @Story("Order")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Delete purchase order by ID negative")
    void deleteOrderByIdNegativeTest() {
        installResponseSpecification(responseSpecNotFound404());

        DeleteOrderResponse deleteOrderResponse = given()
                .pathParam("orderId", 999)
                .when()
                .delete("store/order/{orderId}")
                .then()
                .extract().as(DeleteOrderResponse.class);

        assertThat(deleteOrderResponse.getCode()).isEqualTo(404);
        assertThat(deleteOrderResponse.getType()).isEqualTo("unknown");
        assertThat(deleteOrderResponse.getMessage()).isEqualTo("Order Not Found");
    }

    @Test
    @Feature("Store")
    @Story("Inventory")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Returns pet inventories by status")
    void getInventoryTest() {
        installResponseSpecification(responseSpecOK200());

        InventoryResponse inventoryResponse = given()
                .when()
                .get("store/inventory")
                .then()
                .extract().as(InventoryResponse.class);

        assertThat(inventoryResponse.getSold()).isNotZero();
        assertThat(inventoryResponse.getAvailable()).isNotZero();
        assertThat(inventoryResponse.getString()).isNotZero();
    }


}
