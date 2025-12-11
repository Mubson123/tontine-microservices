package com.tontine.customer.constraint;

import com.tontine.customer.fixtures.CustomerFixtures;
import com.tontine.customer.fixtures.MembershipFixtures;
import com.tontine.customer.models.Customer;
import com.tontine.customer.models.Membership;
import com.tontine.customer.models.utils.Address;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static com.tontine.customer.constance.Constance.REQUIRED;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class ConstraintViolationTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void shouldDetectConstraintViolationsOnCustomerFields() {
        Customer wrongCustomer = CustomerFixtures.wrongCustomer();

        Set<ConstraintViolation<Customer>> violations = validator.validate(wrongCustomer);
        assertThat(violations)
                .hasSize(4)
                .extracting(ConstraintViolation::getMessage)
                .containsExactlyInAnyOrder(
                        "title required",
                        "firstname " + REQUIRED,
                        "birthdate required",
                        "email should be valid"
                );
    }

    @Test
    void shouldDetectConstraintViolationsOnAddressFields() {
        Address wrongAddress = CustomerFixtures.wrongAddress();

        Set<ConstraintViolation<Address>> violations = validator.validate(wrongAddress);
        assertThat(violations)
                .hasSize(5)
                .extracting(ConstraintViolation::getMessage)
                .containsExactlyInAnyOrder(
                        "street " + REQUIRED,
                        "city " + REQUIRED,
                        "state " + REQUIRED,
                        "zipCode must be a 5-digit number",
                        "country " + REQUIRED
                );
    }

    @Test
    void shouldDetectConstraintViolationsOnMembershipFields() {
        Membership wrongMembership = MembershipFixtures.wrongMembership();

        Set<ConstraintViolation<Membership>> violations = validator.validate(wrongMembership);
        assertThat(violations)
                .hasSize(2)
                .extracting(ConstraintViolation::getMessage)
                .containsExactlyInAnyOrder(
                        "customer required",
                        "member status required"
                );
    }
}
