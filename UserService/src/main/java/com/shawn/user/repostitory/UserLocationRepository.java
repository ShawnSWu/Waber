package com.shawn.user.repostitory;

import com.shawn.user.model.entity.UserLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserLocationRepository extends JpaRepository<UserLocation, Long> {

    UserLocation findFirstByUserIdOrderByIdDesc(long userId);
}
