package ua.foxminded.herasimov.warehouse.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ua.foxminded.herasimov.warehouse.model.Goods;
import ua.foxminded.herasimov.warehouse.service.impl.GoodsServiceImpl;

import javax.validation.Valid;

@Controller
public class GoodsController {

    private GoodsServiceImpl service;

    @Autowired
    public GoodsController(GoodsServiceImpl service) {
        this.service = service;
    }

    @GetMapping("/goods")
    public String showOrders(Model model) {
        model.addAttribute("goodsList", service.findAll());
        model.addAttribute("goods", new Goods());
        return "goods";
    }

    @PostMapping("/goods")
    public String createGoods(@Valid @ModelAttribute("goods") Goods goods, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("goodsList", service.findAll());
            return "goods";
        }
        service.create(goods);
        return "redirect:/goods";
    }

    @GetMapping("/goods/delete/{id}")
    public String deleteGoods(@PathVariable("id") Integer id) {
        service.delete(id);
        return "redirect:/goods";
    }

    @GetMapping("/goods/{id}")
    public String showGoodsById(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("goods", service.findById(id));
        return "goods_page";
    }

    @PostMapping("/goods_update")
    public String updateGoods(@Valid @ModelAttribute("goods") Goods goods, BindingResult result) {
        if (result.hasErrors()) {
            return "goods_page";
        }
        service.update(goods);
        return "redirect:/goods/{id}";
    }
}
