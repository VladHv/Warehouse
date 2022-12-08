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
import ua.foxminded.herasimov.warehouse.model.Supplier;
import ua.foxminded.herasimov.warehouse.service.impl.SupplierServiceImpl;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
    void findAllSuppliers_shouldHasStatusOkAndReturnListFromService_whenListNotNullAndNotEmpty() throws Exception {
        Supplier supplier = new Supplier.Builder().withFirstName("Bob").withLastName("Smith").build();
        List<Supplier> allSuppliers = List.of(supplier);
        given(service.findAll()).willReturn(allSuppliers);
        mockMvc.perform(get("/suppliers"))
               .andDo(print())
               .andExpect(status().isOk())
               .andExpect(jsonPath("$", hasSize(1)))
               .andExpect(jsonPath("$[0].firstName", Is.is(supplier.getFirstName())))
               .andExpect(jsonPath("$[0].lastName", Is.is(supplier.getLastName())));
    }

    @Test
    void findAllSuppliers_shouldHasStatusNotFound_whenListFromServiceIsNull() throws Exception {
        List<Supplier> allSuppliers = null;
        given(service.findAll()).willReturn(allSuppliers);
        mockMvc.perform(get("/suppliers"))
               .andDo(print())
               .andExpect(status().isNotFound());
    }

    @Test
    void findAllSuppliers_shouldHasStatusNotFound_whenListFromServiceIsEmpty() throws Exception {
        List<Supplier> allSuppliers = Collections.emptyList();
        given(service.findAll()).willReturn(allSuppliers);
        mockMvc.perform(get("/suppliers"))
               .andDo(print())
               .andExpect(status().isNotFound());
    }

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
        String supplier = new JSONObject().put("firstName", "Bob")
                                          .put("lastName", "Smith")
                                          .toString();
        mockMvc.perform(post("/suppliers")
                            .content(supplier)
                            .contentType(MediaType.APPLICATION_JSON))
               .andDo(print())
               .andExpect(status().isCreated())
               .andExpect(content().string("Supplier is valid"));
        verify(service, times(1)).create(new Supplier.Builder().withFirstName("Bob").withLastName("Smith").build());
        // TODO: 08.12.2022 add verification of service call and messages for other methods
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
