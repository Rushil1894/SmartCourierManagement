package com.SmartCourierManagementApi.smartCourierManagementSystem.Repository;

import com.SmartCourierManagementApi.smartCourierManagementSystem.Entity.DeliveryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeliveryEntityRepository extends JpaRepository<DeliveryEntity, Integer>
{

    @Query(value = "select * from delivery_entity where agent_id = :id",nativeQuery = true)
    List<DeliveryEntity> findAllByAgentId(@Param("id") Integer id);

    @Query(value = "select * from delivery_entity where manager_id = :id",nativeQuery = true)
    List<DeliveryEntity> findAllByManagerId(Integer id);

    @Query(value = "select * from delivery_entity where agent_id = :aid AND package_id = :pid",nativeQuery = true)
    DeliveryEntity findByAgentIdAndPackageId(@Param("aid") Integer aid,@Param("pid") Integer pid);


    List<DeliveryEntity> findByStatusNot(String status);


}