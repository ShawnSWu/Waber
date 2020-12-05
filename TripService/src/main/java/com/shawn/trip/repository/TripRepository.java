package com.shawn.trip.repository;

import com.shawn.trip.model.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface TripRepository extends JpaRepository<Trip, Long> {

    @Transactional
    @Modifying
    @Query("update trip t set t.tripStatus = ?2 where t.id = ?1")
    void updateTripStatus(long tripId, long status);

}
