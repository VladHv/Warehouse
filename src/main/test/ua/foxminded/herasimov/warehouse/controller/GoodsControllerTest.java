package ua.foxminded.herasimov.warehouse.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import ua.foxminded.herasimov.warehouse.service.impl.GoodsServiceImpl;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = GoodsController.class)
class GoodsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GoodsServiceImpl service;

    @InjectMocks
    private GoodsController controller;


    @Test
    void createGoods_shouldHasNameFieldErrors_whenNameSizeOneOrLess() throws Exception {
        mockMvc.perform(post("/goods")
                            .param("name", "A")
                            .param("price", "12"))
               .andExpect(model().attributeHasFieldErrors("goods", "name"))
               .andExpect(view().name("goods"))
               .andExpect(status().isOk())
               .andDo(print());
    }

    @Test
    void createGoods_shouldHasNameFieldErrors_whenNameMoreThanTwoHundredAndFifty() throws Exception {
        mockMvc.perform(post("/goods")
                            .param("name", "A".repeat(251))
                            .param("price", "12"))
               .andExpect(model().attributeHasFieldErrors("goods", "name"))
               .andExpect(view().name("goods"))
               .andExpect(status().isOk())
               .andDo(print());
    }

    @Test
    void createGoods_shouldHasNameFieldErrors_whenNameIsNull() throws Exception {
        mockMvc.perform(post("/goods")
                            .param("price", "12"))
               .andExpect(model().attributeHasFieldErrors("goods", "name"))
               .andExpect(view().name("goods"))
               .andExpect(status().isOk())
               .andDo(print());
    }

    @Test
    void createGoods_shouldHasPriceFieldErrors_whenPriceValueIsLessThanOne() throws Exception {
        mockMvc.perform(post("/goods")
                            .param("name", "Bob")
                            .param("price", "-1"))
               .andExpect(model().attributeHasFieldErrors("goods", "price"))
               .andExpect(view().name("goods"))
               .andExpect(status().isOk())
               .andDo(print());
    }

    @Test
    void createGoods_shouldHasPriceFieldErrors_whenPriceValueIsNull() throws Exception {
        mockMvc.perform(post("/goods")
                            .param("name", "Bob")
                            .param("price", ""))
               .andExpect(model().attributeHasFieldErrors("goods", "price"))
               .andExpect(view().name("goods"))
               .andExpect(status().isOk())
               .andDo(print());
    }

    @Test
    void createGoods_shouldHasNoErrorsAndRedirectToPage_whenNameAndPriceAreValid() throws Exception {
        mockMvc.perform(post("/goods")
                            .param("name", "Bean")
                            .param("price", "12"))
               .andExpect(model().attributeHasNoErrors())
               .andExpect(redirectedUrl("/goods"))
               .andExpect(status().is3xxRedirection())
               .andDo(print());
    }

    @Test
    void updateGoods_shouldHasNameFieldErrors_whenNameSizeOneOrLess() throws Exception {
        mockMvc.perform(post("/goods/{id}", 1)
                            .param("name", "A")
                            .param("price", "12"))
               .andExpect(model().attributeHasFieldErrors("goods", "name"))
               .andExpect(view().name("goods_page"))
               .andExpect(status().isOk())
               .andDo(print());
    }

    @Test
    void updateGoods_shouldHasNameFieldErrors_whenNameMoreThanTwoHundredAndFifty() throws Exception {
        mockMvc.perform(post("/goods/{id}", 1)
                            .param("name", "A".repeat(251))
                            .param("price", "12"))
               .andExpect(model().attributeHasFieldErrors("goods", "name"))
               .andExpect(view().name("goods_page"))
               .andExpect(status().isOk())
               .andDo(print());
    }

    @Test
    void updateGoods_shouldHasNameFieldErrors_whenNameIsNull() throws Exception {
        mockMvc.perform(post("/goods/{id}", 1)
                            .param("price", "12"))
               .andExpect(model().attributeHasFieldErrors("goods", "name"))
               .andExpect(view().name("goods_page"))
               .andExpect(status().isOk())
               .andDo(print());
    }

    @Test
    void updateGoods_shouldHasPriceFieldErrors_whenPriceValueIsLessThanOne() throws Exception {
        mockMvc.perform(post("/goods/{id}", 1)
                            .param("name", "Bob")
                            .param("price", "-1"))
               .andExpect(model().attributeHasFieldErrors("goods", "price"))
               .andExpect(view().name("goods_page"))
               .andExpect(status().isOk())
               .andDo(print());
    }

    @Test
    void updateGoods_shouldHasPriceFieldErrors_whenPriceValueIsNull() throws Exception {
        mockMvc.perform(post("/goods/{id}", 1)
                            .param("name", "Bob")
                            .param("price", ""))
               .andExpect(model().attributeHasFieldErrors("goods", "price"))
               .andExpect(view().name("goods_page"))
               .andExpect(status().isOk())
               .andDo(print());
    }

    @Test
    void updateGoods_shouldHasNoErrorsAndRedirectToPage_whenNameAndPriceAreValid() throws Exception {
        mockMvc.perform(post("/goods/{id}", 1)
                            .param("name", "Bean")
                            .param("price", "12"))
               .andExpect(model().attributeHasNoErrors())
               .andExpect(redirectedUrl("/goods/1"))
               .andExpect(status().is3xxRedirection())
               .andDo(print());
    }
}
