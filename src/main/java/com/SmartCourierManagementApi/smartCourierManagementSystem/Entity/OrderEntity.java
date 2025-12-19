package com.SmartCourierManagementApi.smartCourierManagementSystem.Entity;


import com.SmartCourierManagementApi.smartCourierManagementSystem.enumeration.PackageStatus;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class OrderEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer order_id;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private UserEntity userEntity;

    private LocalDate order_date;

    private double total_weight;

    private String pickup_location;


    @OneToMany(mappedBy = "orderEntity",cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<PackageEntity> packageEntities;





    public Integer getOrder_id() {
        return order_id;
    }

    public void setOrder_id(Integer order_id) {
        this.order_id = order_id;
    }

    public UserEntity getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    public LocalDate getOrder_date() {
        return order_date;
    }

    public void setOrder_date(LocalDate order_date) {
        this.order_date = order_date;
    }

    public double getTotal_weight() {
        return total_weight;
    }

    public void setTotal_weight(double total_weight) {
        this.total_weight = total_weight;
    }

    public String getPickup_location() {
        return pickup_location;
    }

    public void setPickup_location(String pickup_location) {
        this.pickup_location = pickup_location;
    }

    public List<PackageEntity> getPackageEntities() {
        return packageEntities;
    }

    public void setPackageEntities(List<PackageEntity> packageEntities) {
        this.packageEntities = packageEntities;
    }
}
