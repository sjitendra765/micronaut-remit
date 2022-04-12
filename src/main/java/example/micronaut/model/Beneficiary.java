package example.micronaut.model;

import io.micronaut.core.annotation.Introspected;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Introspected
public class Beneficiary {

    @NotBlank
    @NotNull
    public String mobile;

    @NotBlank
    @NotNull
    public String benename;

    @NotBlank
    @NotNull
    public String bankid;

    @NotBlank
    @NotNull
    public String accno;

    @NotBlank
    @NotNull
    public String ifscode;

    public String verified;

    public String gst_state;

    @NotBlank
    @NotNull
    public String dob;

    @NotBlank
    @NotNull
    public String address;

    @NotBlank
    @NotNull
    public String pincode;

    public Beneficiary() {
    }
}