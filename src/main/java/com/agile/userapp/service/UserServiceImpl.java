package com.agile.userapp.service;

import com.agile.userapp.dto.LocationTreeDTO;
import com.agile.userapp.entity.Location;
import com.agile.userapp.entity.User;
import com.agile.userapp.exception.UserAlreadyExistsException;
import com.agile.userapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Page<User> findAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new NoSuchElementException("User not found with username: " + username));
    }

    @Override
    public User createUser(User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new UserAlreadyExistsException("Username already exists: " + user.getUsername());
        }
        if (userRepository.findAll().stream().anyMatch(existingUser -> existingUser.getEmail().equals(user.getEmail()))) {
            throw new IllegalArgumentException("Email already exists: " + user.getEmail());
        }
        return userRepository.save(user);
    }

    @Override
    public User updateUser(String username, User userDetails) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NoSuchElementException("User not found with username: " + username));

        user.setName(userDetails.getName());
        user.setEmail(userDetails.getEmail());
        user.setGender(userDetails.getGender());
        user.setPicture(userDetails.getPicture());
        user.setLocation(userDetails.getLocation());
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(String username) {
        if (!userRepository.findByUsername(username).isPresent()) {
            throw new NoSuchElementException("User not found with username: " + username);
        }
        userRepository.deleteByUsername(username);
    }

    @Override
    public List<User> generateRandomUsers(int number) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://randomuser.me/api/?results=" + number;
        Map<String, Object> response = restTemplate.getForObject(url, Map.class);
        List<Map<String, Object>> results = (List<Map<String, Object>>) response.get("results");

        List<User> users = results.stream().map(result -> {
            User user = new User();
            user.setUsername((String) ((Map<String, Object>) result.get("login")).get("username"));
            user.setName((String) ((Map<String, Object>) result.get("name")).get("first"));
            user.setEmail((String) result.get("email"));
            user.setGender((String) result.get("gender"));
            user.setPicture((String) ((Map<String, Object>) result.get("picture")).get("large"));

            Location location = new Location();
            Map<String, Object> locationData = (Map<String, Object>) result.get("location");
            location.setCountry((String) locationData.get("country"));
            location.setState((String) locationData.get("state"));
            location.setCity((String) locationData.get("city"));
            user.setLocation(location);
            return user;
        }).collect(Collectors.toList());

        return userRepository.saveAll(users);
    }

    @Override
    public List<LocationTreeDTO> getUsersTree() {
        List<User> users = userRepository.findAll();

        Map<String, Map<String, Map<String, List<User>>>> groupedData = users.stream()
                .collect(Collectors.groupingBy(
                        user -> user.getLocation().getCountry(),
                        Collectors.groupingBy(
                                user -> user.getLocation().getState(),
                                Collectors.groupingBy(
                                        user -> user.getLocation().getCity()
                                )
                        )
                ));

        List<LocationTreeDTO> tree = new ArrayList<>();
        for (Map.Entry<String, Map<String, Map<String, List<User>>>> countryEntry : groupedData.entrySet()) {
            LocationTreeDTO countryNode = new LocationTreeDTO(countryEntry.getKey());

            for (Map.Entry<String, Map<String, List<User>>> stateEntry : countryEntry.getValue().entrySet()) {
                LocationTreeDTO stateNode = new LocationTreeDTO(stateEntry.getKey());

                for (Map.Entry<String, List<User>> cityEntry : stateEntry.getValue().entrySet()) {
                    LocationTreeDTO cityNode = new LocationTreeDTO(cityEntry.getKey());

                    for (User user : cityEntry.getValue()) {
                        cityNode.addChild(new LocationTreeDTO(user.getName()));
                    }
                    stateNode.addChild(cityNode);
                }
                countryNode.addChild(stateNode);
            }
            tree.add(countryNode);
        }
        return tree;
    }
}
