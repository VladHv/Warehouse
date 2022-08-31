package ua.foxminded.herasimov.warehouse.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SupplierController {

    @GetMapping("/suppliers")
    public String showSuppliers(){
        return "suppliers";
    }

    @GetMapping("/supplier_orders")
    public String showSupplierOrders(){
        return "supplier_orders";
    }
}
