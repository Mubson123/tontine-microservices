package com.tontine.customer.service;

import com.tontine.customer.exception.MembershipNotFoundException;
import com.tontine.customer.fixtures.MembershipFixtures;
import com.tontine.customer.mapper.MembershipMapper;
import com.tontine.customer.model.ApiMembershipRequest;
import com.tontine.customer.model.ApiMembershipResponse;
import com.tontine.customer.models.Customer;
import com.tontine.customer.models.Membership;
import com.tontine.customer.repository.CustomerRepository;
import com.tontine.customer.repository.MembershipRepository;
import com.tontine.customer.service.impl.MembershipServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MembershipServiceTest {

    @Mock
    private MembershipRepository membershipRepository;
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private MembershipMapper membershipMapper;
    @InjectMocks
    private MembershipServiceImpl membershipService;

    @Test
    void shouldReturnAllMembershipSuccessfully() {
        List<Membership> membershipList = MembershipFixtures.membershipListAll;
        List<ApiMembershipResponse> expected = MembershipFixtures.membershipResponseListAll;
        when(membershipRepository.findAll()).thenReturn(membershipList);
        when(membershipMapper.toApiMembershipList(membershipList)).thenReturn(expected);

        List<ApiMembershipResponse> actual = membershipService.getAll();

        assertNotNull(actual);
        assertEquals(3,actual.size());
        assertEquals(expected, actual);
        verify(membershipRepository).findAll();
        verify(membershipMapper).toApiMembershipList(membershipList);
    }

    @Test
    void shouldReturnAllMembershipByTontineIdSuccessfully() {
        List<Membership> membershipListTontine = MembershipFixtures.membershipListTontine1;
        List<ApiMembershipResponse> expected =
                MembershipFixtures.membershipResponseListTontine1;
        UUID tontineId1 = MembershipFixtures.tontineId1;
        when(membershipRepository.findByTontineId(tontineId1)).thenReturn(membershipListTontine);
        when(membershipMapper.toApiMembershipList(membershipListTontine)).thenReturn(expected);

        List<ApiMembershipResponse> actual = membershipService.getAllMembership(tontineId1);

        assertNotNull(actual);
        assertEquals(2,actual.size());
        assertEquals(expected, actual);
        verify(membershipRepository).findByTontineId(tontineId1);
        verify(membershipMapper).toApiMembershipList(membershipListTontine);
    }

    @Test
    void shouldReturnMembershipByIdSuccessfully() {
        UUID tontineId1 = MembershipFixtures.tontineId1;
        UUID membershipId1 = MembershipFixtures.membershipId1;
        Membership membership = MembershipFixtures.membership2();
        ApiMembershipResponse expected = MembershipFixtures.membershipResponse2();

        when(membershipRepository.findByIdAndTontineId(membershipId1, tontineId1))
                .thenReturn(Optional.of(membership));
        when(membershipMapper.toApiMembership(membership)).thenReturn(expected);

        ApiMembershipResponse actual = membershipService.getMembershipById(tontineId1, membershipId1);

        assertNotNull(actual);
        assertEquals(expected, actual);
        verify(membershipRepository).findByIdAndTontineId(membershipId1, tontineId1);
        verify(membershipMapper).toApiMembership(membership);
    }

    @Test
    void shouldReturnMembershipByIdNotFound() {
        UUID tontineId1 = MembershipFixtures.tontineId1;
        UUID membershipId1 = UUID.randomUUID();
        Membership membership = MembershipFixtures.membership2();

        when(membershipRepository.findByIdAndTontineId(membershipId1, tontineId1))
                .thenReturn(Optional.empty());

        Exception exception = assertThrows(MembershipNotFoundException.class,
                () -> membershipService.getMembershipById(tontineId1, membershipId1));

        String expectedMessage = "Membership with ID %s not found".formatted(membershipId1);
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
        verify(membershipRepository).findByIdAndTontineId(membershipId1, tontineId1);
        verify(membershipMapper, times(0)).toApiMembership(membership);
    }

    @Test
    void shouldCreateMembershipSuccessfully() {
        Customer customer = MembershipFixtures.customer2;
        UUID customerId = customer.getId();
        UUID tontineId1 = MembershipFixtures.tontineId1;
        ApiMembershipRequest membershipRequest = MembershipFixtures.membershipRequest2();

        Membership membership = MembershipFixtures.membership2();
        ApiMembershipResponse expected = MembershipFixtures.membershipResponse2();

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(membershipMapper.toMembership(membershipRequest)).thenReturn(membership);
        when(membershipRepository.save(membership)).thenReturn(membership);
        when(membershipMapper.toApiMembership(membership)).thenReturn(expected);

        ApiMembershipResponse actual = membershipService.createMembership(tontineId1, customerId, membershipRequest);

        assertNotNull(actual);
        assertEquals(expected, actual);
        verify(customerRepository).findById(customerId);
        verify(membershipMapper).toMembership(membershipRequest);
        verify(membershipRepository).save(membership);
        verify(membershipMapper).toApiMembership(membership);
    }

    @Test
    void shouldCreateMembershipNegativeRotationException() {
        Customer customer = MembershipFixtures.customer2;
        UUID customerId = customer.getId();
        UUID tontineId1 = MembershipFixtures.tontineId1;
        ApiMembershipRequest membershipRequest = MembershipFixtures.membershipRequest2();
        membershipRequest.setPositionInRotation(0);

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> membershipService.createMembership(tontineId1, customerId, membershipRequest));

        String expectedMessage = "Position in rotation must be a positive integer";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
        verify(customerRepository).findById(customerId);
        verify(membershipMapper, times(0)).toMembership(membershipRequest);
    }

    @Test
    void shouldUpdateMembershipSuccessfully() {
        UUID tontineId2 = MembershipFixtures.tontineId2;
        ApiMembershipRequest membershipRequest = MembershipFixtures.membershipRequest12();

        Membership membership = MembershipFixtures.membership12();
        UUID membershipId = membership.getId();
        ApiMembershipResponse expected = MembershipFixtures.membershipResponse12();

        when(membershipRepository.findByIdAndTontineId(membershipId, tontineId2)).thenReturn(Optional.of(membership));
        when(membershipRepository.save(membership)).thenReturn(membership);
        when(membershipMapper.toApiMembership(membership)).thenReturn(expected);

        ApiMembershipResponse actual = membershipService.updateMembership(tontineId2, membershipId, membershipRequest);

        assertNotNull(actual);
        assertEquals(expected, actual);
        verify(membershipRepository).findByIdAndTontineId(membershipId, tontineId2);
        verify(membershipRepository).save(membership);
        verify(membershipMapper).toApiMembership(membership);
    }

    @Test
    void shouldDeleteMembershipSuccessfully() {
        UUID tontineId2 = MembershipFixtures.tontineId2;
        Membership membership = MembershipFixtures.membership12();
        UUID membershipId = membership.getId();

        when(membershipRepository.findByIdAndTontineId(membershipId, tontineId2)).thenReturn(Optional.of(membership));
        doNothing().when(membershipRepository).delete(membership);

        membershipService.deleteMembership(tontineId2, membershipId);

        verify(membershipRepository).findByIdAndTontineId(membershipId, tontineId2);
        verify(membershipRepository).delete(membership);
    }
}