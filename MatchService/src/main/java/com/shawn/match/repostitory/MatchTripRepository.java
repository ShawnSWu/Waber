package com.shawn.match.repostitory;

import com.shawn.match.model.entity.MatchTrip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface MatchTripRepository extends JpaRepository<MatchTrip, Long> {

    @Transactional
    @Modifying
    @Query("update match_trip m set m.matchStatus = ?3 where m.id = ?1 and m.passenger = ?2")
    void updateMatchedTripStatus(long matchId, long passenger, long status);

    MatchTrip findByIdAndPassenger(long matchId, long passenger);
}
