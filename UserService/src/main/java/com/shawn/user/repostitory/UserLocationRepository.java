package com.shawn.user.repostitory;

import com.shawn.user.model.entity.UserLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserLocationRepository extends JpaRepository<UserLocation, Long> {

    Optional<UserLocation> findFirstByUserIdOrderByIdDesc(long userId);
}
