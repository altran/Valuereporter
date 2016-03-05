package org.valuereporter.statistics;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by baardl on 05.03.16.
 */
public class ActivityStatistics {

    private String prefix = "ALL";
    private String activityName;
    private Long startTime;
    private Long endTime;
    private Map<String, Object> activities = new HashMap<>();

    public ActivityStatistics(String activityName) {
        this.activityName = activityName;
    }

    public ActivityStatistics(String prefix, String activityName, Long startTime, Long endTime) {
        this.prefix = prefix;
        this.activityName = activityName;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public void add(String userlogons, List<Long> logons) {
        if (activities == null){
            activities = new HashMap<String,Object>();
        }
        activities.put(userlogons,logons);
    }

    public Map<String, Object> getActivities() {
        return activities;
    }
}
