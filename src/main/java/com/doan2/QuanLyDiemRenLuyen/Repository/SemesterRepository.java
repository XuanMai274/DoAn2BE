package com.doan2.QuanLyDiemRenLuyen.Repository;

import com.doan2.QuanLyDiemRenLuyen.Entity.SemesterEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SemesterRepository extends CrudRepository<SemesterEntity,Integer> {
    @NonNull
    List<SemesterEntity> findAll();
    // cách 1:
    @Query("SELECT s FROM SemesterEntity s " +
            "WHERE s.isOpen = true " +
            "AND :now BETWEEN s.evaluationStartDate AND s.evaluationEndDate")
    List<SemesterEntity> findOpenSemestersWithinEvaluationPeriod(@Param("now") LocalDate now);
    // cách 2:
    //List<SemesterEntity> findByIsOpenTrueAndEvaluationStartDateBeforeAndEvaluationEndDateAfter(LocalDateTime now1, LocalDateTime now2);
    SemesterEntity findBySemesterId(int semesterId);
    @Query("SELECT s FROM SemesterEntity s WHERE s.evaluationStartDate IS NOT NULL AND s.evaluationEndDate IS NOT NULL AND s.isOpen = false")
    List<SemesterEntity> findSemesterOpened();
    @Query("SELECT s FROM SemesterEntity s WHERE s.evaluationStartDate IS  NULL AND s.evaluationEndDate IS  NULL AND s.isOpen = false")
    List<SemesterEntity> availableSemesters();



}
