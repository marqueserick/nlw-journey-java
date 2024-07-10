package com.rocketseat.planner.participant;

import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("participants")
public class ParticipantController {

  @Autowired private ParticipantRepository repository;

  @PostMapping("confirm")
  public ResponseEntity<Participant> confirmParticipant(
      @RequestParam UUID tripId, @RequestBody ParticipantDto payload) {
    Optional<Participant> participant = this.repository.findById(tripId);

    if (participant.isPresent()) {
      Participant confirmParticipant = participant.get();
      confirmParticipant.setIsConfirmed(true);
      confirmParticipant.setName(payload.name());
      return ResponseEntity.ok(repository.save(confirmParticipant));
    }

    return ResponseEntity.notFound().build();
  }
}
