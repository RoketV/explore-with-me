package com.example.statservice.repository;

import com.example.statservice.model.EndPointHit;
import com.example.statservice.model.ViewStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatRepository extends JpaRepository<EndPointHit, Long> {

    @Query(value = "select new ViewStats(" +
            "eph.app as app, eph.uri as uri, count(eph.ip) as hits)" +
            "from EndPointHit eph " +
            "where eph.timestamp between :start and :end " +
            "and  eph.uri in :uris " +
            "group by eph.ip, eph.app " +
            "order by hits desc ")
    List<ViewStats> getViewStatsByTimeAndUris(@Param("start") LocalDateTime start,
                                              @Param("end") LocalDateTime end,
                                              @Param("uris") List<String> uris);
}
