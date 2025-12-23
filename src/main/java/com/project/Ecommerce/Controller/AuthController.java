package com.project.Ecommerce.Controller;

import com.project.Ecommerce.Models.AppRole;
import com.project.Ecommerce.Models.Role;
import com.project.Ecommerce.Models.User;
import com.project.Ecommerce.Repo.Role_Repo;
import com.project.Ecommerce.Repo.UserRepository;
import com.project.Ecommerce.Security.jwt.JwtUtils;
import com.project.Ecommerce.Security.jwt.LoginRequest;
import com.project.Ecommerce.Security.jwt.LoginResponse;
import com.project.Ecommerce.Security.jwt.ServiceRequest;
import com.project.Ecommerce.SecurityService.UserDetail;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import org.springframework.security.core.AuthenticationException;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth/")
public class AuthController {
    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userrepo;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private Role_Repo  rolerepo;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication;
        try {
            authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        } catch (AuthenticationException exception) {
            Map<String, Object> map = new HashMap<>();
            map.put("message", "Bad credentials");
            map.put("status", false);
            return new ResponseEntity<Object>(map, HttpStatus.NOT_FOUND);
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetail userDetails = (UserDetail) authentication.getPrincipal();

      //  String jwtToken = jwtUtils.generateTokenFromUsername(userDetails.getUsername());

        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

//       LoginResponse response = new LoginResponse (userDetails.getId(),
//                userDetails.getUsername(), roles,jwtToken);
//      return ResponseEntity.ok(response);

        LoginResponse response = new LoginResponse (userDetails.getId(),
                userDetails.getUsername(), roles);

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE,jwtCookie.toString()).body(response);
        }



        @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody ServiceRequest req){

            if(userrepo.existsByUserName(req.getUserName())){
                return ResponseEntity.badRequest().body("Username already taken");
            }


            if(userrepo.existsByEmail(req.getEmail())){
                return ResponseEntity.badRequest().body("Email already registered");
            }



            User user=new User(req.getUserName(), req.getEmail(), encoder.encode(req.getPassword()));

        Set<String> reqrole=req.getRole();
        Set<Role>roles =new HashSet<>();


            if(reqrole==null){
              Role userrole= rolerepo.findByRoleName(AppRole.ROLE_USER)
                      .orElseThrow(()->new RuntimeException("Error : Role is null"));
              roles.add(userrole);
          }
          else {

              reqrole.forEach(role->{
                  Role userrole;
                  switch (role){
                      case "admin":
                          userrole= rolerepo.findByRoleName(AppRole.ROLE_ADMIN)
                                  .orElseThrow(() -> new RuntimeException("AdminRole not found"));
                          roles.add(userrole);
                          break;
                      case "seller":
                           userrole= rolerepo.findByRoleName(AppRole.ROLE_SELLER)
                                  .orElseThrow(() -> new RuntimeException("SellerRole not found"));
                          roles.add(userrole);
                          break;
                      default:
                           userrole= rolerepo.findByRoleName(AppRole.ROLE_USER)
                                  .orElseThrow(() -> new RuntimeException("Role not found"));
                          roles.add(userrole);
                  }
              });
          }
          user.setRoles(roles);
          userrepo.save(user);
          return ResponseEntity.ok("User should be signup Sucessfuly " );
        }


         @GetMapping("/user")
    public String GetUserName(Authentication auth){

            if(auth!=null)return auth.getName();
            return "Null";
    }


    @GetMapping("/userdetail")
    public ResponseEntity<?>Userinfo(Authentication auth){
        UserDetail userdetail = (UserDetail) auth.getPrincipal();
        List<String>roles=userdetail.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        LoginResponse response = new LoginResponse (userdetail.getId(),
                userdetail.getUsername(), roles);

        return ResponseEntity.ok().body(response);
    }


    @GetMapping("/signout")
    public ResponseEntity<?> Signout(){
        ResponseCookie cookie=jwtUtils.removeJwtCookie();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE,cookie.toString()).body("You are SignOut");
    }



}
