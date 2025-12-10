package com.tontine.customer.service.impl;

import com.tontine.customer.exception.CustomerNotFoundException;
import com.tontine.customer.mapper.CustomerMapper;
import com.tontine.customer.model.ApiCustomerRequest;
import com.tontine.customer.model.ApiCustomerResponse;
import com.tontine.customer.model.ApiProfile;
import com.tontine.customer.models.Customer;
import com.tontine.customer.models.utils.MaritalStatus;
import com.tontine.customer.models.utils.MemberStatus;
import com.tontine.customer.repository.CustomerRepository;
import com.tontine.customer.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    public static final String CUSTOMER_NOT_FOUND = "Customer with ID %s not found";
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
        customer = customerRepository.save(customer);
        return customerMapper.toApiCustomerResponse(customer);
    }

    @Override
    @Transactional
    public ApiCustomerResponse updateCustomer(UUID customerId, ApiCustomerRequest apiCustomerRequest) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(CUSTOMER_NOT_FOUND.formatted(customerId)));
            customerMapper.updateCustomerFromRequest(apiCustomerRequest, customer);
        customer = customerRepository.save(customer);
        return customerMapper.toApiCustomerResponse(customer);
    }

    @Override
    @Transactional
    public void updateProfile(UUID customerId, ApiProfile profile) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(CUSTOMER_NOT_FOUND.formatted(customerId)));
        customer.updateProfile(
                profile.getFirstname(),
                profile.getLastname(),
                profile.getEmail(),
                profile.getPhone(),
                MaritalStatus.valueOf(profile.getMaritalStatus().name()),
                MemberStatus.valueOf(profile.getMemberStatus().name()),
                customerMapper.toAddress(profile.getAddress())
        );
        customerRepository.save(customer);
    }

    @Override
    @Transactional
    public void suspendCustomer(UUID customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(CUSTOMER_NOT_FOUND.formatted(customerId)));
        customer.suspend();
        customerRepository.save(customer);
    }

    @Override
    @Transactional
    public void activateCustomer(UUID customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(CUSTOMER_NOT_FOUND.formatted(customerId)));
        customer.activate();
        customerRepository.save(customer);
    }

    @Override
    @Transactional
    public void deactivateCustomer(UUID customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(CUSTOMER_NOT_FOUND.formatted(customerId)));
        customer.deactivate();
        customerRepository.save(customer);
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
