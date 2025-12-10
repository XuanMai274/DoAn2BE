package com.doan2.QuanLyDiemRenLuyen.Repository;

import com.doan2.QuanLyDiemRenLuyen.Entity.CriteriaTypeEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CriteriaTypeRepository extends CrudRepository<CriteriaTypeEntity,Integer> {
    @NonNull
    @Query("select c from CriteriaTypeEntity c where c.isActive = true order by c.criteriaTypeId")
    List<CriteriaTypeEntity> findByIsActiveTrue();
    CriteriaTypeEntity findByCriteriaTypeId(int criteriaTypeId);
    @NonNull
    List<CriteriaTypeEntity> findAll();
    //lấy CriteriaType nhưng lọc chỉ những Type có Criteria xuất hiện trong ConductFormDetail.
    @Query("""
        SELECT DISTINCT ct  FROM CriteriaTypeEntity ct
        JOIN ct.criteriaEntityList c
        JOIN c.conductFormDetailEntityList d
        WHERE d.conductFormEntity.conductFormId = :conductFormId
        ORDER BY ct.criteriaTypeId
    """)
    List<CriteriaTypeEntity> findCriteriaTypesUsedInConductForm(@Param("conductFormId") int conductFormId);

}
