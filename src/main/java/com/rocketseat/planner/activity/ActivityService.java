package com.rocketseat.planner.activity;

import com.rocketseat.planner.trip.Trip;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ActivityService {

  @Autowired private ActivityRepository repository;

  public ActivityDtoResponse registerActivity(ActivityDtoRequest payload, Trip trip) {
    Activity newActivity = new Activity(payload.title(), payload.occursAt(), trip);

    this.repository.save(newActivity);

    return new ActivityDtoResponse(newActivity.getId());
  }

  public List<ActivityDto> getAllActivitiesByTrip(UUID tripId) {
    return this.repository.findByTripId(tripId).stream()
        .map(
            activity ->
                new ActivityDto(activity.getId(), activity.getTitle(), activity.getOccursAt()))
        .toList();
  }
}
