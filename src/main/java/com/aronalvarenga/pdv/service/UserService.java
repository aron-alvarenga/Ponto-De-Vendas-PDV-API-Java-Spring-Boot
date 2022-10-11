package com.aronalvarenga.pdv.service;

import com.aronalvarenga.pdv.dto.UserDTO;
import com.aronalvarenga.pdv.entity.User;
import com.aronalvarenga.pdv.exceptions.NoItemException;
import com.aronalvarenga.pdv.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<UserDTO> findAll() {
        return userRepository.findAll().stream().map(user -> new UserDTO(user.getId(), user.getName(), user.isEnable()) ).collect(Collectors.toList());
    }

    public UserDTO save(User user) {
        userRepository.save(user);
        return new UserDTO(user.getId(), user.getName(), user.isEnable());
    }

    public UserDTO findById(long id) {
        Optional<User> optional = userRepository.findById(id);

        if(!optional.isPresent()) {
            throw new NoItemException("User not found!");
        }
        User user = optional.get();
        return new UserDTO(user.getId(), user.getName(), user.isEnable());
    }

    public UserDTO update(User user) {
        Optional<User> userToEdit = userRepository.findById(user.getId());

        if(!userToEdit.isPresent()) {
            throw new NoItemException("User not found!");
        }

        userRepository.save(user);
        return new UserDTO(user.getId(), user.getName(), user.isEnable());
    }

    public void deleteById(long id) {
        userRepository.deleteById(id);
    }
}
