package com.tontine.customer.service;


import com.tontine.customer.model.ApiMembershipRequest;
import com.tontine.customer.model.ApiMembershipResponse;

import java.util.List;
import java.util.UUID;

public interface MembershipService {
    List<ApiMembershipResponse> getAll();
    List<ApiMembershipResponse> getAllMembership(UUID tontineId);
    ApiMembershipResponse getMembershipById(UUID tontineId, UUID membershipId);
    ApiMembershipResponse createMembership(UUID tontineId, UUID customerId, ApiMembershipRequest membershipRequest);
    ApiMembershipResponse updateMembership(UUID tontineId, UUID membershipId, ApiMembershipRequest membershipRequest);
    void deleteMembership(UUID tontineId, UUID membershipId);
}
