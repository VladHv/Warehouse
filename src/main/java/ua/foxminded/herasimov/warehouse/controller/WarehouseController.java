package ua.foxminded.herasimov.warehouse.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ua.foxminded.herasimov.warehouse.dto.impl.GoodsItemDto;
import ua.foxminded.herasimov.warehouse.dto.impl.GoodsItemDtoMapper;
import ua.foxminded.herasimov.warehouse.model.Order;
import ua.foxminded.herasimov.warehouse.service.impl.GoodsItemServiceImpl;
import ua.foxminded.herasimov.warehouse.service.impl.OrderServiceImpl;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class WarehouseController {

    private GoodsItemServiceImpl goodsItemService;
    private OrderServiceImpl orderService;
    private GoodsItemDtoMapper dtoMapper;

    @Autowired
    public WarehouseController(GoodsItemServiceImpl goodsItemService,
                               OrderServiceImpl orderService,
                               GoodsItemDtoMapper dtoMapper) {
        this.goodsItemService = goodsItemService;
        this.orderService = orderService;
        this.dtoMapper = dtoMapper;
    }

    @GetMapping("/")
    public String showHomePage(Model model) {
        List<GoodsItemDto> goodsItemsDto =
            goodsItemService.findAll().stream().map(g -> dtoMapper.toDto(g)).collect(Collectors.toList());
        model.addAttribute("goodsItems", goodsItemsDto);
        Order order = orderService.getUnregisteredOrder(); // TODO: 09.09.2022 transfer DTO (create) with total price
        model.addAttribute("order", order);
        return "index";
    }

    //TODO: 09.09.2022 addGoodsToOrder method required
    //TODO: 09.09.2022 createOrder method required
    //TODO: 09.09.2022 cancelOrder method required
}
