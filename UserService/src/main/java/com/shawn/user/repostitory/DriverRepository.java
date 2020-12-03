package com.shawn.user.repostitory;

import com.shawn.user.model.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {

    Driver findByEmail(String email);

    Driver findById(long id);
}
