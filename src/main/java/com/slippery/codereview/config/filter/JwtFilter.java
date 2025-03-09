//package com.slippery.codereview.config.filter;
//
//import com.slippery.codereview.service.JwtService;
//import com.slippery.codereview.service.MyUserDetailsService;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.context.ApplicationContext;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//
//@Slf4j
//@Configuration
//public class JwtFilter extends OncePerRequestFilter {
//    private final JwtService jwtService;
//    private final ApplicationContext applicationContext;
//
//    public JwtFilter(JwtService jwtService, ApplicationContext applicationContext) {
//        this.jwtService = jwtService;
//        this.applicationContext = applicationContext;
//    }
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        String authHeader = request.getHeader("Authorization");
//        String username =null;
//        String token =null;
//        if(authHeader !=null && authHeader.startsWith("Bearer")){
//            token =authHeader.substring(7);
//            username =jwtService.extractUsernameFromToken(token);
//
//        }else{
//            log.warn("no auth header was provided for your request");
//            throw new IOException("auth header not provided");
//        }
//        if(SecurityContextHolder.getContext().getAuthentication()==null &&username !=null){
//            UserDetails userDetails =applicationContext.getBean(MyUserDetailsService.class).loadUserByUsername(username);
//            if(jwtService.validateToken(token,userDetails)){
//                UsernamePasswordAuthenticationToken authenticationToken =new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
////                continue kesho please
//            }
//        }
//    }
//}