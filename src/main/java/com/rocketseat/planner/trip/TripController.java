package com.rocketseat.planner.trip;

import com.rocketseat.planner.participant.Participant;
import com.rocketseat.planner.participant.ParticipantByTrip;
import com.rocketseat.planner.participant.ParticipantService;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
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
    participantService.registerParticipantsToEvent(tripPayload.emailsToInvite(), trip);
    return ResponseEntity.ok(new TripDtoResponse(trip.getId()));
  }

  @GetMapping("{tripId}")
  public ResponseEntity<Trip> getTrip(@PathVariable("tripId") UUID tripId) {
    Optional<Trip> trip = repository.findById(tripId);
    return trip.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
  }

  @PutMapping("{tripId}")
  public ResponseEntity<Trip> updateTrip(
      @PathVariable("tripId") UUID tripId, @RequestBody TripDto tripPayload) {
    Optional<Trip> trip = repository.findById(tripId);
    if (trip.isPresent()) {
      Trip updatedTrip = trip.get();
      updatedTrip.setEndsAt(
          LocalDateTime.parse(tripPayload.endsAt(), DateTimeFormatter.ISO_DATE_TIME));
      updatedTrip.setStartsAt(
          LocalDateTime.parse(tripPayload.startsAt(), DateTimeFormatter.ISO_DATE_TIME));
      updatedTrip.setDestination(tripPayload.destination());
      return ResponseEntity.ok(repository.save(updatedTrip));
    }
    return ResponseEntity.notFound().build();
  }

  @GetMapping("confirm")
  public ResponseEntity<Trip> confirmTrip(@RequestParam("tripId") UUID tripId) {
    Optional<Trip> trip = repository.findById(tripId);
    if (trip.isPresent()) {
      Trip updatedTrip = trip.get();
      updatedTrip.setIsConfirmed(Boolean.TRUE);
      participantService.triggerConfirmationEmail(tripId);
      return ResponseEntity.ok(repository.save(updatedTrip));
    }
    return ResponseEntity.notFound().build();
  }

  @PostMapping("invite")
  public ResponseEntity<List<Participant>> inviteToTrip(
      @RequestParam("tripId") UUID tripId, @RequestBody List<String> emailsToInvite) {
    Optional<Trip> trip = repository.findById(tripId);
    if (trip.isPresent()) {
      Trip tripToInvite = trip.get();
      List<Participant> participants =
          participantService.registerParticipantsToEvent(emailsToInvite, tripToInvite);
      if (tripToInvite.getIsConfirmed())
        participantService.triggerConfirmationEmail(tripId, emailsToInvite);

      return ResponseEntity.ok(participants);
    }

    return ResponseEntity.notFound().build();
  }

  @GetMapping("{tripId}/participants")
  public ResponseEntity<List<ParticipantByTrip>> getParticipants(
      @PathVariable("tripId") UUID tripId) {
    List<Participant> participants = participantService.getParticipantsByTrip(tripId);
    return ResponseEntity.ok(
        participants.stream()
            .map(
                participant ->
                    new ParticipantByTrip(
                        participant.getId(),
                        participant.getName(),
                        participant.getEmail(),
                        participant.getIsConfirmed()))
            .toList());
  }
}
