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
import ua.foxminded.herasimov.warehouse.model.Goods;
import ua.foxminded.herasimov.warehouse.model.GoodsItem;
import ua.foxminded.herasimov.warehouse.service.impl.GoodsItemServiceImpl;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = GoodsItemController.class)
class GoodsItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GoodsItemServiceImpl service;

    @InjectMocks
    private GoodsItemController controller;

    @Test
    void getAllGoodsItems_shouldHasStatusOkAndReturnListFromService_whenListNotNullAndNotEmpty() throws Exception {
        GoodsItem goodsItem = new GoodsItem.Builder().withAmount(3).build();
        List<GoodsItem> allGoodsItems = List.of(goodsItem);
        given(service.findAll()).willReturn(allGoodsItems);
        mockMvc.perform(get("/goodsItems"))
               .andDo(print())
               .andExpect(status().isOk())
               .andExpect(jsonPath("$", hasSize(1)))
               .andExpect(jsonPath("$[0].amount", Is.is(goodsItem.getAmount())));
    }

    @Test
    void getAllGoodsItems_shouldHasStatusNotFound_whenListFromServiceIsNull() throws Exception {
        List<GoodsItem> allGoodsItems = null;
        given(service.findAll()).willReturn(allGoodsItems);
        mockMvc.perform(get("/goodsItems"))
               .andDo(print())
               .andExpect(status().isNotFound());
    }

    @Test
    void getAllGoodsItems_shouldHasStatusNotFound_whenListFromServiceIsEmpty() throws Exception {
        List<GoodsItem> allGoodsItems = Collections.emptyList();
        given(service.findAll()).willReturn(allGoodsItems);
        mockMvc.perform(get("/goodsItems"))
               .andDo(print())
               .andExpect(status().isNotFound());
    }

    @Test
    void findGoodsItemById_shouldHasStatusOkAndReturnEntity_whenServiceReturnSupplier() throws Exception {
        GoodsItem goodsItem = new GoodsItem.Builder().withId(1).withAmount(3).build();
        given(service.findById(goodsItem.getId())).willReturn(goodsItem);

        mockMvc.perform(get("/goodsItems/{id}", 1))
               .andDo(print())
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.amount", Is.is(goodsItem.getAmount())));
    }

    @Test
    void findGoodsItemById_shouldHasStatusBadRequest_whenServiceThrowsServiceEx() throws Exception {
        Integer goodsItemId = 1;
        given(service.findById(goodsItemId)).willThrow(
            new ServiceException("GoodsItem not found by ID: " + goodsItemId));
        mockMvc.perform(get("/goodsItems/{id}", goodsItemId))
               .andDo(print())
               .andExpect(status().isNotFound());
    }

    @Test
    void createGoodsItem_shouldHasStatusBadRequestAndValidationMessage_whenGoodsIsNull() throws Exception {
        String goodsItem = getJson(new GoodsItem.Builder().withGoods(null).withAmount(12).build());
        mockMvc.perform(post("/goodsItems")
                            .content(goodsItem)
                            .contentType(MediaType.APPLICATION_JSON))
               .andDo(print())
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.goods", Is.is("Choose goods!")));
    }

    @Test
    void createGoodsItem_shouldHasStatusBadRequestAndValidationMessage_whenAmountIsNull() throws Exception {
        String goodsItem = getJson(new GoodsItem.Builder().withGoods(new Goods()).withAmount(null).build());
        mockMvc.perform(post("/goodsItems")
                            .content(goodsItem)
                            .contentType(MediaType.APPLICATION_JSON))
               .andDo(print())
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.amount", Is.is("Input amount!")));
    }

    @Test
    void createGoodsItem_shouldHasStatusBadRequestAndValidationMessage_whenAmountIsLessThanOne() throws Exception {
        String goodsItem = getJson(new GoodsItem.Builder().withGoods(new Goods()).withAmount(0).build());
        mockMvc.perform(post("/goodsItems")
                            .content(goodsItem)
                            .contentType(MediaType.APPLICATION_JSON))
               .andDo(print())
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.amount", Is.is("Amount should be at least one")));
    }

    @Test
    void createGoodsItem_shouldHasNoErrorsAndStatusCreated_whenGoodsIdAndAmountAreValid() throws Exception {
        String goodsItem = getJson(new GoodsItem.Builder().withGoods(new Goods()).withAmount(12).build());
        mockMvc.perform(post("/goodsItems")
                            .content(goodsItem)
                            .contentType(MediaType.APPLICATION_JSON))
               .andDo(print())
               .andExpect(status().isCreated())
               .andExpect(content().string("GoodsItem is valid"));
    }

    @Test
    void updateGoodsItem_shouldHasStatusBadRequestAndValidationMessage_whenGoodsIsNull() throws Exception {
        String goodsItem = getJson(new GoodsItem.Builder().withGoods(null).withAmount(12).build());
        mockMvc.perform(put("/goodsItems/{id}", 1)
                            .content(goodsItem)
                            .contentType(MediaType.APPLICATION_JSON))
               .andDo(print())
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.goods", Is.is("Choose goods!")));
    }

    @Test
    void updateGoodsItem_shouldHasStatusBadRequestAndValidationMessage_whenAmountIsNull() throws Exception {
        String goodsItem = getJson(new GoodsItem.Builder().withGoods(new Goods()).withAmount(null).build());
        mockMvc.perform(put("/goodsItems/{id}", 1)
                            .content(goodsItem)
                            .contentType(MediaType.APPLICATION_JSON))
               .andDo(print())
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.amount", Is.is("Input amount!")));
    }

    @Test
    void updateGoodsItem_shouldHasStatusBadRequestAndValidationMessage_whenAmountIsLessThanOne() throws Exception {
        String goodsItem = getJson(new GoodsItem.Builder().withGoods(new Goods()).withAmount(0).build());
        mockMvc.perform(put("/goodsItems/{id}", 1)
                            .content(goodsItem)
                            .contentType(MediaType.APPLICATION_JSON))
               .andDo(print())
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.amount", Is.is("Amount should be at least one")));
    }

    @Test
    void updateGoodsItem_shouldHasNoErrorsAndStatusOk_whenGoodsIdAndAmountAreValid() throws Exception {
        GoodsItem goodsItem =
            new GoodsItem.Builder().withId(1).withGoods(new Goods()).withAmount(12).build();
        String goodsItemJSON = getJson(goodsItem);
        given(service.update(goodsItem, goodsItem.getId())).willReturn(goodsItem);
        mockMvc.perform(put("/goodsItems/{id}", 1)
                            .content(goodsItemJSON)
                            .contentType(MediaType.APPLICATION_JSON))
               .andDo(print())
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.amount", Is.is(goodsItem.getAmount())));
    }

    @Test
    void deleteGoodsItem_should_when() throws Exception {
        Integer goodsItemId = 1;
        mockMvc.perform(delete("/goodsItems/{id}", goodsItemId))
               .andDo(print())
               .andExpect(status().isOk())
               .andExpect(content().string("GoodsItem deleted successfully"));
    }

    private String getJson(Object object) throws JsonProcessingException {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        return ow.writeValueAsString(object);
    }
}
