package com.niyiment.backup.domain.dto;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

public class ScheduleRequest {
    @NotNull
    private String jobClass;
    @NotNull
    private String cronExpression;
    private Map<String, Object> jobData = new HashMap<>();

    //<editor-fold defaultstate="collapsed" desc="delombok">
    @SuppressWarnings("all")
    public ScheduleRequest() {
    }

    @SuppressWarnings("all")
    public String getJobClass() {
        return this.jobClass;
    }

    @SuppressWarnings("all")
    public String getCronExpression() {
        return this.cronExpression;
    }

    @SuppressWarnings("all")
    public Map<String, Object> getJobData() {
        return this.jobData;
    }

    @SuppressWarnings("all")
    public void setJobClass(final String jobClass) {
        this.jobClass = jobClass;
    }

    @SuppressWarnings("all")
    public void setCronExpression(final String cronExpression) {
        this.cronExpression = cronExpression;
    }

    @SuppressWarnings("all")
    public void setJobData(final Map<String, Object> jobData) {
        this.jobData = jobData;
    }

    @Override
    @SuppressWarnings("all")
    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof ScheduleRequest)) return false;
        final ScheduleRequest other = (ScheduleRequest) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$jobClass = this.getJobClass();
        final Object other$jobClass = other.getJobClass();
        if (this$jobClass == null ? other$jobClass != null : !this$jobClass.equals(other$jobClass)) return false;
        final Object this$cronExpression = this.getCronExpression();
        final Object other$cronExpression = other.getCronExpression();
        if (this$cronExpression == null ? other$cronExpression != null : !this$cronExpression.equals(other$cronExpression)) return false;
        final Object this$jobData = this.getJobData();
        final Object other$jobData = other.getJobData();
        if (this$jobData == null ? other$jobData != null : !this$jobData.equals(other$jobData)) return false;
        return true;
    }

    @SuppressWarnings("all")
    protected boolean canEqual(final Object other) {
        return other instanceof ScheduleRequest;
    }

    @Override
    @SuppressWarnings("all")
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $jobClass = this.getJobClass();
        result = result * PRIME + ($jobClass == null ? 43 : $jobClass.hashCode());
        final Object $cronExpression = this.getCronExpression();
        result = result * PRIME + ($cronExpression == null ? 43 : $cronExpression.hashCode());
        final Object $jobData = this.getJobData();
        result = result * PRIME + ($jobData == null ? 43 : $jobData.hashCode());
        return result;
    }

    @Override
    @SuppressWarnings("all")
    public String toString() {
        return "ScheduleRequest(jobClass=" + this.getJobClass() + ", cronExpression=" + this.getCronExpression() + ", jobData=" + this.getJobData() + ")";
    }
    //</editor-fold>
}
