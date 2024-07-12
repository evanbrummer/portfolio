package com.iastate.project_matcher.controller;

import com.iastate.project_matcher.dto.LoginDTO;
import com.iastate.project_matcher.dto.UserDTO;
import com.iastate.project_matcher.entity.PreferencesEntity;
import com.iastate.project_matcher.entity.UserEntity;
import com.iastate.project_matcher.exception.UserAlreadyExistsException;
import com.iastate.project_matcher.exception.UserDoesNotExistException;
import com.iastate.project_matcher.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(value= "user")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO request) {
        try {
            String email = request.getEmail();
            UserEntity user = userService.getUser(email);

            if (user.usesSingleSignOn()) {
                // TODO: redirect to SSO
                return new ResponseEntity<>(user, new HttpHeaders(), HttpStatus.FOUND);
            } else {
                return new ResponseEntity<>("Password is required for this user.", new HttpHeaders(), HttpStatus.UNAUTHORIZED);
            }

        } catch (UserDoesNotExistException e) {
            return new ResponseEntity<>(new HttpHeaders(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(new HttpHeaders(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<UserEntity> createUser(@RequestBody UserDTO request) {
        try {
            UserEntity response = userService.createNewUser(request);
            return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.CREATED);
        } catch (UserAlreadyExistsException e) {
            return new ResponseEntity<>(new HttpHeaders(), HttpStatus.CONFLICT);
        } catch (Exception e) {
            return new ResponseEntity<>(new HttpHeaders(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/preferences/{id}")
    public ResponseEntity<UserEntity> savePreferences(@RequestBody PreferencesEntity preferences, @PathVariable int id) throws UserDoesNotExistException {
        try {
            UserEntity response = userService.saveUserPreferences(id, preferences);
            return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.CREATED);
        } catch(Exception e) {
            return new ResponseEntity<>(new HttpHeaders(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserEntity> getUserbyId(@PathVariable int id) {
        try {
            UserEntity response = userService.getUser(id);
            return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.OK);
        } catch (UserDoesNotExistException e) {
            return new ResponseEntity<>(new HttpHeaders(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(new HttpHeaders(), HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/students/active")
    public ResponseEntity<List<UserEntity>> getAllActiveStudents() {
        try {
            List<UserEntity> response = userService.getAllActiveStudents();
            return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new HttpHeaders(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<UserEntity> deleteUser(@PathVariable int id) {
        try {
            UserEntity response = userService.deleteUserEntity(id);
            return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.OK);
        } catch(Exception e) {
            return new ResponseEntity<>(new HttpHeaders(), HttpStatus.BAD_REQUEST);
        }
    }
}
