package ua.foxminded.herasimov.warehouse.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ua.foxminded.herasimov.warehouse.dto.impl.GoodsItemDto;
import ua.foxminded.herasimov.warehouse.dto.impl.GoodsItemDtoMapper;
import ua.foxminded.herasimov.warehouse.service.impl.GoodsItemServiceImpl;
import ua.foxminded.herasimov.warehouse.service.impl.GoodsServiceImpl;

@Controller
public class GoodsItemController {

    private GoodsItemServiceImpl goodsItemService;
    private GoodsServiceImpl goodsService;
    private GoodsItemDtoMapper dtoMapper;

    @Autowired
    public GoodsItemController(GoodsItemServiceImpl goodsItemService,
                               GoodsServiceImpl goodsService,
                               GoodsItemDtoMapper dtoMapper) {
        this.goodsItemService = goodsItemService;
        this.goodsService = goodsService;
        this.dtoMapper = dtoMapper;
    }

    @GetMapping("/warehouse_goods")
    public String showGoodsItems(Model model) {
        model.addAttribute("goodsItems", goodsItemService.findAll());
        model.addAttribute("goodsList", goodsService.findAll());
        model.addAttribute("goodsItem", new GoodsItemDto());
        return "warehouse_goods";
    }

    @PostMapping("/warehouse_goods")
    public String createGoodsItem(@ModelAttribute("goodsItem") GoodsItemDto goodsItemDto) {
        goodsItemService.create(dtoMapper.toEntity(goodsItemDto));
        return "redirect:/warehouse_goods";
    }

    @GetMapping("/warehouse_goods/delete/{id}")
    public String deleteGoodsItem(@PathVariable("id") Integer id) {
        goodsItemService.delete(id);
        return "redirect:/warehouse_goods";
    }

    @GetMapping("/warehouse_goods/{id}")
    public String showGoodsItemById(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("goodsItem", dtoMapper.toDto(goodsItemService.findById(id)));
        model.addAttribute("goodsList", goodsService.findAll());
        return "warehouse_goods_page";
    }

    @PostMapping("/warehouse_goods/{id}")
    public String updateGoodsItem(@ModelAttribute("goodsItem") GoodsItemDto goodsItemDto) {
        goodsItemService.update(dtoMapper.toEntity(goodsItemDto));
        return "redirect:/warehouse_goods/{id}";
    }


}
