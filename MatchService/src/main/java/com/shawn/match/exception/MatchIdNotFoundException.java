package com.shawn.match.exception;

public class MatchIdNotFoundException extends RuntimeException {
    public MatchIdNotFoundException(long matchId) {
        super(String.format("match id %d not found", matchId));
    }
}
