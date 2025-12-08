package com.doan2.QuanLyDiemRenLuyen.Repository;

import com.doan2.QuanLyDiemRenLuyen.Entity.CriteriaEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.lang.NonNullApi;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CriteriaRepository extends CrudRepository<CriteriaEntity,Integer> {
    @NonNull
    @Query("select c from CriteriaEntity c order by c.criteriaId")
    List<CriteriaEntity> findAll();

    CriteriaEntity findByCriteriaId(int criteriaId);

    @Query("select c from CriteriaEntity c where c.criteriaTypeEntity.criteriaTypeId = :criteriaTypeId order by c.criteriaId")
    List<CriteriaEntity> findByCriteriaTypeId(@Param("criteriaTypeId") int criteriaTypeId);
//    List<CriteriaEntity> findAll();
//    CriteriaEntity findByCriteriaId(int criteriaId);
//    @Query("select c from CriteriaEntity c where c.criteriaTypeEntity.criteriaTypeId = :criteriaTypeId")
//    List<CriteriaEntity> findByCriteriaTypeId(@Param("criteriaTypeId") int criteriaTypeId);
}
