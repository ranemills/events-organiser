package com.mills.repositories;

import com.mills.models.InvitedRelationship;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvitationRepository extends GraphRepository<InvitedRelationship> {

    @Query("MATCH (e:Event)<-[r:INVITED]->(p:Person) WHERE id(e)={0} AND id(p)={1}  return r")
    public InvitedRelationship getResponse(Long eventId, Long personId);

}
