package org.valuereporter.value;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @author <a href="bard.lind@gmail.com">Bard Lind</a>
 */
@Component
public class ValueDao {
    private static final Logger log = LoggerFactory.getLogger(ValueDao.class);

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public ValueDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<ValuableMethod> findUsageByMethod(String prefix) {

        String sql = "SELECT methodName, COUNT(*) FROM ObservedMethod WHERE prefix = ? GROUP BY methodName";
        Object[] parameters = new Object[] {prefix};
        List<ValuableMethod> valuableMethods = jdbcTemplate.query(sql, parameters, new RowMapper<ValuableMethod>() {
            @Override
            public ValuableMethod mapRow(ResultSet resultSet, int i) throws SQLException {

                ValuableMethod observedMethod = new ValuableMethod(
                        resultSet.getString(1),
                        resultSet.getLong(2));
                return observedMethod;
            }
        });
        return valuableMethods;
    }
}
