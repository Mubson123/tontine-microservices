package com.tontine.customer.constance;

public class Constance {
    private Constance() {
        throw new IllegalStateException(Constance.class.getName());
    }

    public static final String REGEX = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
    public static final String REQUIRED = "required or must have at least 3 characters";
}
