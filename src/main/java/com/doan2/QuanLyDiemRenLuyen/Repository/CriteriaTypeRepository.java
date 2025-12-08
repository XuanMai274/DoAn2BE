package com.doan2.QuanLyDiemRenLuyen.Repository;

import com.doan2.QuanLyDiemRenLuyen.Entity.CriteriaTypeEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CriteriaTypeRepository extends CrudRepository<CriteriaTypeEntity,Integer> {
    @NonNull
    @Query("select c from CriteriaTypeEntity c where c.isActive = true order by c.criteriaTypeId")
    List<CriteriaTypeEntity> findByIsActiveTrue();
    CriteriaTypeEntity findByCriteriaTypeId(int criteriaTypeId);
}
