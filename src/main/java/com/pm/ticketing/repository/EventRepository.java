package com.pm.ticketing.repository;

import com.pm.ticketing.model.Event;
import org.springframework.data.repository.CrudRepository;

public interface EventRepository extends CrudRepository<Event,Long> {
}
