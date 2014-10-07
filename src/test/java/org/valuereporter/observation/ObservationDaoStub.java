package org.valuereporter.observation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

/**
 * @author <a href="bard.lind@gmail.com">Bard Lind</a>
 */
public class ObservationDaoStub extends ObservationDao {
    private static final Logger log = LoggerFactory.getLogger(ObservationDaoStub.class);
    public ObservationDaoStub(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    public int[] updateStatistics(String prefix, List<ObservedInterval> intervals) {

        for (ObservedInterval interval : intervals) {
            log.info("updateStatistics {}, {}, {}, {}", prefix, interval.getMethodName(), interval.getCount(), interval.getStartTime());
        }
        return new int[intervals.size()];
    }
}
