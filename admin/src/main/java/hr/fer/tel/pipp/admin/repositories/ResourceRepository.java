package hr.fer.tel.pipp.admin.repositories;

import hr.fer.tel.pipp.admin.model.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@org.springframework.stereotype.Repository
public interface ResourceRepository extends JpaRepository<Resource, String> {

    @Query("select case when count(c)> 0 then true else false end from Resource c where c.internalId like :internalId")
    boolean existByInternalId(@Param("internalId") String internalId);

    @Query("select case when count(c)> 0 then true else false end from Resource c where c.routingKey like :routingKey")
    boolean existByRoutingKey(@Param("routingKey") String routingKey);


}
