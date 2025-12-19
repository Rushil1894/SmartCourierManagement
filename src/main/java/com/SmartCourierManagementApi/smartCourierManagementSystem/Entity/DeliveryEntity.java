package com.SmartCourierManagementApi.smartCourierManagementSystem.Entity;


import com.SmartCourierManagementApi.smartCourierManagementSystem.enumeration.DeliveryAgentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer delivery_id;

    @OneToOne
    @JoinColumn(name = "package_id")
    private PackageEntity packageEntity;

    @ManyToOne
    @JoinColumn(name = "agent_id")
    private UserEntity deliveryAgent;

    @ManyToOne
    @JoinColumn(name = "manager_id")
    private UserEntity assignedByManager;

    @Enumerated(EnumType.STRING)
    private DeliveryAgentStatus status;






    public Integer getDelivery_id() {
        return delivery_id;
    }

    public void setDelivery_id(Integer delivery_id) {
        this.delivery_id = delivery_id;
    }

    public PackageEntity getPackageEntity() {
        return packageEntity;
    }

    public void setPackageEntity(PackageEntity packageEntity) {
        this.packageEntity = packageEntity;
    }

    public UserEntity getDeliveryAgent() {
        return deliveryAgent;
    }

    public void setDeliveryAgent(UserEntity deliveryAgent) {
        this.deliveryAgent = deliveryAgent;
    }

    public UserEntity getAssignedByManager() {
        return assignedByManager;
    }

    public void setAssignedByManager(UserEntity assignedByManager) {
        this.assignedByManager = assignedByManager;
    }

    public DeliveryAgentStatus getStatus() {
        return status;
    }

    public void setStatus(DeliveryAgentStatus status) {
        this.status = status;
    }
}
