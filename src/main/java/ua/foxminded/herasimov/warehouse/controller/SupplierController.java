package ua.foxminded.herasimov.warehouse.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ua.foxminded.herasimov.warehouse.model.Supplier;
import ua.foxminded.herasimov.warehouse.service.impl.SupplierServiceImpl;

import javax.validation.Valid;

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
    public String createSupplier(@Valid @ModelAttribute("supplier") Supplier supplier, BindingResult result,
                                 Model model) {
        if(result.hasErrors()){
            model.addAttribute("suppliers", service.findAll());
            return "suppliers";
        }
        service.create(supplier);
        return "redirect:/suppliers";
    }

    @GetMapping("/suppliers/delete/{id}")
    public String deleteSupplier(@PathVariable("id") Integer id) {
        service.delete(id);
        return "redirect:/suppliers";
    }

    @GetMapping("/suppliers/{id}")
    public String showSupplierById(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("supplier", service.findById(id));
        return "supplier_page";
    }

    @PostMapping("/suppliers/{id}")
    public String updateSupplier(@Valid @ModelAttribute("supplier") Supplier supplier, BindingResult result) {
        if(result.hasErrors()){
            return "supplier_page";
        }
        service.update(supplier);
        return "redirect:/suppliers/{id}";
    }

}
