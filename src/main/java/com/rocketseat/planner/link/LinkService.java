package com.rocketseat.planner.link;

import com.rocketseat.planner.trip.Trip;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LinkService {

  @Autowired private LinkRepository repository;

  public LinkDtoResponse registerLink(LinkDtoRequest payload, Trip trip) {
    Link newLink = new Link(payload.title(), payload.url(), trip);

    this.repository.save(newLink);

    return new LinkDtoResponse(newLink.getId());
  }

  public List<LinkDto> getAllLinksByTrip(UUID tripId) {
    return this.repository.findByTripId(tripId).stream()
        .map(link -> new LinkDto(link.getId(), link.getTitle(), link.getUrl()))
        .toList();
  }
}
