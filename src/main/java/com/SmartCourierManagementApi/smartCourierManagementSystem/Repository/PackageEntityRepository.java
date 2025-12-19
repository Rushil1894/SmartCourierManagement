package com.SmartCourierManagementApi.smartCourierManagementSystem.Repository;

import com.SmartCourierManagementApi.smartCourierManagementSystem.Entity.PackageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PackageEntityRepository extends JpaRepository<PackageEntity, Integer>
{

}