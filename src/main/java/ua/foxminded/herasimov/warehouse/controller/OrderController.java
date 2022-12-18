package ua.foxminded.herasimov.warehouse.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import ua.foxminded.herasimov.warehouse.exception.ServiceException;
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

    @ApiOperation(value = "Find all orders",
                  notes = "This method returns all orders of warehouse")
    @GetMapping
    public ResponseEntity<List<Order>> findAllOrders() {
        List<Order> orders = orderService.findAll();
        if (orders != null && !orders.isEmpty()) {
            return new ResponseEntity<>(orders, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @ApiOperation(value = "Create new order",
                  notes = "This method requires order object")
    @PostMapping
    public ResponseEntity<Order> createOrder(@ApiParam("Order to add. Cannot be null")
                                             @RequestBody Order order) {
        return new ResponseEntity<>(orderService.create(order), HttpStatus.CREATED);
    }

    @ApiOperation(value = "Delete order by id",
                  notes = "Remove order from warehouse")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOrder(@ApiParam(name = "id",
                                                        type = "Integer",
                                                        value = "ID value for the order",
                                                        example = "1",
                                                        required = true)
                                              @PathVariable("id") Integer id) {
        orderService.delete(id);
        return new ResponseEntity<>("Order deleted successfully", HttpStatus.OK);
    }

    @ApiOperation(value = "Update existed order",
                  notes = "This method updates order per id")
    @PutMapping("/{id}")
    public ResponseEntity<Order> updateOrder(@ApiParam(name = "id",
                                                       type = "Integer",
                                                       value = "ID value for the order",
                                                       example = "1",
                                                       required = true)
                                             @PathVariable("id") Integer id,
                                             @ApiParam("Order to update. Cannot be null")
                                             @RequestBody Order order) {
        return new ResponseEntity<>(orderService.update(order, id), HttpStatus.OK);
    }

    @ApiOperation(value = "Find order by id",
                  notes = "Returns a single order. Provide an id to look up specific order")
    @GetMapping("/{id}")
    public ResponseEntity<Order> findOrderById(@ApiParam(name = "id",
                                                         type = "Integer",
                                                         value = "ID value for the order",
                                                         example = "1",
                                                         required = true)
                                               @PathVariable("id") Integer id) {
        try {
            return new ResponseEntity<>(orderService.findById(id), HttpStatus.OK);
        } catch (ServiceException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @ApiOperation(value = "Find all orders items from order",
                  notes = "This method returns all orders items from order. You should to provide order id")
    @GetMapping("/{orderId}/orderItems")
    public ResponseEntity<List<OrderItem>> findAllOrderItemsFromOrder(@ApiParam(name = "orderId",
                                                                                type = "Integer",
                                                                                value = "ID value for the order to " +
                                                                                        "look up",
                                                                                example = "1",
                                                                                required = true)
                                                                      @PathVariable("orderId") Integer orderId) {
        List<OrderItem> orderItems = orderItemService.findAllFromOrder(orderId);
        if (orderItems != null && !orderItems.isEmpty()) {
            return new ResponseEntity<>(orderItems, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @ApiOperation(value = "Add new valid order item in order",
                  notes = "This method requires order item object to add it to specified order")
    @PostMapping("/{orderId}/orderItems")
    public ResponseEntity<String> createOrderItemsInOrder(@ApiParam(name = "orderId",
                                                                    type = "Integer",
                                                                    value = "ID value for the order to " +
                                                                            "add new order item",
                                                                    example = "1",
                                                                    required = true)
                                                          @PathVariable("orderId") Integer orderId,
                                                          @ApiParam("Order item to add. Cannot be null or empty")
                                                          @Valid @RequestBody OrderItem orderItem) {
        orderItemService.createOnOrder(orderItem, orderId);
        return new ResponseEntity<>("OrderItem is valid", HttpStatus.CREATED);
    }

    @ApiOperation(value = "Delete order item by id from order",
                  notes = "Remove order item from specified order")
    @DeleteMapping("/{orderId}/orderItems/{orderItemId}")
    public ResponseEntity<String> deleteOrderItemsFromOrder(@ApiParam(name = "orderId",
                                                                      type = "Integer",
                                                                      value = "ID value for the order to delete from",
                                                                      example = "1",
                                                                      required = true)
                                                            @PathVariable("orderId") Integer orderId,
                                                            @ApiParam(name = "orderItemId",
                                                                      type = "Integer",
                                                                      value = "ID value for the order item to delete",
                                                                      example = "1",
                                                                      required = true)
                                                            @PathVariable("orderItemId") Integer orderItemId) {
        orderItemService.deleteFromOrder(orderItemId, orderId);
        return new ResponseEntity<>("OrderItem deleted successfully", HttpStatus.OK);
    }

    @ApiOperation(value = "Update existed order item in order",
                  notes = "This method updates valid order item per id in specified order")
    @PutMapping("/{orderId}/orderItems/{orderItemId}")
    public ResponseEntity<String> updateOrderItemsInOrder(@ApiParam(name = "orderId",
                                                                    type = "Integer",
                                                                    value = "ID value for the order to update in",
                                                                    example = "1",
                                                                    required = true)
                                                          @PathVariable("orderId") Integer orderId,
                                                          @ApiParam(name = "orderItemId",
                                                                    type = "Integer",
                                                                    value = "ID value for the order item to update",
                                                                    example = "1",
                                                                    required = true)
                                                          @PathVariable("orderItemId") Integer orderItemId,
                                                          @ApiParam("Order item to update. Cannot be null or empty")
                                                          @Valid @RequestBody OrderItem orderItem) {
        orderItemService.updateOnOrder(orderItem, orderItemId, orderId);
        return new ResponseEntity<>("OrderItem is valid", HttpStatus.OK);
    }

    @ApiOperation(value = "Find order item by id in order",
                  notes = "Returns a single order item from specified order. Provide an order item id and order id")
    @GetMapping("/{orderId}/orderItems/{orderItemId}")
    public ResponseEntity<OrderItem> findOrderItemsByIdInOrder(@ApiParam(name = "orderId",
                                                                         type = "Integer",
                                                                         value = "ID value for the order to look up",
                                                                         example = "1",
                                                                         required = true)
                                                               @PathVariable("orderId") Integer orderId,
                                                               @ApiParam(name = "orderItemId",
                                                                         type = "Integer",
                                                                         value = "ID value for the order item to look up",
                                                                         example = "1",
                                                                         required = true)
                                                               @PathVariable("orderItemId") Integer orderItemId) {
        return new ResponseEntity<>(orderItemService.findByIdOnOrder(orderItemId, orderId), HttpStatus.OK);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
        MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}
