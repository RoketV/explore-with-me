package com.explore.mainservice.user.repository;

import com.explore.mainservice.user.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = "FROM User u WHERE u.id in :ids")
    Page<User> findUsersById(@Param("ids") List<Long> ids, Pageable pageable);

    List<User> findByName(String name);
}