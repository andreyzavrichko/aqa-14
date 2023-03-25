package com.example.data;

import com.example.models.store.OrderRequest;
import com.github.javafaker.Faker;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class DataGenerator {
    static Faker faker = new Faker();

    public static OrderRequest createValidOrder() {
        String dateConstant = "3000";
        String dateS = new SimpleDateFormat("-MM-dd'T'HH:mm:ss.SSSZ").format(Calendar.getInstance().getTime()).substring(0, 19);
        String shipDate = dateConstant + dateS;
        return OrderRequest.builder()
                .id(faker.random().nextInt(1, 555))
                .petId(faker.random().nextInt(1, 566))
                .quantity(faker.random().nextInt(1, 888))
                .shipDate(shipDate)
                .status("placed")
                .complete(true)
                .build();
    }

}