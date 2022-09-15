package ua.foxminded.herasimov.warehouse.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ua.foxminded.herasimov.warehouse.model.Order;
import ua.foxminded.herasimov.warehouse.service.impl.OrderServiceImpl;

import java.util.List;

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

    @GetMapping("/supplier_orders")
    public String showSupplierOrders(Model model) {
        // TODO: 15.09.2022 required orders for supplier only
        List<Order> orders = orderService.findAllCreatedOrder();
        model.addAttribute("orders", orders);
        return "warehouse_orders";
    }

    @GetMapping("/warehouse_orders")
    public String showActiveOrders(Model model) {
        List<Order> orders = orderService.findAllCreatedOrder();
        model.addAttribute("orders", orders);
        return "warehouse_orders";
    }

    @GetMapping("/take_order/{id}")
    public String takeOrderToWork(@PathVariable("id") Integer orderId){
        orderService.setStatusInProcess(orderId);
        return "redirect:/warehouse_orders";
    }

    @GetMapping("/complete_order/{id}")
    public String completeOrder(@PathVariable("id") Integer orderId){
        orderService.setStatusCompleted(orderId);
        return "redirect:/warehouse_orders";
    }

    @GetMapping("/cancel_order/{id}")
    public String cancelOrder(@PathVariable("id") Integer orderId){
        orderService.setStatusCancel(orderId);
        return "redirect:/warehouse_orders";
    }

    @GetMapping("/close_order/{id}")
    public String closeOrder(@PathVariable("id") Integer orderId){
        orderService.closeCompletedOrder(orderId);
        return "redirect:/warehouse_orders";
    }
}
