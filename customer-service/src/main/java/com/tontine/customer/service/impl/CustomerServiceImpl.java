package com.tontine.customer.service.impl;

import com.tontine.customer.exception.CustomerNotFoundException;
import com.tontine.customer.mapper.CustomerMapper;
import com.tontine.customer.model.*;
import com.tontine.customer.models.Customer;
import com.tontine.customer.models.utils.MaritalStatus;
import com.tontine.customer.models.utils.Status;
import com.tontine.customer.repository.CustomerRepository;
import com.tontine.customer.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private static final String CUSTOMER_NOT_FOUND = "Customer with ID %s not found";
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;


    @Override
    public List<ApiCustomerResponse> getCustomers() {
        List<Customer> customers = customerRepository.findAll();
        return customerMapper.toApiCustomerResponses(customers);
    }

    @Override
    public ApiCustomerResponse getCustomerById(UUID customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(CUSTOMER_NOT_FOUND.formatted(customerId)));
        return customerMapper.toApiCustomerResponse(customer);
    }

    @Override
    @Transactional
    public ApiCustomerResponse createCustomer(ApiCustomerRequest apiCustomerRequest) {
        Customer customer = customerMapper.toCustomer(apiCustomerRequest);
        customer.setCreatedAt(LocalDateTime.now());
        customer.setUpdatedAt(LocalDateTime.now());
        customer = customerRepository.save(customer);
        return customerMapper.toApiCustomerResponse(customer);
    }

    @Override
    @Transactional
    public ApiCustomerResponse updateCustomer(UUID customerId, ApiCustomerRequest apiCustomerRequest) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(CUSTOMER_NOT_FOUND.formatted(customerId)));
            customerMapper.updateCustomerFromRequest(apiCustomerRequest, customer);
            customer.setUpdatedAt(LocalDateTime.now());
            customer = customerRepository.save(customer);
        return customerMapper.toApiCustomerResponse(customer);
    }

    @Override
    @Transactional
    public ApiCustomerResponse updateProfile(UUID customerId, ApiProfile profile) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(CUSTOMER_NOT_FOUND.formatted(customerId)));
        customer.updateProfile(
                profile.getFirstname(),
                profile.getLastname(),
                profile.getEmail(),
                profile.getPhone(),
                MaritalStatus.valueOf(profile.getMaritalStatus().name()),
                Status.valueOf(profile.getStatus().name()),
                customerMapper.toAddress(profile.getAddress()));
        customer.setUpdatedAt(LocalDateTime.now());
        customerRepository.save(customer);
        return customerMapper.toApiCustomerResponse(customer);
    }

    @Override
    @Transactional
    public ApiStatusResponse suspendCustomer(UUID customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(CUSTOMER_NOT_FOUND.formatted(customerId)));
        customer.suspend();
        customer.setUpdatedAt(LocalDateTime.now());
        customerRepository.save(customer);
        return new ApiStatusResponse().id(customerId)
                .status(ApiStatus.valueOf(customer.getStatus().name()));
    }

    @Override
    @Transactional
    public ApiStatusResponse activateCustomer(UUID customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(CUSTOMER_NOT_FOUND.formatted(customerId)));
        customer.activate();
        customer.setUpdatedAt(LocalDateTime.now());
        customerRepository.save(customer);
        return new ApiStatusResponse().id(customerId)
                .status(ApiStatus.valueOf(customer.getStatus().name()));
    }

    @Override
    @Transactional
    public ApiStatusResponse deactivateCustomer(UUID customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(CUSTOMER_NOT_FOUND.formatted(customerId)));
        customer.deactivate();
        customer.setUpdatedAt(LocalDateTime.now());
        customerRepository.save(customer);
        return new ApiStatusResponse().id(customerId)
                .status(ApiStatus.valueOf(customer.getStatus().name()));
    }

    @Override
    @Transactional
    public void deleteCustomer(UUID customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(
                        CUSTOMER_NOT_FOUND.formatted(customerId)));
        customerRepository.delete(customer);
    }
}