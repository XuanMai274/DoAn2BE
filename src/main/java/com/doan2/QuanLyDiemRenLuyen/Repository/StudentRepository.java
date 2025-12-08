package com.doan2.QuanLyDiemRenLuyen.Repository;

import com.doan2.QuanLyDiemRenLuyen.Entity.StudentEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends CrudRepository<StudentEntity,Integer> {
    @Query("select s from StudentEntity s where s.accountEntity.accountId = :accountId ")
    StudentEntity findByAccountId(@Param("accountId") int accountId);
    @Query("select s from StudentEntity s where s.accountEntity.username = :username ")
    StudentEntity findByUserName(@Param("username") String username);
    StudentEntity findByStudentId(int studentId);
    //lấy lên danh sách sinh viên đã thực hiện phiếu rèn luyện theo lớp và theo học kì
    @Query("""
            SELECT s\s
            FROM StudentEntity s\s
            WHERE s.classId.classId = :classId\s
              AND EXISTS (
                   SELECT cf\s
                   FROM ConductFormEntity cf\s
                   WHERE cf.studentEntity.studentId = s.studentId\s
                     AND cf.semesterEntity.semesterId = :semesterId
              )
       \s""")
    List<StudentEntity> findStudentsSubmittedConductForm(
            @Param("semesterId") int semesterId,
            @Param("classId") int classId

    );

    //lấy lên danh sách sinh viên chưa thực hiện phiếu rèn luyện theo lớp và theo học kì
    @Query("""
        SELECT s\s
        FROM StudentEntity s\s
        WHERE s.classId.classId = :classId\s
          AND NOT EXISTS (
               SELECT cf\s
               FROM ConductFormEntity cf\s
               WHERE cf.studentEntity.studentId = s.studentId\s
                 AND cf.semesterEntity.semesterId = :semesterId
          )
   \s""")
    List<StudentEntity> findStudentsNotSubmittedConductForm(
            @Param("semesterId") int semesterId,
            @Param("classId") int classId
    );
}
