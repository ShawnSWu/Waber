package com.shawn.user.repostitory;

import com.shawn.user.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmailAndRole(String email, long role);

    Optional<User> findByIdAndRole(long id, long role);

    Optional<User> findByEmail(String email);

}
