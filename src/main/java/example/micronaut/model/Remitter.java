package example.micronaut.model;

import io.micronaut.core.annotation.Introspected;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Introspected
public class Remitter {

    @NotBlank
    @NotNull
    public String mobile;

    @NotBlank
    @NotNull
    public String firstname;

    @NotBlank
    @NotNull
    public String lastname;

    @NotBlank
    @NotNull
    public String address;

    @NotBlank
    @NotNull
    public String otp;

    public String pincode;

    public String stateresp;

    public String bank3_flag;

    @NotBlank
    @NotNull
    public String dob;

    @NotBlank
    @NotNull
    public String gst_state;

    public Remitter() {
    }
}