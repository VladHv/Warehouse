package ua.foxminded.herasimov.warehouse.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import ua.foxminded.herasimov.warehouse.exception.ServiceException;
import ua.foxminded.herasimov.warehouse.model.Order;
import ua.foxminded.herasimov.warehouse.model.OrderItem;
import ua.foxminded.herasimov.warehouse.model.OrderStatus;
import ua.foxminded.herasimov.warehouse.service.impl.OrderItemServiceImpl;
import ua.foxminded.herasimov.warehouse.service.impl.OrderServiceImpl;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderServiceImpl orderService;

    @MockBean
    private OrderItemServiceImpl orderItemService;

    @InjectMocks
    private OrderController controller;


    @Test
    void findAllOrders_shouldHasStatusOkAndReturnListFromService_whenListNotNullAndNotEmpty() throws Exception {
        Order order = new Order.Builder().withStatus(OrderStatus.NEW).build();
        List<Order> allOrders = List.of(order);
        given(orderService.findAll()).willReturn(allOrders);
        mockMvc.perform(get("/orders"))
               .andDo(print())
               .andExpect(status().isOk())
               .andExpect(jsonPath("$", hasSize(1)))
               .andExpect(jsonPath("$[0].status", Is.is(order.getStatus().name())));
    }

    @Test
    void findAllOrders_shouldHasStatusOkAndReturnListFromService_whenListFromServiceIsNull() throws Exception {
        List<Order> allOrders = null;
        given(orderService.findAll()).willReturn(allOrders);
        mockMvc.perform(get("/orders"))
               .andDo(print())
               .andExpect(status().isNotFound());
    }

    @Test
    void findAllOrders_shouldHasStatusOkAndReturnListFromService_whenListFromServiceIsEmpty() throws Exception {
        List<Order> allOrders = Collections.emptyList();
        given(orderService.findAll()).willReturn(allOrders);
        mockMvc.perform(get("/orders"))
               .andDo(print())
               .andExpect(status().isNotFound());
    }

    @Test
    void createOrders_shouldHasNoErrorsAndStatusOk_whenOrderNotNull() throws Exception {
        Order order = new Order.Builder().withStatus(OrderStatus.NEW).build();
        String orderJSON = getJson(order);

        given(orderService.create(order)).willReturn(order);

        mockMvc.perform(post("/orders")
                            .content(orderJSON)
                            .contentType(MediaType.APPLICATION_JSON))
               .andDo(print())
               .andExpect(status().isCreated())
               .andExpect(jsonPath("$.status", Is.is(order.getStatus().name())));
    }

    @Test
    void createOrder_shouldHasStatusBadRequest_whenOrderIsNull() throws Exception {
        Order order = null;
        String orderJSON = getJson(order);

        given(orderService.create(order)).willReturn(order);

        mockMvc.perform(post("/orders")
                            .content(orderJSON)
                            .contentType(MediaType.APPLICATION_JSON))
               .andDo(print())
               .andExpect(status().isBadRequest());
    }

    @Test
    void deleteOrder_shouldHasStatusOkAndMessage_whenDeletingOrder() throws Exception {
        Integer orderId = 1;
        mockMvc.perform(delete("/orders/{id}", orderId))
               .andDo(print())
               .andExpect(status().isOk())
               .andExpect(content().string("Order deleted successfully"));
    }

    @Test
    void updateOrder_shouldHasNoErrorsAndStatusOk_whenOrderNotNull() throws Exception {
        Integer orderId = 1;
        Order order = new Order.Builder().withStatus(OrderStatus.NEW).build();
        String orderJSON = getJson(order);

        given(orderService.update(order, orderId)).willReturn(order);

        mockMvc.perform(put("/orders/{id}", orderId)
                            .content(orderJSON)
                            .contentType(MediaType.APPLICATION_JSON))
               .andDo(print())
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.status", Is.is(order.getStatus().name())));
    }

    @Test
    void updateOrder_shouldHasStatusBadRequest_whenOrderIsNull() throws Exception {
        Integer orderId = 1;
        Order order = null;
        String orderJSON = getJson(order);

        given(orderService.update(order, orderId)).willReturn(order);

        mockMvc.perform(put("/orders/{id}", orderId)
                            .content(orderJSON)
                            .contentType(MediaType.APPLICATION_JSON))
               .andDo(print())
               .andExpect(status().isBadRequest());
    }

    @Test
    void findOrderById_shouldHasStatusOkAndReturnEntity_whenServiceReturnOrder() throws Exception {
        Integer orderId = 1;
        Order order = new Order.Builder().withStatus(OrderStatus.NEW).build();
        String orderJSON = getJson(order);

        given(orderService.findById(orderId)).willReturn(order);
        mockMvc.perform(get("/orders/{id}", orderId)
                            .content(orderJSON)
                            .contentType(MediaType.APPLICATION_JSON))
               .andDo(print())
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.status", Is.is(order.getStatus().name())));
    }

    @Test
    void findOrderById_shouldHasStatusBadRequest_whenOrderIdIsIrrelevant() throws Exception {
        Integer orderId = -1;
        Order order = new Order.Builder().withStatus(OrderStatus.NEW).build();
        String orderJSON = getJson(order);

        given(orderService.findById(orderId)).willThrow(new ServiceException("Order not found by ID: " + orderId));
        mockMvc.perform(get("/orders/{id}", orderId)
                            .content(orderJSON)
                            .contentType(MediaType.APPLICATION_JSON))
               .andDo(print())
               .andExpect(status().isNotFound());
    }

    @Test
    void findAllOrderItemsFromOrder_shouldHasStatusOkAndReturnListFromService_whenListNotNullAndNotEmpty() throws
        Exception {
        Integer orderId = 1;
        OrderItem orderItem = new OrderItem.Builder().withAmount(12).build();
        List<OrderItem> allOrderItems = List.of(orderItem);
        given(orderItemService.findAllFromOrder(orderId)).willReturn(allOrderItems);
        mockMvc.perform(get("/orders/{orderId}/orderItems", orderId))
               .andDo(print())
               .andExpect(status().isOk())
               .andExpect(jsonPath("$", hasSize(1)))
               .andExpect(jsonPath("$[0].amount", Is.is(orderItem.getAmount())));
    }

    @Test
    void findAllOrderItemsFromOrder_shouldHasStatusOkAndReturnListFromService_whenListIsNull() throws Exception {
        Integer orderId = 1;
        List<OrderItem> allOrderItems = null;
        given(orderItemService.findAllFromOrder(orderId)).willReturn(allOrderItems);
        mockMvc.perform(get("/orders/{orderId}/orderItems", orderId))
               .andDo(print())
               .andExpect(status().isNotFound());
    }

    @Test
    void findAllOrderItemsFromOrder_shouldHasStatusOkAndReturnListFromService_whenListIsEmpty() throws Exception {
        Integer orderId = 1;
        List<OrderItem> allOrderItems = Collections.emptyList();
        given(orderItemService.findAllFromOrder(orderId)).willReturn(allOrderItems);
        mockMvc.perform(get("/orders/{orderId}/orderItems", orderId))
               .andDo(print())
               .andExpect(status().isNotFound());
    }

    @Test
    void createOrderItemsInOrder_shouldHasNoErrorsAndStatusCreated_whenOrderItemIsValid() throws Exception {
        Integer orderId = 1;
        OrderItem orderItem = new OrderItem.Builder().withAmount(12).build();
        String orderItemJSON = getJson(orderItem);

        given(orderItemService.createOnOrder(orderItem, orderId)).willReturn(orderItem);

        mockMvc.perform(post("/orders/{orderId}/orderItems", orderId)
                            .content(orderItemJSON)
                            .contentType(MediaType.APPLICATION_JSON))
               .andDo(print())
               .andExpect(status().isCreated())
               .andExpect(content().string("OrderItem is valid"));
    }

    @Test
    void createOrderItemsInOrder_shouldHasStatusBadRequestAndValidationMessage_whenAmountIsNull() throws Exception {
        Integer orderId = 1;
        OrderItem orderItem = new OrderItem.Builder().withAmount(null).build();
        String orderItemJSON = getJson(orderItem);

        mockMvc.perform(post("/orders/{orderId}/orderItems", orderId)
                            .content(orderItemJSON)
                            .contentType(MediaType.APPLICATION_JSON))
               .andDo(print())
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.amount", Is.is("Input amount!")));
    }

    @Test
    void createOrderItemsInOrder_shouldHasStatusBadRequestAndValidationMessage_whenAmountLessThanOne() throws
        Exception {
        Integer orderId = 1;
        OrderItem orderItem = new OrderItem.Builder().withAmount(0).build();
        String orderItemJSON = getJson(orderItem);

        mockMvc.perform(post("/orders/{orderId}/orderItems", orderId)
                            .content(orderItemJSON)
                            .contentType(MediaType.APPLICATION_JSON))
               .andDo(print())
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.amount", Is.is("Amount should be at least one")));
    }

    @Test
    void deleteOrderItemsFromOrder_shouldHasStatusOkAndMessage_whenDeletingOrderItemFromOrder() throws Exception {
        Integer orderId = 1;
        Integer orderItemId = 1;
        mockMvc.perform(delete("/orders/{orderId}/orderItems/{orderItemId}", orderId, orderItemId))
               .andDo(print())
               .andExpect(status().isOk())
               .andExpect(content().string("OrderItem deleted successfully"));
    }

    @Test
    void updateOrderItemsInOrder_shouldHasNoErrorsAndStatusCreated_whenOrderItemIsValid() throws Exception {
        Integer orderId = 1;
        Integer orderItemId = 1;
        OrderItem orderItem = new OrderItem.Builder().withAmount(12).build();
        String orderItemJSON = getJson(orderItem);

        given(orderItemService.updateOnOrder(orderItem, orderItemId, orderId)).willReturn(orderItem);

        mockMvc.perform(put("/orders/{orderId}/orderItems/{orderItemId}", orderId, orderItemId)
                            .content(orderItemJSON)
                            .contentType(MediaType.APPLICATION_JSON))
               .andDo(print())
               .andExpect(status().isOk())
               .andExpect(content().string("OrderItem is valid"));
    }

    @Test
    void updateOrderItemsInOrder_shouldHasNoErrorsAndStatusCreated_whenAmountIsNull() throws Exception {
        Integer orderId = 1;
        Integer orderItemId = 1;
        OrderItem orderItem = new OrderItem.Builder().withAmount(null).build();
        String orderItemJSON = getJson(orderItem);

        mockMvc.perform(put("/orders/{orderId}/orderItems/{orderItemId}", orderId, orderItemId)
                            .content(orderItemJSON)
                            .contentType(MediaType.APPLICATION_JSON))
               .andDo(print())
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.amount", Is.is("Input amount!")));
    }

    @Test
    void updateOrderItemsInOrder_shouldHasNoErrorsAndStatusCreated_whenAmountIsLessThanOne() throws Exception {
        Integer orderId = 1;
        Integer orderItemId = 1;
        OrderItem orderItem = new OrderItem.Builder().withAmount(12).build();
        String orderItemJSON = getJson(orderItem);

        given(orderItemService.updateOnOrder(orderItem, orderItemId, orderId)).willReturn(orderItem);

        mockMvc.perform(put("/orders/{orderId}/orderItems/{orderItemId}", orderId, orderItemId)
                            .content(orderItemJSON)
                            .contentType(MediaType.APPLICATION_JSON))
               .andDo(print())
               .andExpect(status().isOk())
               .andExpect(content().string("OrderItem is valid"));
    }

    @Test
    void findOrderItemsByIdInOrder_shouldHasStatusOkAndReturnEntity_whenServiceReturnOrderIem() throws Exception {
        Integer orderId = 1;
        Integer orderItemId = 1;
        OrderItem orderItem = new OrderItem.Builder().withAmount(12).build();

        given(orderItemService.findByIdInOrder(orderItemId, orderId)).willReturn(orderItem);

        mockMvc.perform(get("/orders/{orderId}/orderItems/{orderItemId}", orderId, orderItemId))
               .andDo(print())
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.amount", Is.is(orderItem.getAmount())));
    }

    private String getJson(Object object) throws JsonProcessingException {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        return ow.writeValueAsString(object);
    }
}
