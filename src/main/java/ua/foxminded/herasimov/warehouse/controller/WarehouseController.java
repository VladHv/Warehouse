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
import ua.foxminded.herasimov.warehouse.dto.impl.OrderItemDto;
import ua.foxminded.herasimov.warehouse.dto.impl.OrderItemDtoMapper;
import ua.foxminded.herasimov.warehouse.model.Order;
import ua.foxminded.herasimov.warehouse.service.impl.GoodsItemServiceImpl;
import ua.foxminded.herasimov.warehouse.service.impl.OrderItemServiceImpl;
import ua.foxminded.herasimov.warehouse.service.impl.OrderServiceImpl;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class WarehouseController {

    private GoodsItemServiceImpl goodsItemService;
    private OrderServiceImpl orderService;
    private OrderItemServiceImpl orderItemService;
    private GoodsItemDtoMapper goodsItemDtoMapper;
    private OrderItemDtoMapper orderItemDtoMapper;

    @Autowired
    public WarehouseController(GoodsItemServiceImpl goodsItemService,
                               OrderServiceImpl orderService,
                               OrderItemServiceImpl orderItemService,
                               GoodsItemDtoMapper goodsItemDtoMapper,
                               OrderItemDtoMapper orderItemDtoMapper) {
        this.goodsItemService = goodsItemService;
        this.orderService = orderService;
        this.orderItemService = orderItemService;
        this.goodsItemDtoMapper = goodsItemDtoMapper;
        this.orderItemDtoMapper = orderItemDtoMapper;
    }

    @GetMapping("/")
    public String showHomePage(Model model) {
        List<GoodsItemDto> goodsItemsDto =
            goodsItemService.findAll().stream().map(g -> goodsItemDtoMapper.toDto(g)).collect(Collectors.toList());
        model.addAttribute("goodsItems", goodsItemsDto);
        model.addAttribute("orderItem", new OrderItemDto());

        Order order = orderService.getUnregisteredOrder();

        model.addAttribute("orderId", order.getId());
        model.addAttribute("orderItemsFromOrder", order.getItems());
        model.addAttribute("orderPrice",
                           order.getItems().stream()
                                .mapToInt(orderItem -> orderItem.getAmount() * orderItem.getGoods().getPrice())
                                .sum());
        return "index";
    }

    @PostMapping("/")
    public String addGoodsToOrder(@ModelAttribute("orderItem") OrderItemDto orderItemDto) {
        orderItemService.create(orderItemDtoMapper.toEntity(orderItemDto));
        return "redirect:/";
    }

    @GetMapping("/order/create/{id}")
    public String createOrder(@PathVariable("id") Integer id){
        orderService.setStatusNewToOrder(id);
        return "redirect:/";
    }

    @GetMapping("/order/cancel/{id}")
    public String removeAllGoodsFromOrder(@PathVariable("id") Integer id){
        orderItemService.cancelOrderItemsOfOrder(id);
        return "redirect:/";
    }



    //TODO: 09.09.2022 createOrder method required
    //TODO: 09.09.2022 cancelOrder method required
}
