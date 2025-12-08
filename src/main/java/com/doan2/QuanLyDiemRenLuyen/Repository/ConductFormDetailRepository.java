package com.doan2.QuanLyDiemRenLuyen.Repository;

import com.doan2.QuanLyDiemRenLuyen.Entity.ConductFormDetailEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConductFormDetailRepository extends CrudRepository<ConductFormDetailEntity,Integer> {
    ConductFormDetailEntity findByConductFormDetailId(int conductFormDetailId);
}
