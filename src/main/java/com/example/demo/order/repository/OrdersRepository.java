package com.example.demo.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.order.model.*;

public interface OrdersRepository extends JpaRepository<Orders, Long> {
}
