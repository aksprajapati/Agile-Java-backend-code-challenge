package com.agile.userapp.service;


import com.agile.userapp.dto.LocationTreeDTO;
import com.agile.userapp.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {

    public List<User> getAllUsers();

    public Page<User> findAllUsers(Pageable pageable);

    public User getUserByUsername(String username);

    public User createUser(User user);

    public User updateUser(String username, User userDetails);

    public void deleteUser(String username);

    public List<User> generateRandomUsers(int number);

    public List<LocationTreeDTO> getUsersTree();
}

