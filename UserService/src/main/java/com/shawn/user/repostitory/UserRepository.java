package com.shawn.user.repostitory;

import com.shawn.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmailAndRole(String email, long role);

    User findByIdAndRole(long id, long role);

}
