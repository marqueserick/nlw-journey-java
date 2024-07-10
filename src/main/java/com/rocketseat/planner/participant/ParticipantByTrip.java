package com.rocketseat.planner.participant;

import java.util.UUID;

public record ParticipantByTrip(UUID id, String name, String email, boolean isConfirmed) {}
