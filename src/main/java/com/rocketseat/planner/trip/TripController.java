package com.rocketseat.planner.trip;

import com.rocketseat.planner.participant.ParticipantService;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("trip")
public class TripController {

  @Autowired private ParticipantService participantService;

  @Autowired private TripRepository repository;

  @PostMapping
  public ResponseEntity<TripDtoResponse> createTrip(@RequestBody TripDto tripPayload) {
    Trip trip = new Trip(tripPayload);
    repository.save(trip);
    participantService.registerParticipantsToEvent(tripPayload.emailsToInvite(), trip.getId());
    return ResponseEntity.ok(new TripDtoResponse(trip.getId()));
  }

  @GetMapping("{tripId}")
  public ResponseEntity<Trip> getTrip(@PathVariable("tripId") UUID tripId) {
    Optional<Trip> trip = repository.findById(tripId);
    return trip.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
  }
}
