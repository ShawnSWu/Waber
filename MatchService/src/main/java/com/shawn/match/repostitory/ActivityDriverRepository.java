package com.shawn.match.repostitory;

import com.shawn.match.model.ActivityDriver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityDriverRepository extends JpaRepository<ActivityDriver, Long> {

    ActivityDriver findByActivityAndDriverId(long activityId, long driverId);

    List<ActivityDriver> findAllByActivityAndCarType(long activityId, String carType);
}
