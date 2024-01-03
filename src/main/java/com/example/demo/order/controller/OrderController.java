package com.example.demo.order.controller;

import com.example.demo.order.service.OrderService;
import com.example.demo.product.service.ProductService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.siot.IamportRestClient.response.IamportResponse;
import okhttp3.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin("*")
public class OrderController {

    private final ProductService productService;
    private final OrderService orderService;

    public OrderController(ProductService productService, OrderService orderService) {
        this.productService = productService;
        this.orderService = orderService;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/order/list")
    public ResponseEntity list() {
        return ResponseEntity.ok().body(orderService.list());
    }

    @RequestMapping(method = RequestMethod.GET, value = "/validation")
    public ResponseEntity validation(String impUid) {
        try {
            if (orderService.paymentValidation(impUid)) {
                // 주문

                return ResponseEntity.ok().body("ok");
            } else {
                return ResponseEntity.ok().body("error");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok().body("error");
        }
    }
}
