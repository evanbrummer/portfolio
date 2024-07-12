package com.iastate.project_matcher.service;

import com.iastate.project_matcher.constants.Major;
import com.iastate.project_matcher.constants.UserType;
import com.iastate.project_matcher.dto.UserDTO;
import com.iastate.project_matcher.entity.PreferencesEntity;
import com.iastate.project_matcher.entity.UserEntity;
import com.iastate.project_matcher.exception.UserAlreadyExistsException;
import com.iastate.project_matcher.exception.UserDoesNotExistException;
import com.iastate.project_matcher.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserEntity createNewUser(UserDTO request) throws UserAlreadyExistsException {
        UserEntity result = userDTOtoUserEntity(request);
        if (result.hasUserType(UserType.Student)) {
            PreferencesEntity preferencesEntity = new PreferencesEntity();
            result.setPreferencesEntity(preferencesEntity);
        }
        if (userRepository.existsCurrentUserByUsername(result.getUsername())) {
            throw new UserAlreadyExistsException("User already exists");
        }
        userRepository.save(result);
        return result;
    }

    public UserEntity getUser(int id) throws UserDoesNotExistException {
        UserEntity result = userRepository.getReferenceById((id));
        if (result == null) {
            throw new UserDoesNotExistException("User does not exist");
        }
        return result;
    }

    public UserEntity getUser(String email) throws UserDoesNotExistException {
        UserEntity probe = new UserEntity();
        probe.setEmail(email);

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnoreCase()
                .withIgnorePaths("id");

        Example<UserEntity> example = Example.of(probe, matcher);

        List<UserEntity> userEntities = getAllUsers();

        for (UserEntity user : userEntities) {
            if (user.getEmail().equals(email)) {
                return user;
            }
        }
        return userRepository.findOne(example)
                .orElseThrow(() -> new UserDoesNotExistException("Email " + email + " not found"));
    }

    public List<UserEntity> getAllActiveStudents() {
        List<UserEntity> response = userRepository.findAll();
        for(int x = 0; x < response.size(); x++) {
            if (response.get(x).hasUserType(UserType.Instructor) || !response.get(x).getIsActive()) {
                response.remove(x);
                x = -1;
            }
        }
        return response;
    }

    public List<UserEntity> getAllUsers() {
        List<UserEntity> response = userRepository.findAll();
        return response;
    }

    public UserEntity saveUserPreferences(int id, PreferencesEntity preferences) throws UserDoesNotExistException {
       UserEntity user = getUser(id);
       user.setPreferencesEntity(preferences);
       user.setHasSubmitted(true);
       userRepository.save(user);
       return user;
    }

    private UserEntity userDTOtoUserEntity(UserDTO request) {
        UserEntity userEntity = new UserEntity();
        userEntity.setFirstName(request.getFirstName());
        userEntity.setLastName(request.getLastName());
        userEntity.setUserType(request.getUserType());
        userEntity.setEmail(request.getEmail());
        userEntity.setUsername(request.getUsername());
        userEntity.setIsActive(request.getIsActive());
        userEntity.setMajor(Major.valueOf(request.getMajor()));
        return userEntity;
    }

    public UserEntity deleteUserEntity(int id) {
        UserEntity user = userRepository.getReferenceById(id);
        userRepository.delete(user);
        return user;
    }

    public void saveUserFromDto(UserDTO userDto) {
        UserEntity userEntity = new UserEntity();
        
        userEntity.setFirstName(userDto.getFirstName());
        userEntity.setLastName(userDto.getLastName());
        userEntity.setEmail(userDto.getEmail());
        userEntity.setUsername(userDto.getUsername());
        userEntity.setMajor(Major.valueOf(userDto.getMajor()));
        userEntity.setIsActive(userDto.getIsActive());
        userEntity.setUserType(userDto.getUserType());
        // Save the UserEntity to the database
        userRepository.save(userEntity);
    }

}
