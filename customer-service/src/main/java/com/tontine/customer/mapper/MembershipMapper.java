package com.tontine.customer.mapper;

import com.tontine.customer.model.ApiMembership;
import com.tontine.customer.models.Membership;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface MembershipMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "customer", ignore = true)
    Membership toMembership(ApiMembership apiMembership);

    @Mapping(target = "customerId", source = "customer.id")
    ApiMembership toApiMembership(Membership membership);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "customer", ignore = true)
    void updateMembershipFromApi(ApiMembership apiMembership, @MappingTarget Membership membership);
}
