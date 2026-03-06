package com.revworkforce.leave.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LeaveConfig {

    @Value("${leave.total-allocated:24}")
    private int totalAllocatedLeaves;

    public int getTotalAllocatedLeaves() {
        return totalAllocatedLeaves;
    }

    public void setTotalAllocatedLeaves(int totalAllocatedLeaves) {
        this.totalAllocatedLeaves = totalAllocatedLeaves;
    }
}
