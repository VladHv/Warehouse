package ua.foxminded.herasimov.warehouse.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class OrderController {

    @GetMapping("/orders")
    public String showAllOrders(){
        return "orders";
    }

    @GetMapping("/warehouse_orders")
    public String showActiveOrders(){
        return "warehouse_orders";
    }
}
