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
    GoodsController controller;


    @Test
    void createGoods_whenBad() throws Exception {
        mockMvc.perform(post("/goods")
                            .param("name", "B")
                            .param("price", "12"))
               .andExpect(model().attributeHasFieldErrors("goods", "name"))
               .andExpect(view().name("goods"))
               .andExpect(status().isOk())
               .andDo(print());
    }

    @Test
    void createGoods_whenGood() throws Exception {
        mockMvc.perform(post("/goods")
                            .param("name", "Bean")
                            .param("price", "12"))
               .andExpect(redirectedUrl("/goods"))
               .andExpect(status().is3xxRedirection())
               .andDo(print());
    }

}
