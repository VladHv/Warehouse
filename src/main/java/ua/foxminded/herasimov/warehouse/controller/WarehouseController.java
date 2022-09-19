package ua.foxminded.herasimov.warehouse.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ua.foxminded.herasimov.warehouse.dto.impl.*;
import ua.foxminded.herasimov.warehouse.model.*;
import ua.foxminded.herasimov.warehouse.service.impl.GoodsItemServiceImpl;
import ua.foxminded.herasimov.warehouse.service.impl.OrderItemServiceImpl;
import ua.foxminded.herasimov.warehouse.service.impl.OrderServiceImpl;
import ua.foxminded.herasimov.warehouse.service.impl.SupplierServiceImpl;

import javax.validation.Valid;
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
    private OrderDtoMapper orderDtoMapper;

    @Autowired
    public WarehouseController(GoodsItemServiceImpl goodsItemService,
                               OrderServiceImpl orderService,
                               OrderItemServiceImpl orderItemService,
                               GoodsItemDtoMapper goodsItemDtoMapper,
                               OrderItemDtoMapper orderItemDtoMapper,
                               SupplierServiceImpl supplierService,
                               OrderDtoMapper orderDtoMapper) {
        this.goodsItemService = goodsItemService;
        this.orderService = orderService;
        this.orderItemService = orderItemService;
        this.goodsItemDtoMapper = goodsItemDtoMapper;
        this.orderItemDtoMapper = orderItemDtoMapper;
        this.supplierService = supplierService;
        this.orderDtoMapper = orderDtoMapper;
    }

    @GetMapping("/")
    public String showHomePage(Model model) {
        List<GoodsItem> goodsItems = goodsItemService.findAll();
        if (!goodsItems.isEmpty()) {
            List<GoodsItemDto> goodsItemDtos =
                goodsItems.stream().map(g -> goodsItemDtoMapper.toDto(g)).collect(Collectors.toList());
            model.addAttribute("goodsItems", goodsItemDtos);
        }

        List<Supplier> suppliers = supplierService.findAll();
        model.addAttribute("suppliers", suppliers);

        Order order = orderService.getUnregisteredOrder();
        model.addAttribute("order", orderDtoMapper.toDto(order));
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
                                  @RequestParam(value = "amount", required = false) Integer amount,
                                  RedirectAttributes redirectAttributes) {
        if (amount == null || amount <= 0) {
            redirectAttributes.addFlashAttribute("amountError", "Amount should be at least one");
            return "redirect:/";
        }
        OrderItemDto orderItemDto = new OrderItemDto.Builder().withOrderId(orderId)
                                                              .withGoodsId(goodsId)
                                                              .withAmount(amount)
                                                              .build();
        orderItemService.create(orderItemDtoMapper.toEntity(orderItemDto));
        return "redirect:/";
    }

    @GetMapping("/order_item/delete/{id}")
    public String removeGoodsFromOrder(@PathVariable("id") Integer id) {
        orderItemService.delete(id);
        return "redirect:/";
    }

    @PostMapping("/new_order")
    public String createNewOrder(@Valid @ModelAttribute("order") OrderDto orderDto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            List<GoodsItem> goodsItems = goodsItemService.findAll();
            if (!goodsItems.isEmpty()) {
                List<GoodsItemDto> goodsItemDtos =
                    goodsItems.stream().map(g -> goodsItemDtoMapper.toDto(g)).collect(Collectors.toList());
                model.addAttribute("goodsItems", goodsItemDtos);
            }

            List<Supplier> suppliers = supplierService.findAll();
            model.addAttribute("suppliers", suppliers);

            Order order = orderService.getUnregisteredOrder();
            Set<OrderItem> orderItemsFromOrder = order.getOrderItems();
            if (orderItemsFromOrder != null && !orderItemsFromOrder.isEmpty()) {
                model.addAttribute("orderItemsFromOrder", orderItemsFromOrder);
                model.addAttribute("orderPrice",
                                   orderItemsFromOrder.stream().mapToInt(
                                       orderItem -> orderItem.getAmount() * orderItem.getGoods().getPrice()).sum());
            }
            return "index";
        }
        Order order = orderDtoMapper.toEntity(orderDto);
        order.setStatus(OrderStatus.NEW);
        orderService.update(order);
        return "redirect:/";
    }

    @GetMapping("/order/cancel/{id}")
    public String removeAllGoodsFromOrder(@PathVariable("id") Integer id) {
        orderItemService.cancelOrderItemsOfOrder(id);
        return "redirect:/";
    }
}
