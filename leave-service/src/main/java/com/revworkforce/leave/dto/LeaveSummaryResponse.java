package com.revworkforce.leave.dto;

public class LeaveSummaryResponse {

    private int totalAllocated;
    private int used;
    private int pending;
    private int remaining;

    // Constructors
    public LeaveSummaryResponse() {
    }

    public LeaveSummaryResponse(int totalAllocated, int used, int pending, int remaining) {
        this.totalAllocated = totalAllocated;
        this.used = used;
        this.pending = pending;
        this.remaining = remaining;
    }

    // Getters and Setters
    public int getTotalAllocated() {
        return totalAllocated;
    }

    public void setTotalAllocated(int totalAllocated) {
        this.totalAllocated = totalAllocated;
    }

    public int getUsed() {
        return used;
    }

    public void setUsed(int used) {
        this.used = used;
    }

    public int getPending() {
        return pending;
    }

    public void setPending(int pending) {
        this.pending = pending;
    }

    public int getRemaining() {
        return remaining;
    }

    public void setRemaining(int remaining) {
        this.remaining = remaining;
    }
}
