package com.jardin.api.repositories;

import com.jardin.api.model.entities.Garment;
import com.jardin.api.model.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    @Query("select u from User u where u.username = :username")
    User getUserByUsername(@Param("username") String username);

    @Modifying
    @Query(nativeQuery = true, value = "update \"user\" set online = :online where id = :id")
    @Transactional
    void updateOnlineStatus(@Param("online") boolean online, @Param("id") Long id);

    @Modifying
    @Query(nativeQuery = true, value = "update \"user\" set sessiontoken = :sessionToken where id = :id")
    @Transactional
    void updateSessionToken(@Param("sessionToken") String sessionToken, @Param("id") Long id);
}
