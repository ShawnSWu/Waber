package com.shawn.match.repostitory;

import com.shawn.match.model.entity.ActivityDriver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityDriverRepository extends JpaRepository<ActivityDriver, Long> {

    ActivityDriver findByActivityIdAndDriverId(long activityId, long driverId);

    List<ActivityDriver> findAllByActivityIdAndCarTypeId(long activityId, long carTypeId);
}
