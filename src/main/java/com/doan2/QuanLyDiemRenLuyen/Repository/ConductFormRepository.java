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
    // lay tat ca cac phieu ren luyen cua tat ca cac hoc ki theo lop( để so sánh điểm ca nhân với lớp tại dashbroad)
    @Query("""
        SELECT cf
        FROM ConductFormEntity cf
        JOIN cf.studentEntity s
        JOIN s.classId c
        WHERE cf.semesterEntity.semesterId = :semesterId
          AND c.classId = :classId
    """)
    List<ConductFormEntity> findBySemesterAndClass(
            @Param("semesterId") int semesterId,
            @Param("classId") int classId
    );
    // tính điểm trung bình theo lớp
//    @Query("SELECT new com.doan2.QuanLyDiemRenLuyen.DTO.ClassAverageScoreDTO(" +
//            "c.classId, c.className, AVG(cf.staff_score)) " +
//            "FROM ConductFormEntity cf " +
//            "JOIN cf.studentEntity s " +
//            "JOIN s.classId c " +
//            "WHERE c.facultyId.facultyId = :facultyId " +
//            "AND cf.staff_score > 0 " +
//            "GROUP BY c.classId, c.className " +
//            "ORDER BY c.className ASC")
//    List<ClassAverageScoreDTO> findAverageStaffScoreByFaculty(@Param("facultyId") int facultyId);
    // Tính điểm trung bình theo lớp theo khoa + học kỳ
    @Query("SELECT new com.doan2.QuanLyDiemRenLuyen.DTO.ClassAverageScoreDTO(" +
            "c.classId, c.className, AVG(cf.staff_score)) " +
            "FROM ConductFormEntity cf " +
            "JOIN cf.studentEntity s " +
            "JOIN s.classId c " +
            "WHERE c.facultyId.facultyId = :facultyId " +
            "AND cf.semesterEntity.semesterId = :semesterId " +
            "AND cf.staff_score > 0 " +
            "GROUP BY c.classId, c.className " +
            "ORDER BY c.className ASC")
    List<ClassAverageScoreDTO> findAverageStaffScoreByFacultyAndSemester(
            @Param("facultyId") int facultyId,
            @Param("semesterId") int semesterId
    );

}
