package org.valuereporter.activity;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import static org.slf4j.LoggerFactory.getLogger;
import static org.testng.Assert.assertEquals;
import static org.testng.AssertJUnit.assertTrue;

/**
 * Created by baardl on 18/05/2016.
 */
public class ActivitiesServiceStreamTest {
    private static final Logger log = getLogger(ActivitiesServiceStreamTest.class);
    private ObjectMapper mapper;

    @BeforeClass
    public void beforeClass() throws Exception{
        mapper = new ObjectMapper();
    }

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

    @Test
    public void testUserSessionActivities() throws Exception{
        List<UserSessionObservedActivity> observedActivities = new ArrayList<>();
        String byPassword = "userSessionCreatedByPassword";
        String byPin = "userSessionCreatedByPin";
        String appToken1 = "appToken1";
        String appToken2 = "appToken2";
        UserSessionObservedActivity user1 = new UserSessionObservedActivity("user1", byPassword, appToken1);
        assertEquals(UserSessionObservedActivity.USER_SESSION_ACTIVITY, user1.getName());
        observedActivities.add(user1);
        observedActivities.add(new UserSessionObservedActivity("user1", byPassword, appToken2));
        observedActivities.add(new UserSessionObservedActivity("user1", byPin, appToken1));
        String json = this.mapper.writeValueAsString(observedActivities);
        assertTrue(json.contains("ByPin"));
        log.debug("Activities json {}", json);
        Map<String, Long> counted = observedActivities.stream().collect(groupingBy(UserSessionObservedActivity::getName, counting()));

        assertEquals(observedActivities.size(),3);
        assertEquals(counted.get(UserSessionObservedActivity.USER_SESSION_ACTIVITY).longValue(),3);
        assertEquals(counted.size(),1);
    }

    private ObservedActivity buildStubedActivity(String activity) {
        return new ObservedActivity(activity, System.currentTimeMillis(),null);
    }

    @Test
    public void testFindLogonsByUserid() throws Exception {

    }

    public class UserSessionObservedActivity extends org.valuereporter.agent.activity.ObservedActivity {
        public static final String USER_SESSION_ACTIVITY = "userSession";
        private static final String USER_SESSION_ACTIVITY_DB_KEY = "userid";

        public UserSessionObservedActivity(String userid,String userSessionActivity,String applicationtokenid) {
            super(USER_SESSION_ACTIVITY, System.currentTimeMillis());
            String applicationid = "app" + applicationtokenid;
            put("userid", userid);
            put("usersessionfunction", userSessionActivity);
            put("applicationtokenid", applicationtokenid);
            put("applicationid", applicationid);
        }
    }
}