package com.airline.dto;
import com.airline.entity.Role; import jakarta.validation.constraints.*;
public class LoginDto{ @Email @NotBlank public String email; @NotBlank public String password; @NotNull public Role role; public String getEmail(){return email;} public void setEmail(String email){this.email=email;} public String getPassword(){return password;} public void setPassword(String password){this.password=password;} public Role getRole(){return role;} public void setRole(Role role){this.role=role;} }
