package ru.practicum.explore.with.me.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.explore.with.me.model.StatViewDto;
import ru.practicum.explore.with.me.model.StatHit;

import java.time.LocalDateTime;
import java.util.List;

public interface StatRepository extends JpaRepository<StatHit, Long>, JpaSpecificationExecutor<StatHit> {

    @Query("SELECT new ru.practicum.explore.with.me.model.StatViewDto(st.app, st.uri, COUNT(st.ip)) " +
            "FROM StatHit AS st " +
            "WHERE st.timestamp > :start and st.timestamp < :end and st.uri IN :uris " +
            "GROUP BY st.app, st.uri")
    List<StatViewDto> countTotalIp(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("uris") List<String> uris);

    @Query("" +
            "SELECT new ru.practicum.explore.with.me.model.StatViewDto(st.app, st.uri, COUNT(DISTINCT st.ip)) " +
            "FROM StatHit AS st " +
            "WHERE st.timestamp > :start and st.timestamp < :end and st.uri IN :uris " +
            "GROUP BY st.app, st.uri")
    List<StatViewDto> countTotalIpDistinct(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("uris") List<String> uris);

}
