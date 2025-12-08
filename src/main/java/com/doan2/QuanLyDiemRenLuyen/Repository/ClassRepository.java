package com.doan2.QuanLyDiemRenLuyen.Repository;

import com.doan2.QuanLyDiemRenLuyen.Entity.ClassEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClassRepository extends CrudRepository<ClassEntity,Integer> {
    @NonNull
    List<ClassEntity> findAll();
    List<ClassEntity> findByFacultyId_facultyId(int facultyId);
}
