package com.shawn.user.repostitory;

import com.shawn.user.model.UserLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserLocationRepository extends JpaRepository<UserLocation, Long> {

    UserLocation findFirstByUserIdOrderByIdDesc(long userId);

    List<UserLocation> findAllByUserId(long userId);


}
