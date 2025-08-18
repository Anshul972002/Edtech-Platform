package com.example.edtech.service;


import com.example.edtech.config.UserPrincipal;
import com.example.edtech.dto.Userdto;
import com.example.edtech.entity.CourseEntity;
import com.example.edtech.entity.UserEntity;
import com.example.edtech.repository.CourseRepository;
import com.example.edtech.repository.UserRepository;
import com.example.edtech.util.CloudinaryResponse;
import com.example.edtech.util.DtoToEntity;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.token.TokenService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
   private final UserRepository repository;

private final CourseRepository courseRepository;

private final CourseService courseService;
private final PasswordEncoder passwordEncoder;
private final RefreshTokenService refreshTokenService;
private final FileUploadService fileUploadService;
private final CommentService commentService;
private final CourseProgressService courseProgressService;


public boolean save(Userdto user, CloudinaryResponse response){


try{
   Map<String, String> profile = Map.of("id", response.getId(), "url", response.getUrl());
   user.setPassword(passwordEncoder.encode(user.getPassword()));
   DtoToEntity util=new DtoToEntity();
   UserEntity entityuser = util.convertUser(user);
   entityuser.setProfile(profile);
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
      throw new RuntimeException("User not found");

   }

   public UserEntity findUserById(ObjectId id){
      UserEntity user = repository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
return user;
}

   public UserEntity getProfile(){
      Authentication auth= SecurityContextHolder.getContext().getAuthentication();
      return getUser(auth.getName());
   }


   public List<CourseEntity> getEnrolledCourses(List<ObjectId>courseIds) {
      List<CourseEntity> courses = courseRepository.findByIdIn(courseIds);

      return courses;
   }
   @Override
   public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

      UserEntity user = repository.findByEmail(username);
      return UserPrincipal.create(user);
   }


   public String  getId() {
      UserPrincipal principal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
return principal.getId();
}

   public boolean deleteUser(UserEntity user) {
      try {
         // 1. Delete Cloudinary profile image (if any)
         if (user.getProfile() != null) {
            fileUploadService.deleteFile(user.getProfile().get("id"));
         }

         // 2. Remove from enrolled courses
         courseService.deleteUser(user.getId());

         // 3. Invalidate tokens
         refreshTokenService.invalidateToken(user.getId());

         // 4. Handle comments (e.g., anonymize)
         commentService.anonymizeUserId(user.getId());

         // 5. Delete progress
         courseProgressService.deleteProgress(user.getId());

         // 6. Finally delete the user itself
         repository.delete(user);

         return true;  // success
      } catch (Exception e) {
         throw new RuntimeException("Failed to delete user with id"+user.getId());

      }
   }



   public boolean lockUser(ObjectId userId) {
   try
   {
      UserEntity user = repository.findById(userId)
              .orElseThrow(() -> new RuntimeException("User not found"));
      user.setAccountLocked(true);
      repository.save(user);
      return true;
   }
   catch (Exception e){
      System.out.println(e.getMessage());
      return  false;
   }


   }

   public boolean unlockUser(ObjectId userId) {
     try {
        UserEntity user = repository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setAccountLocked(false);
        repository.save(user);
        return false;
     }

       catch (Exception e){
         System.out.println(e.getMessage());
         return  false;
      }
   }

   public boolean disableUser(ObjectId userId) {
   try {
      UserEntity user = repository.findById(userId)
              .orElseThrow(() -> new RuntimeException("User not found"));
      user.setEnabled(false);
      repository.save(user);
      return  true;
   }

       catch (Exception e){
         System.out.println(e.getMessage());
         return  false;
      }
   }

   public boolean enableUser(ObjectId userId) {
   try {
      UserEntity user = repository.findById(userId)
              .orElseThrow(() -> new RuntimeException("User not found"));
      user.setEnabled(true);
      repository.save(user);
      return true;
   }


        catch (Exception e){
         System.out.println(e.getMessage());
         return  false;
      }
   }

}
