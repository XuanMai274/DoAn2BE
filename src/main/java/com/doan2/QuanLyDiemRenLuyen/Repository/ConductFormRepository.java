package com.doan2.QuanLyDiemRenLuyen.Repository;

import com.doan2.QuanLyDiemRenLuyen.DTO.ClassAverageScoreDTO;
import com.doan2.QuanLyDiemRenLuyen.Entity.ConductFormEntity;
import com.doan2.QuanLyDiemRenLuyen.Entity.StudentEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConductFormRepository extends CrudRepository<ConductFormEntity,Integer > {
    @Query("SELECT DISTINCT c FROM ConductFormEntity c WHERE c.studentEntity.studentId = :studentId")
    List<ConductFormEntity> findByStudentEntity_StudentId(@Param("studentId") int studentId);
    ConductFormEntity findByConductFormId(int conductFormId);
    @Query("SELECT cf " +
            "FROM ConductFormEntity cf " +
            "JOIN FETCH cf.studentEntity s " +
            "JOIN FETCH s.classId c " +
            "JOIN FETCH cf.semesterEntity sem " +
            "WHERE c.classId = :classId AND sem.semesterId = :semesterId")
    List<ConductFormEntity> findWithStudentAndClassByClassAndSemester(
            @Param("classId") int classId,
            @Param("semesterId") int semesterId
    );
    ConductFormEntity findByStudentEntity_studentId(int studentId);
    ConductFormEntity findTopByStudentEntity_StudentIdOrderByCreateAtDesc(int studentId);
    List<ConductFormEntity> findByStudentEntity_studentIdOrderBySemesterEntity_semesterId(int studentId);
    @Query("SELECT c FROM ConductFormEntity c WHERE c.studentEntity.studentId = :studentId")
    List<ConductFormEntity> findByStudentId(int studentId);

    @Query("SELECT c FROM ConductFormEntity c WHERE c.semesterEntity.semesterId = :semesterId")
    List<ConductFormEntity> findBySemesterId(int semesterId);

    // tính điểm trung bình theo lớp
    @Query("SELECT new com.doan2.QuanLyDiemRenLuyen.DTO.ClassAverageScoreDTO(" +
            "c.classId, c.className, AVG(cf.staff_score)) " +
            "FROM ConductFormEntity cf " +
            "JOIN cf.studentEntity s " +
            "JOIN s.classId c " +
            "WHERE c.facultyId.facultyId = :facultyId " +
            "GROUP BY c.classId, c.className " +
            "ORDER BY c.className ASC")
    List<ClassAverageScoreDTO> findAverageStaffScoreByFaculty(@Param("facultyId") int facultyId);

}
