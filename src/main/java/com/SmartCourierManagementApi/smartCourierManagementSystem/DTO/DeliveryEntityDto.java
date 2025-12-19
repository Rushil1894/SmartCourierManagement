package com.SmartCourierManagementApi.smartCourierManagementSystem.DTO;

import com.SmartCourierManagementApi.smartCourierManagementSystem.enumeration.DeliveryAgentStatus;

public class DeliveryEntityDto
{
    private Integer delivery_id;
    private Integer package_id;
    private Integer agent_id;
    private Integer manager_id;
    private DeliveryAgentStatus status;

    public Integer getDelivery_id() {
        return delivery_id;
    }

    public void setDelivery_id(Integer delivery_id) {
        this.delivery_id = delivery_id;
    }

    public Integer getPackage_id() {
        return package_id;
    }

    public void setPackage_id(Integer package_id) {
        this.package_id = package_id;
    }

    public Integer getAgent_id() {
        return agent_id;
    }

    public void setAgent_id(Integer agent_id) {
        this.agent_id = agent_id;
    }

    public Integer getManager_id() {
        return manager_id;
    }

    public void setManager_id(Integer manager_id) {
        this.manager_id = manager_id;
    }

    public DeliveryAgentStatus getStatus() {
        return status;
    }

    public void setStatus(DeliveryAgentStatus status) {
        this.status = status;
    }
}
