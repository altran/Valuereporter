package org.valuereporter.activity;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by baardl on 05.03.16.
 */
public class UserSessionMapper implements RowMapper {

    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {

        Timestamp startTime = resultSet.getTimestamp("starttime");
        String userid = resultSet.getString("userid");
        String applicationtokenid = resultSet.getString("applicationtokenid");
        String applicationid = resultSet.getString("applicationid");
        Map<String, Object> data = new HashMap<>();
        data.put("userid",userid);
        data.put("applicationtokenid",applicationtokenid);
        data.put("applicationid", applicationid);

        ObservedActivity observedActivity = new ObservedActivity("userSession", startTime.getTime(), data);
        return observedActivity;
    }
}
