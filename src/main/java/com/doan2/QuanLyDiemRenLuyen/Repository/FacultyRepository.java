package com.doan2.QuanLyDiemRenLuyen.Repository;

import com.doan2.QuanLyDiemRenLuyen.Entity.FacultyEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FacultyRepository extends CrudRepository<FacultyEntity,Integer> {
    @NonNull
    List<FacultyEntity>  findAll();
    FacultyEntity findByFacultyId(int facultyId);
}
