package ua.foxminded.herasimov.warehouse.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GoodsItemController {

    @GetMapping("/warehouse_goods")
    public String showOrders(){
        return "warehouse_goods";
    }
}
