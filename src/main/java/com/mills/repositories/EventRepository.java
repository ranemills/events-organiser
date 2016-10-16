package com.mills.repositories;

import com.mills.models.Event;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends GraphRepository<Event> {
}
