package com.tontine.customer.service.impl;

import com.tontine.customer.exception.CustomerNotFoundException;
import com.tontine.customer.exception.MembershipNotFoundException;
import com.tontine.customer.mapper.MembershipMapper;
import com.tontine.customer.model.ApiMembershipRequest;
import com.tontine.customer.model.ApiMembershipResponse;
import com.tontine.customer.models.Customer;
import com.tontine.customer.models.Membership;
import com.tontine.customer.models.utils.MemberRole;
import com.tontine.customer.models.utils.MemberStatus;
import com.tontine.customer.repository.CustomerRepository;
import com.tontine.customer.repository.MembershipRepository;
import com.tontine.customer.service.MembershipService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MembershipServiceImpl implements MembershipService {
    private static final String MEMBERSHIP_NOT_FOUND = "Membership with ID %s not found";
    private final MembershipRepository membershipRepository;
    private final CustomerRepository customerRepository;
    private final MembershipMapper membershipMapper;

    @Override
    public List<ApiMembershipResponse> getAll() {
        List<Membership> membershipList = membershipRepository.findAll();
        return membershipMapper.toApiMembershipList(membershipList);
    }

    @Override
    public List<ApiMembershipResponse> getAllMembership(UUID tontineId) {
        List<Membership> membershipList = membershipRepository
                .findByTontineId(tontineId);
        return membershipMapper.toApiMembershipList(membershipList);
    }

    @Override
    public ApiMembershipResponse getMembershipById(UUID tontineId, UUID membershipId) {
        Membership membership = membershipRepository
                .findByIdAndTontineId(membershipId, tontineId)
                .orElseThrow(() -> new MembershipNotFoundException(
                        MEMBERSHIP_NOT_FOUND.formatted(membershipId)));
        return membershipMapper.toApiMembership(membership);
    }

    @Override
    @Transactional
    public ApiMembershipResponse createMembership(UUID tontineId, UUID customerId, ApiMembershipRequest membershipRequest) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer with ID %s not found".formatted(customerId)));

        Membership membership = membershipMapper.toMembership(membershipRequest);
        membership.setCustomer(customer);
        membership.setTontineId(tontineId);
        membership.setJoinedAt(LocalDate.now());
        membership.setCreatedAt(LocalDateTime.now());
        membership.setUpdatedAt(LocalDateTime.now());
        membership.setMemberStatus(MemberStatus.ACTIVE);
        membership = membershipRepository.save(membership);
        return membershipMapper.toApiMembership(membership);
    }

    @Override
    @Transactional
    public ApiMembershipResponse updateMembership(UUID tontineId, UUID membershipId, ApiMembershipRequest membershipRequest) {
        Membership membership = membershipRepository
                .findByIdAndTontineId(membershipId, tontineId)
                .orElseThrow(() -> new MembershipNotFoundException(
                        MEMBERSHIP_NOT_FOUND.formatted(membershipId)));
        membership.assignRole(MemberRole.valueOf(membershipRequest.getMemberRole().name()));
        membership.changeRotationPosition(membership.getPositionInRotation());
        membership.setUpdatedAt(LocalDateTime.now());
        membership = membershipRepository.save(membership);
        return membershipMapper.toApiMembership(membership);
    }

    @Override
    @Transactional
    public void deleteMembership(UUID tontineId, UUID membershipId) {
        Membership membership = membershipRepository
                .findByIdAndTontineId(membershipId, tontineId)
                .orElseThrow(() -> new MembershipNotFoundException(
                        MEMBERSHIP_NOT_FOUND.formatted(membershipId)));
        membershipRepository.delete(membership);
    }
}
