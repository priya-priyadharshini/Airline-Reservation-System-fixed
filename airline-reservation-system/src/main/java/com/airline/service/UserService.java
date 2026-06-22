package com.airline.service;
import com.airline.dto.*; import com.airline.entity.*; import com.airline.repository.UserRepository; import org.springframework.security.crypto.password.PasswordEncoder; import org.springframework.stereotype.Service; import java.util.*;
@Service
public class UserService{
 private final UserRepository repo; private final PasswordEncoder enc;
 public UserService(UserRepository repo,PasswordEncoder enc){this.repo=repo;this.enc=enc;}
 public User register(RegisterDto d){
  if(!d.password.equals(d.confirmPassword)) throw new IllegalArgumentException("Passwords do not match");
  if(repo.findByEmailIgnoreCase(d.email).isPresent()) throw new IllegalArgumentException("Email already exists");
  User u=new User(); u.setFullName(d.fullName.trim()); u.setEmail(d.email.toLowerCase().trim()); u.setPhone(d.phone); u.setPassword(enc.encode(d.password)); u.setRole(Role.USER); u.setActive(true); return repo.save(u);
 }
 public User login(LoginDto d){
  User u=repo.findByEmailIgnoreCase(d.email).orElseThrow(()->new IllegalArgumentException("Invalid email or password"));
  if(!u.isActive()) throw new IllegalArgumentException("Account is deactivated");
  if(u.getRole()!=d.role) throw new IllegalArgumentException("Selected role does not match this account");
  if(!enc.matches(d.password,u.getPassword())) throw new IllegalArgumentException("Invalid email or password");
  return u;
 }
 public List<User> all(){return repo.findAll();} public Optional<User> byId(Long id){return repo.findById(id);} public void toggle(Long id){User u=repo.findById(id).orElseThrow(); u.setActive(!u.isActive()); repo.save(u);} public void delete(Long id){repo.deleteById(id);} public long count(){return repo.count();}
}
