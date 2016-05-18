package org.valuereporter.activity;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import static org.testng.Assert.assertEquals;

/**
 * Created by baardl on 18/05/2016.
 */
public class ActivitiesServiceStreamTest {

    @BeforeMethod
    public void setUp() throws Exception {

    }

    @Test
    public void testUpdateActivities() throws Exception {

    }

    @Test
    public void testUpdateActivitiesByName() throws Exception {
        List<ObservedActivity> list = new ArrayList<>();

        list.add(buildStubedActivity("logon"));
        list.add(buildStubedActivity("Hello"));
        list.add(buildStubedActivity("World"));
        list.add(buildStubedActivity("logon"));

        Map<String, Long> counted = list.stream().collect(groupingBy(ObservedActivity::getName, counting()));

        assertEquals(4, list.size());
        assertEquals(2, counted.get("logon").longValue());
        assertEquals(3, counted.size());




    }

    private ObservedActivity buildStubedActivity(String activity) {
        return new ObservedActivity(activity, System.currentTimeMillis(),null);
    }

    @Test
    public void testFindLogonsByUserid() throws Exception {

    }
}