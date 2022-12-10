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
        verify(service, times(1)).findAll();
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
    void findSupplierById_shouldHasStatusOkAndReturnService_whenServiceReturnSupplier() throws Exception {
        Supplier supplier = new Supplier.Builder().withId(1).withFirstName("Bob").withLastName("Smith").build();
        given(service.findById(supplier.getId())).willReturn(supplier);

        mockMvc.perform(get("/suppliers/{id}", 1))
               .andDo(print())
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.firstName", Is.is(supplier.getFirstName())))
               .andExpect(jsonPath("$.lastName", Is.is(supplier.getLastName())));
        verify(service, times(1)).findById(supplier.getId());
    }

    @Test
    void findSupplierById_shouldHasStatusBadRequest_whenServiceThrowsServiceEx() throws Exception {
        Integer supplierId = 1;
        given(service.findById(supplierId)).willThrow(new ServiceException("Supplier not found by ID: " + supplierId));
        mockMvc.perform(get("/suppliers/{id}", supplierId))
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
        Integer supplierId = 1;
        Supplier supplier = new Supplier.Builder().withFirstName("Bob").withLastName("Smith").build();
        String supplierJSON = new JSONObject().put("firstName", supplier.getFirstName())
                                              .put("lastName", supplier.getLastName())
                                              .toString();

        given(service.update(supplier, supplierId)).willReturn(supplier);

        mockMvc.perform(put("/suppliers/{id}", supplierId)
                            .content(supplierJSON)
                            .contentType(MediaType.APPLICATION_JSON))
               .andDo(print())
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.firstName", Is.is(supplier.getFirstName())))
               .andExpect(jsonPath("$.lastName", Is.is(supplier.getLastName())));
        verify(service, times(1)).update(new Supplier.Builder()
                                             .withFirstName(supplier.getFirstName())
                                             .withLastName(supplier.getLastName())
                                             .build(), supplierId);
    }

    @Test
    void deleteSupplier_shouldHasStatusOkAndMessage_whenDeletingSupplier() throws Exception {
        Integer supplierId = 1;

        mockMvc.perform(delete("/suppliers/{id}", supplierId))
               .andDo(print())
               .andExpect(status().isOk())
               .andExpect(content().string("Supplier deleted successfully"));
        verify(service, times(1)).delete(supplierId);
    }
}
