package com.niyiment.backup.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ScheduleResponse {
    private final boolean success;
    private final String message;
    private String jobId;
    private String jobGroup;

    //<editor-fold defaultstate="collapsed" desc="delombok">
    @SuppressWarnings("all")
    public boolean isSuccess() {
        return this.success;
    }

    @SuppressWarnings("all")
    public String getMessage() {
        return this.message;
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
    public void setJobId(final String jobId) {
        this.jobId = jobId;
    }

    @SuppressWarnings("all")
    public void setJobGroup(final String jobGroup) {
        this.jobGroup = jobGroup;
    }

    @Override
    @SuppressWarnings("all")
    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof ScheduleResponse)) return false;
        final ScheduleResponse other = (ScheduleResponse) o;
        if (!other.canEqual((Object) this)) return false;
        if (this.isSuccess() != other.isSuccess()) return false;
        final Object this$message = this.getMessage();
        final Object other$message = other.getMessage();
        if (this$message == null ? other$message != null : !this$message.equals(other$message)) return false;
        final Object this$jobId = this.getJobId();
        final Object other$jobId = other.getJobId();
        if (this$jobId == null ? other$jobId != null : !this$jobId.equals(other$jobId)) return false;
        final Object this$jobGroup = this.getJobGroup();
        final Object other$jobGroup = other.getJobGroup();
        if (this$jobGroup == null ? other$jobGroup != null : !this$jobGroup.equals(other$jobGroup)) return false;
        return true;
    }

    @SuppressWarnings("all")
    protected boolean canEqual(final Object other) {
        return other instanceof ScheduleResponse;
    }

    @Override
    @SuppressWarnings("all")
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * PRIME + (this.isSuccess() ? 79 : 97);
        final Object $message = this.getMessage();
        result = result * PRIME + ($message == null ? 43 : $message.hashCode());
        final Object $jobId = this.getJobId();
        result = result * PRIME + ($jobId == null ? 43 : $jobId.hashCode());
        final Object $jobGroup = this.getJobGroup();
        result = result * PRIME + ($jobGroup == null ? 43 : $jobGroup.hashCode());
        return result;
    }

    @Override
    @SuppressWarnings("all")
    public String toString() {
        return "ScheduleResponse(success=" + this.isSuccess() + ", message=" + this.getMessage() + ", jobId=" + this.getJobId() + ", jobGroup=" + this.getJobGroup() + ")";
    }

    @SuppressWarnings("all")
    public ScheduleResponse(final boolean success, final String message) {
        this.success = success;
        this.message = message;
    }
    //</editor-fold>
}
