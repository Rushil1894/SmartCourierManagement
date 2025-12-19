package com.SmartCourierManagementApi.smartCourierManagementSystem.DTO;

import lombok.Data;


public class BulkAssignmentDto
{
    private Integer packageId;
    private Integer agentId;

    public Integer getPackageId() {
        return packageId;
    }

    public void setPackageId(Integer packageId) {
        this.packageId = packageId;
    }

    public Integer getAgentId() {
        return agentId;
    }

    public void setAgentId(Integer agentId) {
        this.agentId = agentId;
    }
}

