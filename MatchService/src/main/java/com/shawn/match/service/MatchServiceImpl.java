package com.shawn.match.service;

import com.shawn.match.exception.ParticipateActivityException;
import com.shawn.match.model.Activity;
import com.shawn.match.model.ActivityParticipant;
import com.shawn.match.repostitory.ActivityParticipantRepository;
import com.shawn.match.repostitory.ActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MatchServiceImpl implements MatchService{

    @Autowired
    ActivityRepository activityRepository;

    @Autowired
    ActivityParticipantRepository activityParticipantRepository;

    @Override
    public void participateActivity(String activityName, long driverId) {
        Activity activity = activityRepository.findByName(activityName);
        System.out.println(activity.getName());
        if (Optional.ofNullable(activity).isPresent()) {
            if (!isAlreadyParticipate(activity, driverId)) {
                activityParticipantRepository.save(ActivityParticipant.builder()
                        .activity(activity.getId())
                        .participant(driverId)
                        .build());
            } else {
                throw new ParticipateActivityException("You have participated.");
            }
        } else {
            throw new ParticipateActivityException("Activity is not exist.");
        }
    }

    private boolean isAlreadyParticipate(Activity activity, long driverId) {
        return Optional.ofNullable(activityParticipantRepository.findByActivityAndParticipant(activity.getId(), driverId)).isPresent();
    }


}
