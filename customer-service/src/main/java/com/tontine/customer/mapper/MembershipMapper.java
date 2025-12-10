package com.tontine.customer.mapper;

import com.tontine.customer.model.ApiMembershipRequest;
import com.tontine.customer.model.ApiMembershipResponse;
import com.tontine.customer.models.Membership;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MembershipMapper {

    Membership toMembership(ApiMembershipRequest membershipRequest);

    @Mapping(target = "customerId", source = "customer.id")
    ApiMembershipResponse toApiMembership(Membership membership);

    List<ApiMembershipResponse> toApiMembershipList(List<Membership> membershipList);
}
