package org.valuereporter.activity;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by t-blind5-01 on 02.03.2016.
 */
@Service
public class ActivitiesService {
    private static final Logger log = getLogger(ActivitiesService.class);

    private final ActivitiesDao activitiesDao;

    @Autowired
    public ActivitiesService(ActivitiesDao activitiesDao) {
        this.activitiesDao = activitiesDao;
    }

    public long updateActivities(String prefix, List<ObservedActivity> observedActivities) {
        long updatedActivities = 0;
        if (observedActivities != null && observedActivities.size() > 0) {
            log.trace("Try to update {} activities for {}", observedActivities.size(), prefix);
            //TODO split into separate lambda expressions depending on activity.name
            ObservedActivity activity = observedActivities.get(0);
            String tableName = activity.getName();
            Map<String, Object> data = activity.getData();
            if (data != null && data.keySet() != null) {
                Set<String> keys = data.keySet();
                ArrayList<String> columnNames = new ArrayList<>(keys.size());
                for (String key : keys) {
                    columnNames.add(key);
                }
                updatedActivities = activitiesDao.insertActivities(tableName, columnNames, observedActivities);
            }
        }
        return updatedActivities;
    }
}
