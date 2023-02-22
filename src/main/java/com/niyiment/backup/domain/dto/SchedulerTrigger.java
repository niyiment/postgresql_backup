package com.niyiment.backup.domain.dto;

import java.util.Date;

public class SchedulerTrigger {
    private String jobId;
    private String jobGroup;
    private String cronExpression;
    private Boolean active = true;
    private String description;
    private Date startTime;
    private Date endTime;
    private Date finalFireTime;
    private Date nextFireTime;
    private Date previousFireTime;

    //<editor-fold defaultstate="collapsed" desc="delombok">
    @SuppressWarnings("all")
    public SchedulerTrigger() {
    }

    @SuppressWarnings("all")
    public String getJobId() {
        return this.jobId;
    }

    @SuppressWarnings("all")
    public String getJobGroup() {
        return this.jobGroup;
    }

    @SuppressWarnings("all")
    public String getCronExpression() {
        return this.cronExpression;
    }

    @SuppressWarnings("all")
    public Boolean getActive() {
        return this.active;
    }

    @SuppressWarnings("all")
    public String getDescription() {
        return this.description;
    }

    @SuppressWarnings("all")
    public Date getStartTime() {
        return this.startTime;
    }

    @SuppressWarnings("all")
    public Date getEndTime() {
        return this.endTime;
    }

    @SuppressWarnings("all")
    public Date getFinalFireTime() {
        return this.finalFireTime;
    }

    @SuppressWarnings("all")
    public Date getNextFireTime() {
        return this.nextFireTime;
    }

    @SuppressWarnings("all")
    public Date getPreviousFireTime() {
        return this.previousFireTime;
    }

    @SuppressWarnings("all")
    public void setJobId(final String jobId) {
        this.jobId = jobId;
    }

    @SuppressWarnings("all")
    public void setJobGroup(final String jobGroup) {
        this.jobGroup = jobGroup;
    }

    @SuppressWarnings("all")
    public void setCronExpression(final String cronExpression) {
        this.cronExpression = cronExpression;
    }

    @SuppressWarnings("all")
    public void setActive(final Boolean active) {
        this.active = active;
    }

    @SuppressWarnings("all")
    public void setDescription(final String description) {
        this.description = description;
    }

    @SuppressWarnings("all")
    public void setStartTime(final Date startTime) {
        this.startTime = startTime;
    }

    @SuppressWarnings("all")
    public void setEndTime(final Date endTime) {
        this.endTime = endTime;
    }

    @SuppressWarnings("all")
    public void setFinalFireTime(final Date finalFireTime) {
        this.finalFireTime = finalFireTime;
    }

    @SuppressWarnings("all")
    public void setNextFireTime(final Date nextFireTime) {
        this.nextFireTime = nextFireTime;
    }

    @SuppressWarnings("all")
    public void setPreviousFireTime(final Date previousFireTime) {
        this.previousFireTime = previousFireTime;
    }

    @Override
    @SuppressWarnings("all")
    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof SchedulerTrigger)) return false;
        final SchedulerTrigger other = (SchedulerTrigger) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$active = this.getActive();
        final Object other$active = other.getActive();
        if (this$active == null ? other$active != null : !this$active.equals(other$active)) return false;
        final Object this$jobId = this.getJobId();
        final Object other$jobId = other.getJobId();
        if (this$jobId == null ? other$jobId != null : !this$jobId.equals(other$jobId)) return false;
        final Object this$jobGroup = this.getJobGroup();
        final Object other$jobGroup = other.getJobGroup();
        if (this$jobGroup == null ? other$jobGroup != null : !this$jobGroup.equals(other$jobGroup)) return false;
        final Object this$cronExpression = this.getCronExpression();
        final Object other$cronExpression = other.getCronExpression();
        if (this$cronExpression == null ? other$cronExpression != null : !this$cronExpression.equals(other$cronExpression)) return false;
        final Object this$description = this.getDescription();
        final Object other$description = other.getDescription();
        if (this$description == null ? other$description != null : !this$description.equals(other$description)) return false;
        final Object this$startTime = this.getStartTime();
        final Object other$startTime = other.getStartTime();
        if (this$startTime == null ? other$startTime != null : !this$startTime.equals(other$startTime)) return false;
        final Object this$endTime = this.getEndTime();
        final Object other$endTime = other.getEndTime();
        if (this$endTime == null ? other$endTime != null : !this$endTime.equals(other$endTime)) return false;
        final Object this$finalFireTime = this.getFinalFireTime();
        final Object other$finalFireTime = other.getFinalFireTime();
        if (this$finalFireTime == null ? other$finalFireTime != null : !this$finalFireTime.equals(other$finalFireTime)) return false;
        final Object this$nextFireTime = this.getNextFireTime();
        final Object other$nextFireTime = other.getNextFireTime();
        if (this$nextFireTime == null ? other$nextFireTime != null : !this$nextFireTime.equals(other$nextFireTime)) return false;
        final Object this$previousFireTime = this.getPreviousFireTime();
        final Object other$previousFireTime = other.getPreviousFireTime();
        if (this$previousFireTime == null ? other$previousFireTime != null : !this$previousFireTime.equals(other$previousFireTime)) return false;
        return true;
    }

    @SuppressWarnings("all")
    protected boolean canEqual(final Object other) {
        return other instanceof SchedulerTrigger;
    }

    @Override
    @SuppressWarnings("all")
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $active = this.getActive();
        result = result * PRIME + ($active == null ? 43 : $active.hashCode());
        final Object $jobId = this.getJobId();
        result = result * PRIME + ($jobId == null ? 43 : $jobId.hashCode());
        final Object $jobGroup = this.getJobGroup();
        result = result * PRIME + ($jobGroup == null ? 43 : $jobGroup.hashCode());
        final Object $cronExpression = this.getCronExpression();
        result = result * PRIME + ($cronExpression == null ? 43 : $cronExpression.hashCode());
        final Object $description = this.getDescription();
        result = result * PRIME + ($description == null ? 43 : $description.hashCode());
        final Object $startTime = this.getStartTime();
        result = result * PRIME + ($startTime == null ? 43 : $startTime.hashCode());
        final Object $endTime = this.getEndTime();
        result = result * PRIME + ($endTime == null ? 43 : $endTime.hashCode());
        final Object $finalFireTime = this.getFinalFireTime();
        result = result * PRIME + ($finalFireTime == null ? 43 : $finalFireTime.hashCode());
        final Object $nextFireTime = this.getNextFireTime();
        result = result * PRIME + ($nextFireTime == null ? 43 : $nextFireTime.hashCode());
        final Object $previousFireTime = this.getPreviousFireTime();
        result = result * PRIME + ($previousFireTime == null ? 43 : $previousFireTime.hashCode());
        return result;
    }

    @Override
    @SuppressWarnings("all")
    public String toString() {
        return "SchedulerTrigger(jobId=" + this.getJobId() + ", jobGroup=" + this.getJobGroup() + ", cronExpression=" + this.getCronExpression() + ", active=" + this.getActive() + ", description=" + this.getDescription() + ", startTime=" + this.getStartTime() + ", endTime=" + this.getEndTime() + ", finalFireTime=" + this.getFinalFireTime() + ", nextFireTime=" + this.getNextFireTime() + ", previousFireTime=" + this.getPreviousFireTime() + ")";
    }
    //</editor-fold>
}
