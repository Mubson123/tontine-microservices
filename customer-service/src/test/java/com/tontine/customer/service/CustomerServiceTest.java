package com.tontine.customer.service;

import com.tontine.customer.exception.CustomerNotFoundException;
import com.tontine.customer.fixtures.CustomerFixtures;
import com.tontine.customer.mapper.CustomerMapper;
import com.tontine.customer.model.ApiCustomerRequest;
import com.tontine.customer.model.ApiCustomerResponse;
import com.tontine.customer.models.Customer;
import com.tontine.customer.repository.CustomerRepository;
import com.tontine.customer.service.impl.CustomerServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private CustomerMapper customerMapper;
    @InjectMocks
    private CustomerServiceImpl customerService;


    @Test
    void shouldReturnAllCustomersSuccessfully() {
        List<Customer> customers = CustomerFixtures.customerList;
        List<ApiCustomerResponse> expected = CustomerFixtures.responseList;
        when(customerRepository.findAll()).thenReturn(customers);
        when(customerMapper.toApiCustomerResponses(customers)).thenReturn(expected);

        List<ApiCustomerResponse> actual = customerService.getCustomers();

        assertNotNull(actual);
        assertEquals(2, actual.size());
        assertEquals(actual, expected);

        verify(customerRepository, times(1)).findAll();
        verify(customerMapper, times(1)).toApiCustomerResponses(customers);
    }

    @Test
    void shouldReturnCustomerByIdSuccessfully() {
        Customer customer = CustomerFixtures.customer2();
        UUID customerId = customer.getId();
        ApiCustomerResponse expected = CustomerFixtures.apiCustomerResponse2();
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(customerMapper.toApiCustomerResponse(customer)).thenReturn(expected);

        ApiCustomerResponse actual = customerService.getCustomerById(customerId);

        assertNotNull(actual);
        assertEquals(actual, expected);
        verify(customerRepository, times(1)).findById(customerId);
        verify(customerMapper, times(1)).toApiCustomerResponse(customer);
    }

    @Test
    void shouldReturnCustomerByIdNotFound() {
        UUID customerId = UUID.randomUUID();
        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(CustomerNotFoundException.class,
                () -> customerService.getCustomerById(customerId));

        String expectedMessage = "Customer with ID %s not found".formatted(customerId);
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
        verify(customerRepository, times(1)).findById(customerId);
        verify(customerMapper, times(0)).toApiCustomerResponse(any());
    }

    @Test
    void shouldCreateCustomerSuccessfully() {
        ApiCustomerRequest customerRequest = CustomerFixtures.apiCustomerRequest1();
        Customer customer = CustomerFixtures.customer1();
        ApiCustomerResponse expected = CustomerFixtures.apiCustomerResponse1();

        when(customerMapper.toCustomer(customerRequest)).thenReturn(customer);
        when(customerRepository.save(customer)).thenReturn(customer);
        when(customerMapper.toApiCustomerResponse(customer)).thenReturn(expected);

        ApiCustomerResponse actual = customerService.createCustomer(customerRequest);
        assertNotNull(actual);
        assertEquals(actual, expected);

        verify(customerMapper, times(1)).toCustomer(customerRequest);
        verify(customerRepository, times(1)).save(customer);
        verify(customerMapper, times(1)).toApiCustomerResponse(customer);
    }

    @Test
    void shouldUpdateCustomerSuccessfully() {
        ApiCustomerRequest customerRequest = CustomerFixtures.apiCustomerRequest2();
        Customer customer = CustomerFixtures.customer2();
        UUID customerId = customer.getId();
        ApiCustomerResponse expected = CustomerFixtures.apiCustomerResponse2();

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        doNothing().when(customerMapper).updateCustomerFromRequest(customerRequest, customer);
        when(customerMapper.toApiCustomerResponse(customer)).thenReturn(expected);

        ApiCustomerResponse actual = customerService.updateCustomer(customerId, customerRequest);

        assertNotNull(actual);
        assertEquals(actual, expected);

        verify(customerRepository, times(1)).findById(customerId);
        verify(customerMapper, times(1)).updateCustomerFromRequest(customerRequest, customer);
        verify(customerMapper, times(1)).toApiCustomerResponse(customer);
    }

    @Test
    void deleteCustomer() {
        Customer customer = CustomerFixtures.customer1();
        UUID customerId = customer.getId();

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        doNothing().when(customerRepository).delete(customer);

        customerService.deleteCustomer(customerId);

        verify(customerRepository, times(1)).findById(customerId);
        verify(customerRepository, times(1)).delete(customer);
    }
}