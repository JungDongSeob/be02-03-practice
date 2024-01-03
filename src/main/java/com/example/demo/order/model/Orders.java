package com.example.demo.order.model;


import com.example.demo.user.model.User;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Builder
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="User_id")
    private User user;

    private String impUid;

    @OneToMany(mappedBy = "orders", fetch = FetchType.LAZY)
    private List<OrderProducts> orderProductsList = new ArrayList<>();

}
