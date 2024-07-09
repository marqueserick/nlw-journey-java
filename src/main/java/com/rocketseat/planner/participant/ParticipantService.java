package com.rocketseat.planner.participant;

import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class ParticipantService {

  public void registerParticipantsToEvent(List<String> participants, UUID tripId) {}

  public void triggerConfirmationEmail(UUID tripId) {}
}
