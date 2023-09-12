package com.sm.serversmanagement.repository;

import com.sm.serversmanagement.model.Server;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ServerRepository extends JpaRepository<Server, Long> {

    Server findServerByIpAddress(String ipAddress);

    @Query(
            "SELECT s FROM Server s WHERE s.isDeleted=:deleted AND " +
            "LOWER(s.name) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(s.ipAddress) LIKE LOWER(CONCAT('%', :search, '%'))"
    )
    List<Server> findAllServersBySearch(
            String search,
            @Param("deleted") boolean deleted
    );

    @Query(
            "SELECT s FROM Server s WHERE s.isDeleted=:deleted"
    )
    List<Server> findAllServers(
            @Param("deleted") boolean deleted
    );

}
