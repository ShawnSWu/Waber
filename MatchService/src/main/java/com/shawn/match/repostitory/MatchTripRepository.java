package com.shawn.match.repostitory;

import com.shawn.match.model.MatchTrip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchTripRepository extends JpaRepository<MatchTrip, Long> {

}
