package ua.foxminded.herasimov.warehouse.controller;

import org.hamcrest.core.Is;
import org.json.JSONObject;
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
import ua.foxminded.herasimov.warehouse.service.impl.GoodsServiceImpl;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
    void getAllGoods_shouldHasStatusOkAndReturnListFromService_whenListNotNullAndNotEmpty() throws Exception {
        Goods goods = new Goods.Builder().withName("Tomato").withPrice(99).build();
        List<Goods> allGoods = List.of(goods);
        given(service.findAll()).willReturn(allGoods);
        mockMvc.perform(get("/goods"))
               .andDo(print())
               .andExpect(status().isOk())
               .andExpect(jsonPath("$", hasSize(1)))
               .andExpect(jsonPath("$[0].name", Is.is(goods.getName())))
               .andExpect(jsonPath("$[0].price", Is.is(goods.getPrice())));
    }

    @Test
    void getAllGoods_shouldHasStatusNotFound_whenListFromServiceIsNull() throws Exception {
        List<Goods> allGoods = null;
        given(service.findAll()).willReturn(allGoods);
        mockMvc.perform(get("/goods"))
               .andDo(print())
               .andExpect(status().isNotFound());
    }

    @Test
    void getAllGoods_shouldHasStatusNotFound_whenListFromServiceIsEmpty() throws Exception {
        List<Goods> allGoods = Collections.emptyList();
        given(service.findAll()).willReturn(allGoods);
        mockMvc.perform(get("/goods"))
               .andDo(print())
               .andExpect(status().isNotFound());
    }

    @Test
    void findSupplierById_shouldHasStatusOkAndReturnEntity_whenServiceReturnGoods() throws Exception {
        Goods goods = new Goods.Builder().withId(1).withName("Tomato").withPrice(99).build();
        given(service.findById(goods.getId())).willReturn(goods);

        mockMvc.perform(get("/goods/{id}", 1))
               .andDo(print())
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.name", Is.is(goods.getName())))
               .andExpect(jsonPath("$.price", Is.is(goods.getPrice())));
    }

    @Test
    void findSupplierById_shouldHasStatusBadRequest_whenServiceThrowsServiceEx() throws Exception {
        Integer supplierId = 1;
        given(service.findById(supplierId)).willThrow(new ServiceException("Goods not found by ID: " + supplierId));
        mockMvc.perform(get("/goods/{id}", supplierId))
               .andDo(print())
               .andExpect(status().isNotFound());
    }

    @Test
    void createGoods_shouldHasStatusBadRequestAndValidationMessage_whenNameSizeOneOrLess() throws Exception {
        String goods = new JSONObject().put("name", "A")
                                       .put("price", 99)
                                       .toString();
        mockMvc.perform(post("/goods")
                            .content(goods)
                            .contentType(MediaType.APPLICATION_JSON))
               .andDo(print())
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.name", Is.is("Length should be from 2 to 250")));
    }

    @Test
    void createGoods_shouldHasStatusBadRequestAndValidationMessage_whenNameMoreThanTwoHundredAndFifty() throws
        Exception {
        String goods = new JSONObject().put("name", "A".repeat(251))
                                       .put("price", 99)
                                       .toString();
        mockMvc.perform(post("/goods")
                            .content(goods)
                            .contentType(MediaType.APPLICATION_JSON))
               .andDo(print())
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.name", Is.is("Length should be from 2 to 250")));
    }

    @Test
    void createGoods_shouldHasStatusBadRequestAndValidationMessage_whenNameIsNull() throws Exception {
        String goods = new JSONObject().put("price", 99)
                                       .toString();
        mockMvc.perform(post("/goods")
                            .content(goods)
                            .contentType(MediaType.APPLICATION_JSON))
               .andDo(print())
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.name", Is.is("Input name of goods!")));
    }

    @Test
    void createGoods_shouldHasStatusBadRequestAndValidationMessage_whenPriceValueIsLessThanOne() throws Exception {
        String goods = new JSONObject().put("name", "Tomato")
                                       .put("price", -1)
                                       .toString();
        mockMvc.perform(post("/goods")
                            .content(goods)
                            .contentType(MediaType.APPLICATION_JSON))
               .andDo(print())
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.price", Is.is("Price should be at least one dollar")));
    }

    @Test
    void createGoods_shouldHasStatusBadRequestAndValidationMessage_whenPriceValueIsNull() throws Exception {
        String goods = new JSONObject().put("name", "Tomato")
                                       .toString();
        mockMvc.perform(post("/goods")
                            .content(goods)
                            .contentType(MediaType.APPLICATION_JSON))
               .andDo(print())
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.price", Is.is("Input price!")));
    }

    @Test
    void createGoods_shouldHasNoErrorsAndStatusCreated_whenNameAndPriceAreValid() throws Exception {
        String goods = new JSONObject().put("name", "Tomato")
                                       .put("price", 99)
                                       .toString();
        mockMvc.perform(post("/goods")
                            .content(goods)
                            .contentType(MediaType.APPLICATION_JSON))
               .andDo(print())
               .andExpect(status().isCreated())
               .andExpect(content().string("Goods is valid"));
    }

    @Test
    void updateGoods_shouldHasStatusBadRequestAndValidationMessage_whenNameSizeOneOrLess() throws Exception {
        String goods = new JSONObject().put("name", "A")
                                       .put("price", 99)
                                       .toString();
        mockMvc.perform(put("/goods/{id}", 1)
                            .content(goods)
                            .contentType(MediaType.APPLICATION_JSON))
               .andDo(print())
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.name", Is.is("Length should be from 2 to 250")));
    }

    @Test
    void updateGoods_shouldHasStatusBadRequestAndValidationMessage_whenNameMoreThanTwoHundredAndFifty() throws
        Exception {
        String goods = new JSONObject().put("name", "A".repeat(251))
                                       .put("price", 99)
                                       .toString();
        mockMvc.perform(put("/goods/{id}", 1)
                            .content(goods)
                            .contentType(MediaType.APPLICATION_JSON))
               .andDo(print())
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.name", Is.is("Length should be from 2 to 250")));
    }

    @Test
    void updateGoods_shouldHasStatusBadRequestAndValidationMessage_whenNameIsNull() throws Exception {
        String goods = new JSONObject().put("price", 99)
                                       .toString();
        mockMvc.perform(put("/goods/{id}", 1)
                            .content(goods)
                            .contentType(MediaType.APPLICATION_JSON))
               .andDo(print())
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.name", Is.is("Input name of goods!")));
    }

    @Test
    void updateGoods_shouldHasStatusBadRequestAndValidationMessage_whenPriceValueIsLessThanOne() throws Exception {
        String goods = new JSONObject().put("name", "Tomato")
                                       .put("price", -1)
                                       .toString();
        mockMvc.perform(put("/goods/{id}", 1)
                            .content(goods)
                            .contentType(MediaType.APPLICATION_JSON))
               .andDo(print())
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.price", Is.is("Price should be at least one dollar")));
    }

    @Test
    void updateGoods_shouldHasStatusBadRequestAndValidationMessage_whenPriceValueIsNull() throws Exception {
        String goods = new JSONObject().put("name", "Tomato")
                                       .toString();
        mockMvc.perform(put("/goods/{id}", 1)
                            .content(goods)
                            .contentType(MediaType.APPLICATION_JSON))
               .andDo(print())
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.price", Is.is("Input price!")));
    }

    @Test
    void updateGoods_shouldHasNoErrorsAndStatusOk_whenNameAndPriceAreValid() throws Exception {
        Integer goodsId = 1;
        Goods goods = new Goods.Builder().withName("Tomato").withPrice(99).build();
        String goodsJSON = new JSONObject().put("name", "Tomato")
                                           .put("price", 99)
                                           .toString();

        given(service.update(goods, goodsId)).willReturn(goods);

        mockMvc.perform(put("/goods/{id}", 1)
                            .content(goodsJSON)
                            .contentType(MediaType.APPLICATION_JSON))
               .andDo(print())
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.name", Is.is(goods.getName())))
               .andExpect(jsonPath("$.price", Is.is(goods.getPrice())));
    }

    @Test
    void deleteSupplier_shouldHasStatusOkAndConfirmingMessage_whenDeletingSupplierWithNoRequestedMessage() throws
        Exception {
        Integer goodsId = 1;
        mockMvc.perform(delete("/goods/{id}", goodsId))
               .andDo(print())
               .andExpect(status().isOk())
               .andExpect(content().string("Goods deleted successfully"));
    }

    @Test
    void deleteSupplier_shouldHasStatusOkAndMessage_whenDeletingSupplierWithValidRequestedMessage() throws Exception {
        Integer goodsId = 1;
        String requestedMessage = " YO!";
        mockMvc.perform(delete("/goods/{id}", goodsId)
                            .param("message", requestedMessage))
               .andDo(print())
               .andExpect(status().isOk())
               .andExpect(content().string("Goods deleted successfully" + requestedMessage));
    }

    @Test
    void deleteSupplier_shouldHasStatusBadRequestAndValidationMessage_whenDeletingSupplierWithRequestedMessageShorterThanTwo() throws
        Exception {
        Integer goodsId = 1;
        String requestedMessage = "A";
        mockMvc.perform(delete("/goods/{id}", goodsId)
                            .param("message", requestedMessage))
               .andDo(print())
               .andExpect(status().isBadRequest())
               .andExpect(content().string(
                   "not valid due to validation error: deleteGoods.message: Length should be from 2 to 5"));

    }

    @Test
    void deleteSupplier_shouldHasStatusBadRequestAndValidationMessage_whenDeletingSupplierWithRequestedMessageLongerThanFive() throws
        Exception {
        Integer goodsId = 1;
        String requestedMessage = "A".repeat(6);
        mockMvc.perform(delete("/goods/{id}", goodsId)
                            .param("message", requestedMessage))
               .andDo(print())
               .andExpect(status().isBadRequest())
               .andExpect(content().string(
                   "not valid due to validation error: deleteGoods.message: Length should be from 2 to 5"));
    }
}
