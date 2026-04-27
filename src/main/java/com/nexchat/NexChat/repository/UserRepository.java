package com.nexchat.NexChat.repository;

import com.nexchat.NexChat.modal.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);
//
//    @Modifying
//    @Query("UPDATE User u SET u.tempUsername =:tempUsername where u.id = :id")
//    int updateUserById(@Param("id") Long id, @Param("tempUsername") String tempUsername);

}
