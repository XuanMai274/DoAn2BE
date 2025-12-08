package com.doan2.QuanLyDiemRenLuyen.Repository;

import com.doan2.QuanLyDiemRenLuyen.Entity.FeedbackEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackRepository extends CrudRepository<FeedbackEntity,Integer> {
    FeedbackEntity findByFeedbackId(int feedbackId);
    List<FeedbackEntity> findByStudentEntity_studentId(int studentId);
    @Query("""
        SELECT f FROM FeedbackEntity f
        WHERE f.conductFormEntity.managerAccept = :managerId
        ORDER BY f.createAt DESC
    """)
    List<FeedbackEntity> getFeedbackByManager(@Param("managerId") int managerId);

}
