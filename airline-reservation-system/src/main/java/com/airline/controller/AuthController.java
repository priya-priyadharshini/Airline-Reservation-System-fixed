package com.airline.controller;
import com.airline.dto.*; import com.airline.entity.*; import com.airline.service.UserService; import jakarta.servlet.http.*; import jakarta.validation.Valid; import org.springframework.stereotype.Controller; import org.springframework.ui.Model; import org.springframework.validation.BindingResult; import org.springframework.web.bind.annotation.*;
@Controller
public class AuthController{
 private final UserService users; public AuthController(UserService users){this.users=users;}
 @GetMapping("/") public String index(){return "index";} @GetMapping("/login") public String login(Model m){m.addAttribute("login",new LoginDto()); return "auth/login";}
 @PostMapping("/login") public String doLogin(@Valid @ModelAttribute("login") LoginDto dto, BindingResult br, HttpSession session, Model m){ if(br.hasErrors())return "auth/login"; try{User u=users.login(dto); session.setAttribute("userId",u.getId()); session.setAttribute("role",u.getRole()); session.setAttribute("name",u.getFullName()); return u.getRole()==Role.ADMIN?"redirect:/admin/dashboard":"redirect:/user/dashboard";}catch(Exception e){m.addAttribute("error",e.getMessage());return "auth/login";} }
 @GetMapping("/register") public String register(Model m){m.addAttribute("user",new RegisterDto());return "auth/register";}
 @PostMapping("/register") public String save(@Valid @ModelAttribute("user") RegisterDto dto, BindingResult br, Model m){ if(br.hasErrors())return "auth/register"; try{users.register(dto); return "redirect:/login?registered";}catch(Exception e){m.addAttribute("error",e.getMessage());return "auth/register";} }
 @GetMapping("/logout") public String logout(HttpSession s){s.invalidate(); return "redirect:/login?logout";}
}
