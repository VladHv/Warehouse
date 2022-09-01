package ua.foxminded.herasimov.warehouse.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ua.foxminded.herasimov.warehouse.model.Goods;
import ua.foxminded.herasimov.warehouse.model.Supplier;
import ua.foxminded.herasimov.warehouse.service.impl.SupplierServiceImpl;

@Controller
public class SupplierController {

    private SupplierServiceImpl service;

    @Autowired
    public SupplierController(SupplierServiceImpl service) {
        this.service = service;
    }

    @GetMapping("/suppliers")
    public String showSuppliers(Model model) {
        model.addAttribute("suppliers", service.findAll());
        model.addAttribute("supplier", new Supplier());
        return "suppliers";
    }

    @PostMapping("/suppliers")
    public String createSupplier(@ModelAttribute("supplier") Supplier supplier) {
        service.create(supplier);
        return "redirect:/suppliers";
    }

    @GetMapping("/suppliers/delete/{id}")
    public String deleteSupplier(@PathVariable("id") Integer id) {
        service.delete(id);
        return "redirect:/goods";
    }

    @GetMapping("/suppliers/{id}")
    public String showSupplierById(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("supplier", service.findById(id));
        return "supplier_page";
    }

    @PostMapping("/suppliers/{id}")
    public String updateSupplier(@ModelAttribute("supplier") Supplier supplier) {
        service.update(supplier);
        return "redirect:/suppliers/{id}";
    }

    @GetMapping("/supplier_orders")
    public String showSupplierOrders() {
        return "supplier_orders";
    }
}
