package com.doan2.QuanLyDiemRenLuyen.Repository;

import com.doan2.QuanLyDiemRenLuyen.Entity.FacultySemesterEntity;
import com.doan2.QuanLyDiemRenLuyen.Entity.SemesterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface FacultySemesterRepository extends JpaRepository<FacultySemesterEntity, Integer> {
    FacultySemesterEntity findByFacultyFacultyIdAndSemesterSemesterId(int facultyId, int semesterId);
    // danh sách học kì đang mở
    @Query("""
            SELECT fs FROM FacultySemesterEntity fs
            WHERE fs.faculty.facultyId = :facultyId
            AND fs.isOpen = TRUE
            AND :now BETWEEN fs.evaluationStartDate AND fs.evaluationEndDate
            """)
    List<FacultySemesterEntity> findOpenSemestersWithinEvaluationPeriod(
            @Param("facultyId") int facultyId,
            @Param("now") LocalDate now);
    // danh sách học kì đã mở
    @Query("""
            SELECT fs FROM FacultySemesterEntity fs
            WHERE fs.faculty.facultyId = :facultyId
            AND fs.isOpen = FALSE
            """)
    List<FacultySemesterEntity> findSemesterOpened(@Param("facultyId") int facultyId);
    // Học kỳ chưa từng mở cho khoa này
    @Query("""
            SELECT s FROM SemesterEntity s
            WHERE s.semesterId NOT IN (
                SELECT fs.semester.semesterId
                FROM FacultySemesterEntity fs
                WHERE fs.faculty.facultyId = :facultyId
            )
            """)
    List<SemesterEntity> findAvailableSemesters(@Param("facultyId") int facultyId);

}
