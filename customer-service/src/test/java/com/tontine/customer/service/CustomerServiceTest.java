package com.tontine.customer.service;

import com.tontine.customer.exception.CustomerNotFoundException;
import com.tontine.customer.fixtures.CustomerFixtures;
import com.tontine.customer.mapper.CustomerMapper;
import com.tontine.customer.model.*;
import com.tontine.customer.models.Customer;
import com.tontine.customer.models.utils.Address;
import com.tontine.customer.models.utils.Status;
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
        assertEquals(expected, actual);
        verify(customerRepository).findAll();
        verify(customerMapper).toApiCustomerResponses(customers);
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
        assertEquals(expected, actual);
        verify(customerRepository).findById(customerId);
        verify(customerMapper).toApiCustomerResponse(customer);
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
        verify(customerRepository).findById(customerId);
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
        assertEquals(expected, actual);
        verify(customerMapper).toCustomer(customerRequest);
        verify(customerRepository).save(customer);
        verify(customerMapper).toApiCustomerResponse(customer);
    }

    @Test
    void shouldUpdateCustomerSuccessfully() {
        ApiCustomerRequest customerRequest = CustomerFixtures.apiCustomerRequest2();
        Customer customer = CustomerFixtures.customer2();
        UUID customerId = customer.getId();
        ApiCustomerResponse expected = CustomerFixtures.apiCustomerResponse2();

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        doNothing().when(customerMapper).updateCustomerFromRequest(customerRequest, customer);
        when(customerRepository.save(customer)).thenReturn(customer);
        when(customerMapper.toApiCustomerResponse(customer)).thenReturn(expected);

        ApiCustomerResponse actual = customerService.updateCustomer(customerId, customerRequest);

        assertNotNull(actual);
        assertEquals(expected, actual);
        verify(customerRepository).findById(customerId);
        verify(customerMapper).updateCustomerFromRequest(customerRequest, customer);
        verify(customerMapper).toApiCustomerResponse(customer);
    }

    @Test
    void shouldUpdateProfileSuccessfully() {
        Customer customer = CustomerFixtures.customer2();
        UUID customerId = customer.getId();
        ApiProfile profile = CustomerFixtures.profile();
        Address address = CustomerFixtures.address2();
        ApiCustomerResponse expected = CustomerFixtures.apiCustomerResponse2();

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(customerMapper.toAddress(profile.getAddress())).thenReturn(address);
        when(customerRepository.save(customer)).thenReturn(customer);
        when(customerMapper.toApiCustomerResponse(customer)).thenReturn(expected);

        ApiCustomerResponse actual = customerService.updateProfile(customerId, profile);

        assertNotNull(actual);
        assertEquals(expected, actual);
        verify(customerRepository).save(customer);
        verify(customerRepository).findById(customerId);
        verify(customerMapper).toAddress(profile.getAddress());
        verify(customerMapper).toApiCustomerResponse(customer);
    }

    @Test
    void shouldUpdateSuspendedProfileException() {
        Customer customer = CustomerFixtures.customer2();
        customer.setStatus(Status.SUSPENDED);
        UUID customerId = customer.getId();
        ApiProfile profile = CustomerFixtures.profile();
        profile.setStatus(ApiStatus.SUSPENDED);
        Address address = CustomerFixtures.address2();
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(customerMapper.toAddress(profile.getAddress())).thenReturn(address);

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> customerService.updateProfile(customerId, profile));

        String expectedMessage = "Cannot update profile of a suspended customer";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
        verify(customerRepository).findById(customerId);
        verify(customerRepository, times(0)).save(customer);
    }

    @Test
    void shouldSuspendCustomerSuccessfully() {
        Customer customer = CustomerFixtures.customer1();
        UUID customerId = customer.getId();
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(customerRepository.save(customer)).thenReturn(customer);

        customerService.suspendCustomer(customerId);

        assertEquals(customer.getStatus().name(), Status.SUSPENDED.name());
        verify(customerRepository).findById(customerId);
        verify(customerRepository).save(customer);
    }

    @Test
    void shouldSuspendAlreadySuspendedCustomerException() {
        Customer customer = CustomerFixtures.customer1();
        customer.setStatus(Status.SUSPENDED);
        UUID customerId = customer.getId();
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> customerService.suspendCustomer(customerId));

        String expectedMessage = "Customer already suspended";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
        verify(customerRepository).findById(customerId);
        verify(customerRepository, times(0)).save(customer);
    }

    @Test
    void shouldActivateNonActiveCustomerSuccessfully() {
        Customer customer = CustomerFixtures.customer2();
        customer.setStatus(Status.INACTIVE);
        UUID customerId = customer.getId();
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(customerRepository.save(customer)).thenReturn(customer);

        customerService.activateCustomer(customerId);

        assertEquals(Status.ACTIVE.name(), customer.getStatus().name());
        verify(customerRepository).findById(customerId);
        verify(customerRepository).save(customer);
    }

    @Test
    void shouldActivateAlreadyActiveCustomerException() {
        Customer customer = CustomerFixtures.customer2();
        UUID customerId = customer.getId();
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> customerService.activateCustomer(customerId));

        String expectedMessage = "Customer already active";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
        verify(customerRepository).findById(customerId);
        verify(customerRepository, times(0)).save(customer);
    }

    @Test
    void shouldDeactivateActiveCustomerSuccessfully() {
        Customer customer = CustomerFixtures.customer1();
        UUID customerId = customer.getId();
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(customerRepository.save(customer)).thenReturn(customer);

        customerService.deactivateCustomer(customerId);

        assertEquals(Status.INACTIVE.name(), customer.getStatus().name());
        verify(customerRepository).findById(customerId);
        verify(customerRepository).save(customer);
    }

    @Test
    void shouldDeactivateInactiveCustomerException() {
        Customer customer = CustomerFixtures.customer1();
        customer.setStatus(Status.INACTIVE);
        UUID customerId = customer.getId();
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> customerService.deactivateCustomer(customerId));

        String expectedMessage = "Customer already inactive";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
        verify(customerRepository).findById(customerId);
        verify(customerRepository, times(0)).save(customer);
    }

    @Test
    void shouldDeactivateSuspendedCustomerException() {
        Customer customer = CustomerFixtures.customer1();
        customer.setStatus(Status.SUSPENDED);
        UUID customerId = customer.getId();
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> customerService.deactivateCustomer(customerId));

        String expectedMessage = "Customer already suspended";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
        verify(customerRepository).findById(customerId);
        verify(customerRepository, times(0)).save(customer);
    }

    @Test
    void shouldDeleteCustomerSuccessfully() {
        Customer customer = CustomerFixtures.customer1();
        UUID customerId = customer.getId();

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        doNothing().when(customerRepository).delete(customer);

        customerService.deleteCustomer(customerId);

        verify(customerRepository).findById(customerId);
        verify(customerRepository).delete(customer);
    }
}