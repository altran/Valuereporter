package org.valuereporter.activity;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.IntStream;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by t-blind5-01 on 02.03.2016.
 */
@Component
public class ActivitiesDao {
    private static final Logger log = getLogger(ActivitiesDao.class);

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ActivitiesDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    protected long insertActivities(String tableName, final List<String> columnNames, final List<ObservedActivity> activities) {
        if (tableName == null || tableName.isEmpty()) {
            throw new IllegalArgumentException("tableName must have a value");
        }
        if (columnNames == null) {
            throw new IllegalArgumentException("columntNames must not be null");
        }
        String sql = buildSql(tableName, columnNames);

        int [] updatePrStatement = jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ObservedActivity activity = activities.get(i);
                int paramNum = 1;
                for (String columnName : columnNames) {
                    ps.setObject(paramNum, activity.getValue(columnName));
                }
//                ps.setObject(1, activity.getValue(columnNames.get(0)));
//                ps.setLong(1, customer.getCustId());
//                ps.setString(2, customer.getName());
//                ps.setInt(3, customer.getAge() );
            }

            @Override
            public int getBatchSize() {
                return activities.size();
            }
        });

        int sum = 0;
        if (updatePrStatement != null) {
            sum = IntStream.of(updatePrStatement).sum();
        }
        return sum;
    }

    protected String buildSql(String tableName, List<String> columnNames) {
        String sql = "INSERT INTO " + tableName +
                "(";
        for (String columnName : columnNames) {
            sql += columnName + ", ";
        }
        sql = sql.substring(0,sql.length() - 2);
        sql += ") VALUES (";
        for (int i = 0; i < columnNames.size(); i++) {
            if (i < (columnNames.size() - 1)) {
                sql += "?,";
            } else {
                sql += "?";
            }
        }
        sql += ")";
        return sql;
    }
}
