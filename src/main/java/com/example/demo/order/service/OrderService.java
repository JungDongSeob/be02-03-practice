package com.example.demo.order.service;

import com.example.demo.order.model.GetOrdersRes;
import com.example.demo.order.model.OrderProducts;
import com.example.demo.order.model.Orders;
import com.example.demo.order.model.PaymentProducts;
import com.example.demo.order.repository.OrderProductsRepository;
import com.example.demo.order.repository.OrdersRepository;
import com.example.demo.product.model.Product;
import com.example.demo.product.model.dto.ProductReadRes;
import com.example.demo.product.service.ProductService;
import com.example.demo.user.model.User;
import com.google.gson.Gson;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final IamportClient iamportClient;
    private final ProductService productService;
    private final OrdersRepository ordersRepository;
    private final OrderProductsRepository orderProductsRepository;


    public List<GetOrdersRes> list() {
        List<Orders> ordersList = ordersRepository.findAll();
        List<GetOrdersRes> response = new ArrayList<>();

        for (Orders orders : ordersList) {
            List<ProductReadRes> productReadResList = new ArrayList<>();
            for (OrderProducts orderProducts: orders.getOrderProductsList()) {

                ProductReadRes productReadRes = ProductReadRes.builder()
                        .id(orderProducts.getProduct().getId())
                        .name(orderProducts.getProduct().getName())
                        .price(orderProducts.getProduct().getPrice())
                        .build();

                productReadResList.add(productReadRes);
            }

            GetOrdersRes getOrdersRes = GetOrdersRes.builder()
                    .id(orders.getId())
                    .userName(orders.getUser().getName())
                    .products(productReadResList)
                    .build();
            response.add(getOrdersRes);
        }

        return response;
    }

    public void createOrders(String impUid, PaymentProducts paymentProducts) {
        Orders orders = Orders.builder()
                .user(User.builder().id(1L).build())
                .impUid(impUid)
                .build();
        orders = ordersRepository.save(orders);

        for (Product product: paymentProducts.getProducts()) {
            orderProductsRepository.save(
                    OrderProducts.builder()
                            .orders(orders)
                            .product(product)
                    .build()
            );
        }
    }


    public Boolean paymentValidation(String impUid) throws IamportResponseException, IOException {
        IamportResponse<Payment> response = getPaymentInfo(impUid);
        Integer amount = response.getResponse().getAmount().intValue();

        String customDataString = response.getResponse().getCustomData();
        Gson gson = new Gson();
        PaymentProducts paymentProducts = gson.fromJson(customDataString, PaymentProducts.class);

        Integer totalPrice = productService.getTotalPrice(paymentProducts);

        if(amount.equals(totalPrice) ) {
            // 주문
            createOrders(impUid, paymentProducts);
            return true;
        }

        return false;

    }

    public IamportResponse getPaymentInfo(String impUid) throws IamportResponseException, IOException {
        IamportResponse<Payment> response = iamportClient.paymentByImpUid(impUid);

        return response;
    }

}
