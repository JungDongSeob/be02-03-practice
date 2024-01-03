package com.example.demo.product.model;

import com.example.demo.order.model.OrderProducts;
import lombok.*;

import javax.persistence.*;
import java.util.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Builder
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String name;
    Integer price;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    private List<OrderProducts> orderProductsList = new ArrayList<>();
}
