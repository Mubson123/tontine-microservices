package com.tontine.customer.fixtures;

import com.tontine.customer.model.ApiMemberRole;
import com.tontine.customer.model.ApiStatus;
import com.tontine.customer.model.ApiMembershipRequest;
import com.tontine.customer.model.ApiMembershipResponse;
import com.tontine.customer.models.Membership;
import com.tontine.customer.models.utils.MemberRole;
import com.tontine.customer.models.utils.Status;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class MembershipFixtures {
    private MembershipFixtures() {
        throw new IllegalArgumentException(MembershipFixtures.class.getName());
    }
    public static final UUID membershipId1 = UUID.randomUUID();
    public static final UUID membershipId2 = UUID.randomUUID();
    public static final UUID customerId1 = UUID.randomUUID();
    public static final UUID customerId2 = UUID.randomUUID();
    public static final UUID tontineId1 = UUID.randomUUID();
    public static final UUID tontineId2 = UUID.randomUUID();


    public static Membership membership11() {
        return Membership.builder()
                .id(membershipId1)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .customerId(customerId1)
                .tontineId(tontineId1)
                .joinedAt(LocalDate.now())
                .memberRole(MemberRole.MEMBER)
                .positionInRotation(3)
                .status(Status.ACTIVE)
                .build();
    }

    public static ApiMembershipResponse membershipResponse11() {
        return new ApiMembershipResponse()
                .id(membershipId1)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .customerId(customerId1)
                .tontineId(tontineId1)
                .joinedAt(LocalDate.now())
                .memberRole(ApiMemberRole.MEMBER)
                .positionInRotation(3)
                .status(ApiStatus.ACTIVE);
    }

    public static ApiMembershipRequest membershipRequest12() {
        return new ApiMembershipRequest()
                .memberRole(ApiMemberRole.SECRETARY)
                .positionInRotation(6);
    }

    public static Membership membership12() {
        return Membership.builder()
                .id(membershipId1)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .customerId(customerId1)
                .tontineId(tontineId2)
                .joinedAt(LocalDate.now())
                .memberRole(MemberRole.SECRETARY)
                .positionInRotation(6)
                .status(Status.ACTIVE)
                .build();
    }

    public static ApiMembershipResponse membershipResponse12() {
        return new ApiMembershipResponse()
                .id(membershipId1)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .customerId(customerId1)
                .tontineId(tontineId2)
                .joinedAt(LocalDate.now())
                .memberRole(ApiMemberRole.SECRETARY)
                .positionInRotation(6)
                .status(ApiStatus.ACTIVE);
    }

    public static ApiMembershipRequest membershipRequest2() {
        return new ApiMembershipRequest()
                .memberRole(ApiMemberRole.MEMBER)
                .positionInRotation(1);
    }

    public static Membership membership2() {
        return Membership.builder()
                .id(membershipId2)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .customerId(customerId2)
                .tontineId(tontineId1)
                .joinedAt(LocalDate.now())
                .memberRole(MemberRole.MEMBER)
                .positionInRotation(1)
                .status(Status.ACTIVE)
                .build();
    }

    public static ApiMembershipResponse membershipResponse2() {
        return new ApiMembershipResponse()
                .id(membershipId2)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .customerId(customerId2)
                .tontineId(tontineId1)
                .joinedAt(LocalDate.now())
                .memberRole(ApiMemberRole.MEMBER)
                .positionInRotation(1)
                .status(ApiStatus.ACTIVE);
    }

    public static List<Membership> membershipListTontine1 = List.of(membership11(), membership2());
    public static List<ApiMembershipResponse> membershipResponseListTontine1 =
            List.of(membershipResponse11(), membershipResponse2());

    public static List<Membership> membershipListAll = List.of(membership11(), membership2(), membership12());
    public static List<ApiMembershipResponse> membershipResponseListAll =
            List.of(membershipResponse11(), membershipResponse2(), membershipResponse12());

    public static Membership wrongMembership() {
        return Membership.builder()
                .id(membershipId2)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                // customer missing
                .tontineId(tontineId1)
                .joinedAt(LocalDate.now())
                .memberRole(MemberRole.MEMBER)
                .positionInRotation(0) // 0 is not permit
                // member status missing
                .build();
    }
}
