package com.shawn.match.repostitory;

import com.shawn.match.model.WaitingMatchPassenger;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WaitingMatchPassengerRepository extends JpaRepository<WaitingMatchPassenger, Long> {

    WaitingMatchPassenger findByPassengerAndId(long passenger, long matchId);
}
