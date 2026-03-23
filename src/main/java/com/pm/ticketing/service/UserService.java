package com.pm.ticketing.service;
import com.pm.ticketing.dto.CreateUserRequest;
import com.pm.ticketing.dto.UserResponse;
import com.pm.ticketing.exception.ResourceNotFoundException;
import com.pm.ticketing.model.User;
import com.pm.ticketing.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponse createUser(CreateUserRequest request){
        User user = new User(request.name(), request.email(), request.initialBalance());
        User saved =  userRepository.save(user);
        return toResponse(saved);
    }

    public User findUserOrThrow(Long id){
        return userRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("User not found with id: " + id));
    }

    public UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(), user.getName(), user.getEmail(), user.getWalletBalance()
        );
    }
}
