package ua.foxminded.herasimov.warehouse.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.foxminded.herasimov.warehouse.dto.impl.GoodsItemDto;
import ua.foxminded.herasimov.warehouse.dto.impl.GoodsItemDtoMapper;
import ua.foxminded.herasimov.warehouse.dto.impl.OrderItemDto;
import ua.foxminded.herasimov.warehouse.dto.impl.OrderItemDtoMapper;
import ua.foxminded.herasimov.warehouse.model.GoodsItem;
import ua.foxminded.herasimov.warehouse.model.Order;
import ua.foxminded.herasimov.warehouse.model.OrderItem;
import ua.foxminded.herasimov.warehouse.model.Supplier;
import ua.foxminded.herasimov.warehouse.service.impl.GoodsItemServiceImpl;
import ua.foxminded.herasimov.warehouse.service.impl.OrderItemServiceImpl;
import ua.foxminded.herasimov.warehouse.service.impl.OrderServiceImpl;
import ua.foxminded.herasimov.warehouse.service.impl.SupplierServiceImpl;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class WarehouseController {

    private GoodsItemServiceImpl goodsItemService;
    private OrderServiceImpl orderService;
    private OrderItemServiceImpl orderItemService;
    private GoodsItemDtoMapper goodsItemDtoMapper;
    private OrderItemDtoMapper orderItemDtoMapper;
    private SupplierServiceImpl supplierService;

    @Autowired
    public WarehouseController(GoodsItemServiceImpl goodsItemService,
                               OrderServiceImpl orderService,
                               OrderItemServiceImpl orderItemService,
                               GoodsItemDtoMapper goodsItemDtoMapper,
                               OrderItemDtoMapper orderItemDtoMapper,
                               SupplierServiceImpl supplierService) {
        this.goodsItemService = goodsItemService;
        this.orderService = orderService;
        this.orderItemService = orderItemService;
        this.goodsItemDtoMapper = goodsItemDtoMapper;
        this.orderItemDtoMapper = orderItemDtoMapper;
        this.supplierService = supplierService;
    }

    @GetMapping("/")
    public String showHomePage(Model model) {
        List<GoodsItem> goodsItems = goodsItemService.findAll();
        if(!goodsItems.isEmpty()) {
            List<GoodsItemDto> goodsItemDtos =
                goodsItems.stream().map(g -> goodsItemDtoMapper.toDto(g)).collect(Collectors.toList());
            model.addAttribute("goodsItems", goodsItemDtos);
        }

        List<Supplier> suppliers = supplierService.findAll();
        model.addAttribute("suppliers", suppliers);

        Order order = orderService.getUnregisteredOrder();
        model.addAttribute("orderId", order.getId());
        Set<OrderItem> orderItemsFromOrder = order.getOrderItems();
        if (orderItemsFromOrder != null && !orderItemsFromOrder.isEmpty()) {
            model.addAttribute("orderItemsFromOrder", orderItemsFromOrder);
            model.addAttribute("orderPrice",
                               orderItemsFromOrder.stream().mapToInt(
                                   orderItem -> orderItem.getAmount() * orderItem.getGoods().getPrice()).sum());
        }
        return "index";
    }

    @PostMapping("/add_goods")
    public String addGoodsToOrder(@RequestParam("orderId") Integer orderId,
                                  @RequestParam("goodsId") Integer goodsId,
                                  @RequestParam("amount") Integer amount) {
        orderItemService.create(orderItemDtoMapper.toEntity(new OrderItemDto.Builder()
                                                                .withOrderId(orderId)
                                                                .withGoodsId(goodsId)
                                                                .withAmount(amount)
                                                                .build()));
        return "redirect:/";
    }

    @GetMapping("/order_item/delete/{id}")
    public String removeGoodsFromOrder(@PathVariable("id") Integer id) {
        orderItemService.delete(id);
        return "redirect:/";

    }

    @PostMapping("/new_order")
    public String createNewOrder(@RequestParam("orderId") Integer orderId,
                                 @RequestParam("supplierId") Integer supplierId) {
        orderService.setSupplierAndStatusNew(orderId, supplierId);
        return "redirect:/";
    }

    @GetMapping("/order/cancel/{id}")
    public String removeAllGoodsFromOrder(@PathVariable("id") Integer id) {
        orderItemService.cancelOrderItemsOfOrder(id);
        return "redirect:/";
    }
}
