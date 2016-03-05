package org.valuereporter.statistics;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.valuereporter.activity.ActivitiesDao;
import org.valuereporter.activity.ObservedActivity;
import org.valuereporter.whydah.LogonDao;

import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by t-blind5-01 on 02.03.2016.
 */
@Service
public class ActivityStatisticsService {
    private static final Logger log = getLogger(ActivityStatisticsService.class);

    private final ActivitiesDao activitiesDao;
    private final LogonDao logonDao;

    @Autowired
    public ActivityStatisticsService(ActivitiesDao activitiesDao, LogonDao logonDao) {
        this.activitiesDao = activitiesDao;
        this.logonDao = logonDao;
    }


    public ActivityStatistics findUserLogons(Long startTime, Long endTime) {
        DateTime startPeriod = null;
        DateTime endPeriod = null;
        if (endTime == null) {
            endPeriod = new DateTime();
        } else {
            endPeriod = new DateTime(endTime);
        }
        if (startTime == null) {
            startPeriod = endPeriod.minusDays(1);
        } else {
            startPeriod = new DateTime(startTime);
        }


        List<Long> logons = logonDao.findLogons(startPeriod,endPeriod);
        ActivityStatistics activityStatistics = new ActivityStatistics("All","userlogon", startPeriod.getMillis(), endPeriod.getMillis());
        activityStatistics.add("userlogons", logons);
        return activityStatistics;
    }

    public ActivityStatistics findUserSessions(Long startTime, Long endTime) {
        DateTime startPeriod = null;
        DateTime endPeriod = null;
        if (endTime == null) {
            endPeriod = new DateTime();
        } else {
            endPeriod = new DateTime(endTime);
        }
        if (startTime == null) {
            startPeriod = endPeriod.minusDays(1);
        } else {
            startPeriod = new DateTime(startTime);
        }
        List<ObservedActivity> userSessions = activitiesDao.findUserSessions(startPeriod, endPeriod);
        ActivityStatistics activityStatistics = new ActivityStatistics("All","userSession", startPeriod.getMillis(), endPeriod.getMillis());
        activityStatistics.add("userSessions", userSessions);
        return activityStatistics;
    }
}
