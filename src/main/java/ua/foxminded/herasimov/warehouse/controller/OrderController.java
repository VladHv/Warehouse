package ua.foxminded.herasimov.warehouse.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import ua.foxminded.herasimov.warehouse.model.Order;
import ua.foxminded.herasimov.warehouse.model.OrderItem;
import ua.foxminded.herasimov.warehouse.service.impl.OrderItemServiceImpl;
import ua.foxminded.herasimov.warehouse.service.impl.OrderServiceImpl;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderServiceImpl orderService;
    private final OrderItemServiceImpl orderItemService;

    @Autowired
    public OrderController(OrderServiceImpl orderService,
                           OrderItemServiceImpl orderItemService) {
        this.orderService = orderService;
        this.orderItemService = orderItemService;
    }

    @GetMapping
    public ResponseEntity<List<Order>> findAllOrders() {
        List<Order> orders = orderService.findAll();
        if (orders != null && !orders.isEmpty()) {
            return new ResponseEntity<>(orders, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody Order order) {
        return new ResponseEntity<>(orderService.create(order), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOrder(@PathVariable("id") Integer id) {
        orderService.delete(id);
        return new ResponseEntity<>("Order deleted successfully", HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Order> updateOrder(@PathVariable("id") Integer id, @RequestBody Order order) {
        return new ResponseEntity<>(orderService.update(order, id), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> findOrderById(@PathVariable("id") Integer id) {
        return new ResponseEntity<>(orderService.findById(id), HttpStatus.OK);
    }

    @GetMapping("/{orderId}/orderItems")
    public ResponseEntity<List<OrderItem>> findAllOrderItemsFromOrder(@PathVariable("orderId") Integer orderId) {
        List<OrderItem> orderItems = orderItemService.findAllFromOrder(orderId);
        if (orderItems != null && !orderItems.isEmpty()) {
            return new ResponseEntity<>(orderItems, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/{orderId}/orderItems")
    public ResponseEntity<String> createOrderItemsInOrder(@PathVariable("orderId") Integer orderId,
                                                          @Valid @RequestBody OrderItem orderItem) {
        orderItemService.createOnOrder(orderItem, orderId);
        return new ResponseEntity<>("OrderItem is valid", HttpStatus.CREATED);
    }

    @DeleteMapping("/{orderId}/orderItems/{orderItemId}")
    public ResponseEntity<String> deleteOrderItemsFromOrder(@PathVariable("orderId") Integer orderId,
                                                            @PathVariable("orderItemId") Integer orderItemId) {
        orderItemService.deleteFromOrder(orderItemId, orderId);
        return new ResponseEntity<>("OrderItem deleted successfully", HttpStatus.OK);
    }

    @PutMapping("/{orderId}/orderItems/{orderItemId}")
    public ResponseEntity<String> updateOrderItemsInOrder(@PathVariable("orderId") Integer orderId,
                                                          @PathVariable("orderItemId") Integer orderItemId,
                                                          @Valid @RequestBody OrderItem orderItem) {
        orderItemService.updateOnOrder(orderItem, orderItemId, orderId);
        return new ResponseEntity<>("OrderItem is valid", HttpStatus.CREATED);
    }

    @GetMapping("/{orderId}/orderItems/{orderItemId}")
    public ResponseEntity<OrderItem> findOrderItemsByIdInOrder(@PathVariable("orderId") Integer orderId,
                                                               @PathVariable("orderItemId") Integer orderItemId) {
        return new ResponseEntity<>(orderItemService.findByIdOnOrder(orderItemId, orderId), HttpStatus.OK);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
        MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}
