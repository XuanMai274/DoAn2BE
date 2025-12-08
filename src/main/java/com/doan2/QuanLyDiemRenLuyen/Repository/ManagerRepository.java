package com.doan2.QuanLyDiemRenLuyen.Repository;

import com.doan2.QuanLyDiemRenLuyen.Entity.ManagerEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ManagerRepository extends CrudRepository<ManagerEntity,Integer> {
    @Query("select s from ManagerEntity s where s.accountEntity.accountId = :accountId ")
    ManagerEntity findByAccountId(@Param("accountId") int accountId);
    ManagerEntity findByManagerId(int managerId);
}
