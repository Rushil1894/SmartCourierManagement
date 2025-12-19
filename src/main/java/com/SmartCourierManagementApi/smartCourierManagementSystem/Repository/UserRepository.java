package com.SmartCourierManagementApi.smartCourierManagementSystem.Repository;

import com.SmartCourierManagementApi.smartCourierManagementSystem.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {
}