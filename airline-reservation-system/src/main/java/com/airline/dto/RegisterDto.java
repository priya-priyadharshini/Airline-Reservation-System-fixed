package com.airline.dto;
import jakarta.validation.constraints.*;
public class RegisterDto{
 @NotBlank public String fullName; @Email @NotBlank public String email; @Pattern(regexp="^[0-9]{10}$",message="Phone must be 10 digits") public String phone;
 @Pattern(regexp="^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&]).{8,}$",message="Password must be 8+ chars with uppercase, lowercase, number and special char") public String password;
 @NotBlank public String confirmPassword;
 public String getFullName(){return fullName;} public void setFullName(String fullName){this.fullName=fullName;} public String getEmail(){return email;} public void setEmail(String email){this.email=email;} public String getPhone(){return phone;} public void setPhone(String phone){this.phone=phone;} public String getPassword(){return password;} public void setPassword(String password){this.password=password;} public String getConfirmPassword(){return confirmPassword;} public void setConfirmPassword(String confirmPassword){this.confirmPassword=confirmPassword;}
}
