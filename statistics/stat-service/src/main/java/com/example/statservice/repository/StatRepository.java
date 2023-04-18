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

    @Query("select new com.example.statservice.model.ViewStats(" +
            "eph.app, eph.uri, count(eph.ip))" +
            "from EndPointHit as eph " +
            "where eph.timestamp between :start and :end " +
            "and ((:uris is null) or (eph.uri in (:uris)))" +
            "group by eph.app, eph.uri " +
            "order by count(eph.ip) desc")
    List<ViewStats> getViewStatsByTimeAndUris(@Param("start") LocalDateTime start,
                                              @Param("end") LocalDateTime end,
                                              @Param("uris") List<String> uris);

    @Query(value = "select new com.example.statservice.model.ViewStats(" +
            "eph.app, eph.uri, count(distinct eph.ip))" +
            "from EndPointHit eph " +
            "where eph.timestamp between :start and :end " +
            "and ((:uris is null) or (eph.uri in (:uris)))" +
            "group by eph.ip, eph.app " +
            "order by count(distinct eph.ip) desc ")
    List<ViewStats> getViewStatsByTimeAndUrisUnique(@Param("start") LocalDateTime start,
                                                    @Param("end") LocalDateTime end,
                                                    @Param("uris") List<String> uris);
}
