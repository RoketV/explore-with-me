package com.explore.mainservice.user.jpa;

import com.explore.mainservice.user.model.User;
import com.explore.mainservice.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserPersistServiceImpl implements UserPersistService {

    private final UserRepository userRepository;

    @Override
    public Page<User> getUsers(List<Long> ids, int from, int size) {
        return userRepository.findUsersById(ids, PageRequest.of(from, size));
    }

    @Override
    @Transactional
    public User createUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    public Optional<User> findUserById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public List<User> findUsersByName(String name) {
        return userRepository.findByName(name);
    }
}
