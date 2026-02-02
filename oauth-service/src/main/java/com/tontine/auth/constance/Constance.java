package com.tontine.auth.constance;

public class Constance {
    private Constance() {
        throw new IllegalStateException(Constance.class.getName());
    }

    public static final String REGEX = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
}
