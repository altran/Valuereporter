package org.valuereporter.sla;

import java.util.List;

/**
 * @author <a href="bard.lind@gmail.com">Bard Lind</a>
 */
public class SlaStatisticsRepresentation {

    private final Long start;
    private final Long end;
    private String prefix;
    private String methodName;
    private List<SlaStatistics> slaStatisticses;

    public SlaStatisticsRepresentation(String prefix, String methodName, Long start, Long end, List<SlaStatistics> slaStatisticses) {
        this.prefix = prefix;
        this.methodName = methodName;
        this.start = start;
        this.end = end;
        this.slaStatisticses = slaStatisticses;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getMethodName() {
        return methodName;
    }

    public List<SlaStatistics> getSlaStatisticses() {
        return slaStatisticses;
    }

    public Long getStart() {
        return start;
    }

    public Long getEnd() {
        return end;
    }
}
