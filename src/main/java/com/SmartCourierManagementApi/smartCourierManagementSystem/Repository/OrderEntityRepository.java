package com.SmartCourierManagementApi.smartCourierManagementSystem.Repository;

import com.SmartCourierManagementApi.smartCourierManagementSystem.Entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderEntityRepository extends JpaRepository<OrderEntity, Integer>
{

}