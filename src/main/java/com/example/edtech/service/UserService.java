package com.example.edtech.service;


import com.example.edtech.dto.Userdto;
import com.example.edtech.entity.UserEntity;
import com.example.edtech.repository.UserRepository;
import com.example.edtech.util.DtoToEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Objects;

@Service
public class UserService implements UserDetailsService {
@Autowired
   private UserRepository repository;

@Autowired
private PasswordEncoder passwordEncoder;
public boolean save(Userdto user){


try{
   user.setPassword(passwordEncoder.encode(user.getPassword()));
   DtoToEntity util=new DtoToEntity();
   UserEntity entityuser = util.convertUser(user);
   entityuser.setRole("ROLE_USER");
   entityuser.setCreatedAt(LocalDateTime.now());
   Objects.requireNonNull(repository.save(entityuser));
   return true;
}
catch (Exception exception){
   return  false;
}
}
   public boolean findemailexist(String email) {
      UserEntity byEmail = repository.findByEmail(email);
      if (byEmail != null)
         return true;
      else
         return false;
   }

   public UserEntity getUser(String email) {
      UserEntity user = repository.findByEmail(email);
      if (user!=null)
         return user;
      throw new RuntimeException("USesr not found");

   }

   public UserEntity getProfile(){
      Authentication auth= SecurityContextHolder.getContext().getAuthentication();
      return getUser(auth.getName());
   }

   @Override
   public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

      UserEntity user = repository.findByEmail(username);
      return new User(user.getEmail(),user.getPassword(), Collections.singletonList(new SimpleGrantedAuthority(user.getRole())));
   }
}
