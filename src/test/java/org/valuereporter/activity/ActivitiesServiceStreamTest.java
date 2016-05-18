package org.valuereporter.activity;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

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
        List<String> list = new ArrayList<>();

        list.add("Hello");
        list.add("Hello");
        list.add("World");

        Map<String, Long> counted = list.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        assertEquals(2, counted.get("Hello").longValue());




    }

    @Test
    public void testFindLogonsByUserid() throws Exception {

    }
}