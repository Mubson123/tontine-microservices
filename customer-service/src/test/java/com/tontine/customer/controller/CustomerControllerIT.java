package com.tontine.customer.controller;

import com.tontine.customer.fixtures.CustomerFixtures;
import com.tontine.customer.model.ApiCustomerRequest;
import com.tontine.customer.model.ApiCustomerResponse;
import com.tontine.customer.model.ApiProfile;
import com.tontine.customer.models.utils.Status;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("dev")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CustomerControllerIT {
    static final String BASE_PATH = "/api/customers";
    static final String ADMIN_BASE_PATH = "/api/admin/customers/{customerId}";
    static String customerId;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @Order(10)
    void shouldCreateCustomerSuccessfully() throws Exception {
        ApiCustomerRequest request = CustomerFixtures.apiCustomerRequest1();
        var response = mockMvc.perform(post(BASE_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.lastname").value(request.getLastname()))
                .andReturn()
                .getResponse();
        ApiCustomerResponse created = objectMapper.readValue(response.getContentAsString(), ApiCustomerResponse.class);
        customerId = String.valueOf(created.getId());

        mockMvc.perform(post(BASE_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(CustomerFixtures.apiCustomerRequest2())))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    @Order(20)
    void shouldDeactivateCustomerSuccessfully() throws Exception {
        mockMvc.perform(put(ADMIN_BASE_PATH + "/deactivate", customerId))
                .andDo(print())
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.status").value(Status.INACTIVE.name()));
    }

    @Test
    @Order(30)
    void shouldSuspendCustomerSuccessfully() throws Exception {
        mockMvc.perform(put(ADMIN_BASE_PATH + "/suspend", customerId))
                .andDo(print())
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.status").value(Status.SUSPENDED.name()));
    }

    @Test
    @Order(40)
    void shouldActivateCustomerSuccessfully() throws Exception {
        mockMvc.perform(put(ADMIN_BASE_PATH + "/activate", customerId))
                .andDo(print())
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.status").value(Status.ACTIVE.name()));
    }

    @Test
    @Order(50)
    void shouldReturnAllCustomersSuccessfully() throws Exception {
        mockMvc.perform(get(BASE_PATH))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].firstname").value("John"))
                .andExpect(jsonPath("$[1].firstname").value("Sophia"));

    }

    @Test
    @Order(60)
    void shouldReturnCustomerByIdSuccessfully() throws Exception {
        mockMvc.perform(get(BASE_PATH + "/{customerId}", customerId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstname").value("John"));
    }

    @Test
    @Order(62)
    void shouldReturnCustomerByIdNotFound() throws Exception {
        String id = String.valueOf(UUID.randomUUID());
        mockMvc.perform(get(BASE_PATH + "/{id}", id))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Customer with ID %s not found".formatted(id)));
    }

    @Test
    @Order(70)
    void shouldUpdateCustomerSuccessfully() throws Exception {
        ApiCustomerRequest update = CustomerFixtures.apiCustomerRequest2();
        mockMvc.perform(put(BASE_PATH + "/{customerId}", customerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andDo(print())
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.lastname").value("Smith"));
    }

    @Test
    @Order(80)
    void shouldUpdateCustomerProfileSuccessfully() throws Exception {
        ApiProfile update = CustomerFixtures.profile();
        mockMvc.perform(put(BASE_PATH + "/{customerId}/profile", customerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andDo(print())
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.lastname").value("Jackson"));
    }

    @Test
    @Order(90)
    void shouldDeleteCustomerSuccessfully() throws Exception {
        mockMvc.perform(delete(BASE_PATH + "/{customerId}", customerId))
                .andDo(print())
                .andExpect(status().isNoContent());
    }
}