package ua.foxminded.herasimov.warehouse.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ua.foxminded.herasimov.warehouse.service.impl.GoodsItemServiceImpl;

@Controller
public class WarehouseController {

    private GoodsItemServiceImpl service;

    @Autowired
    public WarehouseController(GoodsItemServiceImpl service) {
        this.service = service;
    }

    @GetMapping("/")
    public String showHomePage(Model model) {
        model.addAttribute("goodsItems", service.findAll());
        return "index";
    }
}
