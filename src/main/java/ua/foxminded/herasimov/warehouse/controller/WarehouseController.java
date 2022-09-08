package ua.foxminded.herasimov.warehouse.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ua.foxminded.herasimov.warehouse.dto.impl.GoodsItemDto;
import ua.foxminded.herasimov.warehouse.dto.impl.GoodsItemDtoMapper;
import ua.foxminded.herasimov.warehouse.service.impl.GoodsItemServiceImpl;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class WarehouseController {

    private GoodsItemServiceImpl service;
    private GoodsItemDtoMapper dtoMapper;

    @Autowired
    public WarehouseController(GoodsItemServiceImpl service,
                               GoodsItemDtoMapper dtoMapper) {
        this.service = service;
        this.dtoMapper = dtoMapper;
    }

    @GetMapping("/")
    public String showHomePage(Model model) {
        List<GoodsItemDto> goodsItemsDto =
            service.findAll().stream().map(g -> dtoMapper.toDto(g)).collect(Collectors.toList());
        model.addAttribute("goodsItems", goodsItemsDto);
        return "index";
    }
}
