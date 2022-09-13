package ua.foxminded.herasimov.warehouse.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ua.foxminded.herasimov.warehouse.service.impl.OrderServiceImpl;

@Controller
public class OrderController {

    private OrderServiceImpl orderService;

    @Autowired
    public OrderController(OrderServiceImpl orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/orders")
    public String showAllOrders() {
        // TODO: 13.09.2022 maybe it is not required...will see
        return "orders";
    }

    @GetMapping("/warehouse_orders")
    public String showActiveOrders(Model model) {
        model.addAttribute("orders", orderService.findAll());
        return "warehouse_orders";
    }
}
