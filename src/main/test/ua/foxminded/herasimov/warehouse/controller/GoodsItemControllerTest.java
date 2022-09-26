package ua.foxminded.herasimov.warehouse.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import ua.foxminded.herasimov.warehouse.dto.impl.GoodsItemDtoMapper;
import ua.foxminded.herasimov.warehouse.service.impl.GoodsItemServiceImpl;
import ua.foxminded.herasimov.warehouse.service.impl.GoodsServiceImpl;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = GoodsItemController.class)
@MockBeans(
    {@MockBean(GoodsItemServiceImpl.class), @MockBean(GoodsServiceImpl.class), @MockBean(GoodsItemDtoMapper.class)})
class GoodsItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    private GoodsItemController controller;

    @Test
    void createGoodsItem_shouldHasGoodIdFieldError_whenGoodsIdIsNull() throws Exception {
        mockMvc.perform(post("/warehouse_goods")
                            .param("goodsId", "")
                            .param("amount", "10"))
               .andExpect(model().attributeHasFieldErrors("goodsItem", "goodsId"))
               .andExpect(view().name("warehouse_goods"))
               .andExpect(status().isOk())
               .andDo(print());
    }

    @Test
    void createGoodsItem_shouldHasAmountFieldError_whenAmountIsNull() throws Exception {
        mockMvc.perform(post("/warehouse_goods")
                            .param("goodsId", "1")
                            .param("amount", ""))
               .andExpect(model().attributeHasFieldErrors("goodsItem", "amount"))
               .andExpect(view().name("warehouse_goods"))
               .andExpect(status().isOk())
               .andDo(print());
    }

    @Test
    void createGoodsItem_shouldHasAmountFieldError_whenAmountIsLessThanOne() throws Exception {
        mockMvc.perform(post("/warehouse_goods")
                            .param("goodsId", "1")
                            .param("amount", "-1"))
               .andExpect(model().attributeHasFieldErrors("goodsItem", "amount"))
               .andExpect(view().name("warehouse_goods"))
               .andExpect(status().isOk())
               .andDo(print());
    }

    @Test
    void createGoodsItem_shouldHasNoErrors_whenGoodsIdAndAmountAreValid() throws Exception {
        mockMvc.perform(post("/warehouse_goods")
                            .param("goodsId", "1")
                            .param("amount", "1"))
               .andExpect(model().attributeHasNoErrors())
               .andExpect(redirectedUrl("/warehouse_goods"))
               .andExpect(status().is3xxRedirection())
               .andDo(print());
    }
    
    @Test
    void updateGoodsItem_shouldHasGoodIdFieldError_whenGoodsIdIsNull() throws Exception {
        mockMvc.perform(post("/warehouse_goods/{id}", 1)
                            .param("goodsId", "")
                            .param("amount", "10"))
               .andExpect(model().attributeHasFieldErrors("goodsItem", "goodsId"))
               .andExpect(view().name("warehouse_goods_page"))
               .andExpect(status().isOk())
               .andDo(print());
    }

    @Test
    void updateGoodsItem_shouldHasAmountFieldError_whenAmountIsNull() throws Exception {
        mockMvc.perform(post("/warehouse_goods/{id}", 1)
                            .param("goodsId", "1")
                            .param("amount", ""))
               .andExpect(model().attributeHasFieldErrors("goodsItem", "amount"))
               .andExpect(view().name("warehouse_goods_page"))
               .andExpect(status().isOk())
               .andDo(print());
    }

    @Test
    void updateGoodsItem_shouldHasAmountFieldError_whenAmountIsLessThanOne() throws Exception {
        mockMvc.perform(post("/warehouse_goods/{id}", 1)
                            .param("goodsId", "1")
                            .param("amount", "-1"))
               .andExpect(model().attributeHasFieldErrors("goodsItem", "amount"))
               .andExpect(view().name("warehouse_goods_page"))
               .andExpect(status().isOk())
               .andDo(print());
    }

    @Test
    void updateGoodsItem_shouldHasNoErrors_whenGoodsIdAndAmountAreValid() throws Exception {
        mockMvc.perform(post("/warehouse_goods/{id}", 1)
                            .param("goodsId", "1")
                            .param("amount", "1"))
               .andExpect(model().attributeHasNoErrors())
               .andExpect(redirectedUrl("/warehouse_goods/1"))
               .andExpect(status().is3xxRedirection())
               .andDo(print());
    }
}
