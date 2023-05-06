package com.explore.mainservice.user.jpa;

import com.explore.mainservice.user.model.User;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface UserPersistService {

    Page<User> getUsers(List<Long> ids, int from, int size);

    User createUser(User user);

    void deleteUserById(Long id);

    Optional<User> findUserById(Long id);

    List<User> findUsersByName(String name);
}