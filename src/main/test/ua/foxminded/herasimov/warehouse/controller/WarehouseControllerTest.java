package ua.foxminded.herasimov.warehouse.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import ua.foxminded.herasimov.warehouse.dto.impl.GoodsItemDtoMapper;
import ua.foxminded.herasimov.warehouse.dto.impl.OrderDto;
import ua.foxminded.herasimov.warehouse.dto.impl.OrderDtoMapper;
import ua.foxminded.herasimov.warehouse.dto.impl.OrderItemDtoMapper;
import ua.foxminded.herasimov.warehouse.model.Order;
import ua.foxminded.herasimov.warehouse.service.impl.GoodsItemServiceImpl;
import ua.foxminded.herasimov.warehouse.service.impl.OrderItemServiceImpl;
import ua.foxminded.herasimov.warehouse.service.impl.OrderServiceImpl;
import ua.foxminded.herasimov.warehouse.service.impl.SupplierServiceImpl;

import java.util.HashSet;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = WarehouseController.class)
@MockBeans({@MockBean(GoodsItemServiceImpl.class), @MockBean(OrderItemServiceImpl.class),
    @MockBean(GoodsItemDtoMapper.class), @MockBean(OrderItemDtoMapper.class),
    @MockBean(SupplierServiceImpl.class)})
class WarehouseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderServiceImpl orderService;

    @MockBean
    private OrderDtoMapper orderDtoMapper;

    @InjectMocks
    private WarehouseController controller;

    @Test
    void createNewOrder_shouldHasFieldError_whenSupplierIdIsNull() throws Exception {
        Mockito.when(orderService.getUnregisteredOrder())
               .thenReturn(new Order.Builder().withOrderItems(new HashSet<>()).build());
        mockMvc.perform(post("/new_order")
                            .param("supplierId", ""))
               .andExpect(model().attributeHasFieldErrors("order", "supplierId"))
               .andExpect(view().name("index"))
               .andExpect(status().isOk())
               .andDo(print());
    }

    @Test
    void createNewOrder_shouldHasNoEror_whenSupplierIdIsNotNull() throws Exception {
        Mockito.when(orderDtoMapper.toEntity(any(OrderDto.class))).thenReturn(new Order());
        mockMvc.perform(post("/new_order")
                            .param("supplierId", "1"))
               .andExpect(model().attributeHasNoErrors())
               .andExpect(redirectedUrl("/"))
               .andExpect(status().is3xxRedirection())
               .andDo(print());
    }

}
