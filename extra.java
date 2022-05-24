package it.unibz.src.extra;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class Extra {
    private final int code;
    private final double unitaryPrice;

    public Extra(@JsonProperty("code") int code, @JsonProperty("unitaryPrice") double unitaryPrice) {
        this.code = code;
        this.unitaryPrice = unitaryPrice;
    }

    public int getCode() {
        return code;
    }

    public double getUnitaryPrice() {
        return unitaryPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Extra)) return false;
        Extra extra = (Extra) o;
        return getCode() == extra.getCode();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCode());
    }
}
