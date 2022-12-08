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
import ua.foxminded.herasimov.warehouse.service.impl.SupplierServiceImpl;

import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = SupplierController.class)
class SupplierControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SupplierServiceImpl service;

    @InjectMocks
    private SupplierController controller;

    @Test
    void createSupplier_shouldHasStatusBadRequestAndValidationMessage_whenFirstNameIsBlank() throws Exception {
        String supplier = new JSONObject().put("firstName", "")
                                          .put("lastName", "Smith")
                                          .toString();
        mockMvc.perform(post("/suppliers")
                            .content(supplier)
                            .contentType(MediaType.APPLICATION_JSON))
               .andDo(print())
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.firstName", Is.is("Length should be from 2 to 250")));
    }

    @Test
    void createSupplier_shouldHasStatusBadRequestAndValidationMessage_whenFirstNameIsNull() throws Exception {
        String supplier = new JSONObject().put("lastName", "Smith")
                                          .toString();
        mockMvc.perform(post("/suppliers")
                            .content(supplier)
                            .contentType(MediaType.APPLICATION_JSON))
               .andDo(print())
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.firstName", Is.is("First name required")));
    }

    @Test
    void createSupplier_shouldHasStatusBadRequestAndValidationMessage_whenFirstNameSizeLessThanTwo() throws Exception {
        String supplier = new JSONObject().put("firstName", "A")
                                          .put("lastName", "Smith")
                                          .toString();
        mockMvc.perform(post("/suppliers")
                            .content(supplier)
                            .contentType(MediaType.APPLICATION_JSON))
               .andDo(print())
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.firstName", Is.is("Length should be from 2 to 250")));
    }

    @Test
    void createSupplier_shouldHasStatusBadRequestAndValidationMessage_whenFirstNameSizeMoreThanTwoHundredAndFifty() throws
        Exception {
        String supplier = new JSONObject().put("firstName", "A".repeat(251))
                                          .put("lastName", "Smith")
                                          .toString();
        mockMvc.perform(post("/suppliers")
                            .content(supplier)
                            .contentType(MediaType.APPLICATION_JSON))
               .andDo(print())
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.firstName", Is.is("Length should be from 2 to 250")));
    }

    @Test
    void createSupplier_shouldHasStatusBadRequestAndValidationMessage_whenLastNameIsBlank() throws Exception {
        String supplier = new JSONObject().put("firstName", "Bob")
                                          .put("lastName", "")
                                          .toString();
        mockMvc.perform(post("/suppliers")
                            .content(supplier)
                            .contentType(MediaType.APPLICATION_JSON))
               .andDo(print())
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.lastName", Is.is("Length should be from 2 to 250")));
    }

    @Test
    void createSupplier_shouldHasStatusBadRequestAndValidationMessage_whenLastNameIsNull() throws Exception {
        String supplier = new JSONObject().put("firstName", "Bob")
                                          .toString();
        mockMvc.perform(post("/suppliers")
                            .content(supplier)
                            .contentType(MediaType.APPLICATION_JSON))
               .andDo(print())
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.lastName", Is.is("Last name required")));
    }

    @Test
    void createSupplier_shouldHasStatusBadRequestAndValidationMessage_whenLastNameSizeLessThanTwo() throws Exception {
        String supplier = new JSONObject().put("firstName", "Bob")
                                          .put("lastName", "A")
                                          .toString();
        mockMvc.perform(post("/suppliers")
                            .content(supplier)
                            .contentType(MediaType.APPLICATION_JSON))
               .andDo(print())
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.lastName", Is.is("Length should be from 2 to 250")));
    }

    @Test
    void createSupplier_shouldHasStatusBadRequestAndValidationMessage_whenLastNameSizeMoreThanTwoHundredAndFifty() throws
        Exception {
        String supplier = new JSONObject().put("firstName", "Bob")
                                          .put("lastName", "A".repeat(251))
                                          .toString();
        mockMvc.perform(post("/suppliers")
                            .content(supplier)
                            .contentType(MediaType.APPLICATION_JSON))
               .andDo(print())
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.lastName", Is.is("Length should be from 2 to 250")));
    }

    @Test
    void createSupplier_shouldHasNoErrorsAndStatusCreated_whenFirstNameAndLastNameAreValid() throws Exception {
        MediaType textPlainUtf8 = new MediaType(MediaType.TEXT_PLAIN, StandardCharsets.UTF_8);
        String supplier = new JSONObject().put("firstName", "Bob")
                                          .put("lastName", "Smith")
                                          .toString();
        mockMvc.perform(post("/suppliers")
                            .content(supplier)
                            .contentType(MediaType.APPLICATION_JSON))
               .andDo(print())
               .andExpect(status().isCreated())
               .andExpect(content().contentType(textPlainUtf8));
    }

    @Test
    void updateSupplier_shouldHasStatusBadRequestAndValidationMessage_whenFirstNameIsBlank() throws Exception {
        String supplier = new JSONObject().put("firstName", "")
                                          .put("lastName", "Smith")
                                          .toString();
        mockMvc.perform(put("/suppliers/{id}", 1)
                            .content(supplier)
                            .contentType(MediaType.APPLICATION_JSON))
               .andDo(print())
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.firstName", Is.is("Length should be from 2 to 250")));
    }

    @Test
    void updateSupplier_shouldHasStatusBadRequestAndValidationMessage_whenFirstNameIsNull() throws Exception {
        String supplier = new JSONObject().put("lastName", "Smith")
                                          .toString();
        mockMvc.perform(put("/suppliers/{id}", 1)
                            .content(supplier)
                            .contentType(MediaType.APPLICATION_JSON))
               .andDo(print())
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.firstName", Is.is("First name required")));
    }

    @Test
    void updateSupplier_shouldHasStatusBadRequestAndValidationMessage_whenFirstNameSizeLessThanTwo() throws Exception {
        String supplier = new JSONObject().put("firstName", "A")
                                          .put("lastName", "Smith")
                                          .toString();
        mockMvc.perform(put("/suppliers/{id}", 1)
                            .content(supplier)
                            .contentType(MediaType.APPLICATION_JSON))
               .andDo(print())
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.firstName", Is.is("Length should be from 2 to 250")));
    }

    @Test
    void updateSupplier_shouldHasStatusBadRequestAndValidationMessage_whenFirstNameSizeMoreThanTwoHundredAndFifty() throws
        Exception {
        String supplier = new JSONObject().put("firstName", "A".repeat(251))
                                          .put("lastName", "Smith")
                                          .toString();
        mockMvc.perform(put("/suppliers/{id}", 1)
                            .content(supplier)
                            .contentType(MediaType.APPLICATION_JSON))
               .andDo(print())
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.firstName", Is.is("Length should be from 2 to 250")));
    }

    @Test
    void updateSupplier_shouldHasStatusBadRequestAndValidationMessage_whenLastNameIsBlank() throws Exception {
        String supplier = new JSONObject().put("firstName", "Bob")
                                          .put("lastName", "")
                                          .toString();
        mockMvc.perform(put("/suppliers/{id}", 1)
                            .content(supplier)
                            .contentType(MediaType.APPLICATION_JSON))
               .andDo(print())
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.lastName", Is.is("Length should be from 2 to 250")));
    }

    @Test
    void updateSupplier_shouldHasStatusBadRequestAndValidationMessage_whenLastNameIsNull() throws Exception {
        String supplier = new JSONObject().put("firstName", "Bob")
                                          .toString();
        mockMvc.perform(put("/suppliers/{id}", 1)
                            .content(supplier)
                            .contentType(MediaType.APPLICATION_JSON))
               .andDo(print())
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.lastName", Is.is("Last name required")));
    }

    @Test
    void updateSupplier_shouldHasStatusBadRequestAndValidationMessage_whenLastNameSizeLessThanTwo() throws Exception {
        String supplier = new JSONObject().put("firstName", "Bob")
                                          .put("lastName", "A")
                                          .toString();
        mockMvc.perform(put("/suppliers/{id}", 1)
                            .content(supplier)
                            .contentType(MediaType.APPLICATION_JSON))
               .andDo(print())
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.lastName", Is.is("Length should be from 2 to 250")));
    }

    @Test
    void updateSupplier_shouldHasStatusBadRequestAndValidationMessage_whenLastNameSizeMoreThanTwoHundredAndFifty() throws
        Exception {
        String supplier = new JSONObject().put("firstName", "Bob")
                                          .put("lastName", "A".repeat(251))
                                          .toString();
        mockMvc.perform(put("/suppliers/{id}", 1)
                            .content(supplier)
                            .contentType(MediaType.APPLICATION_JSON))
               .andDo(print())
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.lastName", Is.is("Length should be from 2 to 250")));
    }

    @Test
    void updateSupplier_shouldHasNoErrorsAndStatusOk_whenFirstNameAndLastNameAreValid() throws Exception {
        String supplier = new JSONObject().put("firstName", "Bob")
                                          .put("lastName", "Smith")
                                          .toString();
        mockMvc.perform(put("/suppliers/{id}", 1)
                            .content(supplier)
                            .contentType(MediaType.APPLICATION_JSON))
               .andDo(print())
               .andExpect(status().isOk());
    }

    // TODO: 08.12.2022 to test DELETE use andReturn() for MockMvc, see: https://stackoverflow.com/questions/18336277/how-to-check-string-in-response-body-with-mockmvc, https://stackoverflow.com/questions/58192256/testing-the-controller-which-returns-response-entity

}
