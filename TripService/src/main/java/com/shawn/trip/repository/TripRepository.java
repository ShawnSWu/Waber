package com.shawn.trip.repository;

import com.shawn.trip.model.entity.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
public interface TripRepository extends JpaRepository<Trip, Long> {

    @Transactional
    @Modifying
    @Query("update trip t set t.tripStatus = ?2, t.tripDistance=?3, t.destinationLatitude=?4, t.destinationLongitude=?5 where t.id = ?1")
    void updateTripStatus(long tripId, long status, long distance, double destinationLatitude, double destinationLongitude);

    Optional<Trip> findByIdAndMatchId(long id, long matchId);

}
