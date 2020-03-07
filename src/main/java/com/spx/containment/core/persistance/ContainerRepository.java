package com.spx.containment.core.persistance;

import com.spx.containment.core.model.Container;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.neo4j.annotation.Depth;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;

//import org.springframework.data.neo4j.annotation.Query;

public interface ContainerRepository extends Neo4jRepository<Container, UUID> {


    // @Query("select c from Container c where c.name=:name")   //TODO one  of these methods needs to go
    //@Query("MATCH (c:Container {name={0}}) RETURN c")
    Optional<Container> findByName(@Param("name") String name, @Depth int depth);

    // @Query("select c from Container c where c.reference=:reference")
    Optional<Container> findByReference(@Param("reference") String reference);


    //    MATCH x=(c:Container)-[p:PARENTING*]->(a:Container)
//    WHERE
//    c.name='BC Hallway 21'
//    and
//    a.name='Manchester Factory'
//    RETURN x
    @Query("MATCH x=(c:Container)-[p:PARENTING*]->(a:Container) WHERE c.name=$child and a.name=$ancestor RETURN Count(x)")
    int countParentPaths(@Param("child") String childName, @Param("ancestor") String ancestorName);
}
