package com.rocketseat.planner.participant;

import com.rocketseat.planner.trip.Trip;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ParticipantService {

  @Autowired private ParticipantRepository repository;

  public List<Participant> registerParticipantsToEvent(List<String> participants, Trip trip) {
    return repository.saveAll(
        participants.stream().map(email -> new Participant(email, trip)).toList());
  }

  public void triggerConfirmationEmail(UUID tripId) {}

  public void triggerConfirmationEmail(UUID tripId, List<String> emailsToInvite) {}

  public List<Participant> getParticipantsByTrip(UUID tripId) {
    return repository.findByTripId(tripId);
  }
}
