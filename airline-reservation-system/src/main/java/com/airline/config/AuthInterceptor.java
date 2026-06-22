package com.airline.config;
import com.airline.entity.Role; import jakarta.servlet.http.*; import org.springframework.stereotype.Component; import org.springframework.web.servlet.HandlerInterceptor;
@Component
public class AuthInterceptor implements HandlerInterceptor{
 public boolean preHandle(HttpServletRequest req,HttpServletResponse res,Object handler)throws Exception{
  Object uid=req.getSession().getAttribute("userId"); Object role=req.getSession().getAttribute("role");
  if(uid==null||role==null){res.sendRedirect("/login"); return false;}
  String path=req.getRequestURI();
  if(path.startsWith("/admin") && role!=Role.ADMIN){res.sendRedirect("/login?denied");return false;}
  if(path.startsWith("/user") && role!=Role.USER){res.sendRedirect("/login?denied");return false;}
  return true;
 }
}
