package org.valuereporter.whydah;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by baardl on 05.03.16.
 */
@Component
public class LogonDao {
    private static final Logger log = getLogger(LogonDao.class);

    public static final String START_TIME_COLUMN = "starttime";

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public LogonDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Long> findLogonsByUserId(String userid){
        String sql = "Select starttime from userlogon where userid=?";
        List<Timestamp> logonsTimestamp = jdbcTemplate.queryForList(sql,new Object[]{userid}, Timestamp.class);
        List<Long> logons = new ArrayList<>(logonsTimestamp.size());
        for (Timestamp timestamp : logonsTimestamp) {
            logons.add(timestamp.getTime());
        }
        return logons;
    }
}
