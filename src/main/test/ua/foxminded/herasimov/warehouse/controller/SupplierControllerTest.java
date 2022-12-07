package ua.foxminded.herasimov.warehouse.controller;

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
    void createSupplier_shouldHasStatusBadRequest_whenFirstNameIsBlank() throws Exception {
        mockMvc.perform(post("/suppliers")
                            .param("firstName", "")
                            .param("lastName", "Smith"))
               .andExpect(status().isBadRequest())
               .andDo(print());
    }

    @Test
    void createSupplier_shouldHasStatusBadRequest_whenFirstNameIsNull() throws Exception {
        mockMvc.perform(post("/suppliers")
                            .param("lastName", "Smith"))
               .andExpect(status().isBadRequest())
               .andDo(print());
    }

    @Test
    void createSupplier_shouldHasStatusBadRequest_whenFirstNameSizeLessThanTwo() throws Exception {
        mockMvc.perform(post("/suppliers")
                            .param("firstName", "A")
                            .param("lastName", "Smith"))
               .andExpect(status().isBadRequest())
               .andDo(print());
    }

    @Test
    void createSupplier_shouldHasStatusBadRequest_whenFirstNameSizeMoreThanTwoHundredAndFifty() throws Exception {
        mockMvc.perform(post("/suppliers")
                            .param("firstName", "A".repeat(251))
                            .param("lastName", "Smith"))
               .andExpect(status().isBadRequest())
               .andDo(print());
    }

    @Test
    void createSupplier_shouldHasStatusBadRequest_whenLastNameIsBlank() throws Exception {
        mockMvc.perform(post("/suppliers")
                            .param("firstName", "Bob")
                            .param("lastName", ""))
               .andExpect(status().isBadRequest())
               .andDo(print());
    }

    @Test
    void createSupplier_shouldHasStatusBadRequest_whenLastNameIsNull() throws Exception {
        mockMvc.perform(post("/suppliers")
                            .param("firstName", "Bob"))
               .andExpect(status().isBadRequest())
               .andDo(print());
    }

    @Test
    void createSupplier_shouldHasStatusBadRequest_whenLastNameSizeLessThanTwo() throws Exception {
        mockMvc.perform(post("/suppliers")
                            .param("firstName", "Bob")
                            .param("lastName", "A"))
               .andExpect(status().isBadRequest())
               .andDo(print());
    }

    @Test
    void createSupplier_shouldHasStatusBadRequest_whenLastNameSizeMoreThanTwoHundredAndFifty() throws Exception {
        mockMvc.perform(post("/suppliers")
                            .param("firstName", "Bob")
                            .param("lastName", "A".repeat(251)))
               .andExpect(status().isBadRequest())
               .andDo(print());
    }

    // TODO: 07.12.2022 update previous test methods like next one

    @Test
    void createSupplier_shouldHasNoErrors_whenFirstNameAndLastNameAreValid() throws Exception {
        MediaType textPlainUtf8 = new MediaType(MediaType.TEXT_PLAIN, StandardCharsets.UTF_8);
        String supplier = "{\"firstName\": \"Bob\", \"lastName\" : \"Smith\"}";
        mockMvc.perform(post("/suppliers")
                            .content(supplier)
                            .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isCreated())
               .andExpect(content().contentType(textPlainUtf8))
               .andDo(print());
    }

    @Test
    void updateSupplier_shouldHasFirstNameFieldError_whenFirstNameIsBlank() throws Exception {
        mockMvc.perform(post("/suppliers/{id}", 1)
                            .param("firstName", "")
                            .param("lastName", "Smith"))
               .andExpect(model().attributeHasFieldErrors("supplier", "firstName"))
               .andExpect(view().name("supplier_page"))
               .andExpect(status().isOk())
               .andDo(print());
    }

    @Test
    void updateSupplier_shouldHasFirstNameFieldError_whenFirstNameIsNull() throws Exception {
        mockMvc.perform(post("/suppliers/{id}", 1)
                            .param("lastName", "Smith"))
               .andExpect(model().attributeHasFieldErrors("supplier", "firstName"))
               .andExpect(view().name("supplier_page"))
               .andExpect(status().isOk())
               .andDo(print());
    }

    @Test
    void updateSupplier_shouldHasFirstNameFieldError_whenFirstNameSizeLessThanTwo() throws Exception {
        mockMvc.perform(post("/suppliers/{id}", 1)
                            .param("firstName", "A")
                            .param("lastName", "Smith"))
               .andExpect(model().attributeHasFieldErrors("supplier", "firstName"))
               .andExpect(view().name("supplier_page"))
               .andExpect(status().isOk())
               .andDo(print());
    }

    @Test
    void updateSupplier_shouldHasFirstNameFieldError_whenFirstNameSizeMoreThanTwoHundredAndFifty() throws Exception {
        mockMvc.perform(post("/suppliers/{id}", 1)
                            .param("firstName", "A".repeat(251))
                            .param("lastName", "Smith"))
               .andExpect(model().attributeHasFieldErrors("supplier", "firstName"))
               .andExpect(view().name("supplier_page"))
               .andExpect(status().isOk())
               .andDo(print());
    }

    @Test
    void updateSupplier_shouldHasLastNameFieldError_whenLastNameIsBlank() throws Exception {
        mockMvc.perform(post("/suppliers/{id}", 1)
                            .param("firstName", "Bob")
                            .param("lastName", ""))
               .andExpect(model().attributeHasFieldErrors("supplier", "lastName"))
               .andExpect(view().name("supplier_page"))
               .andExpect(status().isOk())
               .andDo(print());
    }

    @Test
    void updateSupplier_shouldHasLastNameFieldError_whenLastNameIsNull() throws Exception {
        mockMvc.perform(post("/suppliers/{id}", 1)
                            .param("firstName", "Bob"))
               .andExpect(model().attributeHasFieldErrors("supplier", "lastName"))
               .andExpect(view().name("supplier_page"))
               .andExpect(status().isOk())
               .andDo(print());
    }

    @Test
    void updateSupplier_shouldHasLastNameFieldError_whenLastNameSizeLessThanTwo() throws Exception {
        mockMvc.perform(post("/suppliers/{id}", 1)
                            .param("firstName", "Bob")
                            .param("lastName", "A"))
               .andExpect(model().attributeHasFieldErrors("supplier", "lastName"))
               .andExpect(view().name("supplier_page"))
               .andExpect(status().isOk())
               .andDo(print());
    }

    @Test
    void updateSupplier_shouldHasLastNameFieldError_whenLastNameSizeMoreThanTwoHundredAndFifty() throws Exception {
        mockMvc.perform(post("/suppliers/{id}", 1)
                            .param("firstName", "Bob")
                            .param("lastName", "A".repeat(251)))
               .andExpect(model().attributeHasFieldErrors("supplier", "lastName"))
               .andExpect(view().name("supplier_page"))
               .andExpect(status().isOk())
               .andDo(print());
    }

    @Test
    void updateSupplier_shouldHasNoErrors_whenFirstNameAndLastNameAreValid() throws Exception {
        mockMvc.perform(post("/suppliers/{id}", 1)
                            .param("firstName", "Bob")
                            .param("lastName", "Smith"))
               .andExpect(model().attributeHasNoErrors())
               .andExpect(redirectedUrl("/suppliers/1"))
               .andExpect(status().is3xxRedirection())
               .andDo(print());
    }
}
