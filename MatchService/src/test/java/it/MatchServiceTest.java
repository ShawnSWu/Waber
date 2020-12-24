package it;

import com.shawn.match.controller.MatchController;
import com.shawn.match.model.dto.*;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;
import static org.springframework.test.web.client.ExpectedCount.manyTimes;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@Sql(scripts = {"classpath:clear.sql", "classpath:testData.sql"}, executionPhase = BEFORE_TEST_METHOD)
public class MatchServiceTest extends BaseMvcTest {

    private final String URL_PREFIX = "/api";

    @Autowired
    private MatchController matchController;

    @Autowired
    RestTemplate restTemplate;

    @Value("${user.service.rul}")
    private String USER_SERVICE_API;


    @Override
    protected Object getController() {
        return matchController;
    }


    @Test
    public void should_return_matchId_when_start_matching() throws Exception {
        int activityId = 1;
        String preferredConditionActivity = "ValentinesDay";
        int activityExtraPrice = 30;

        int carTypeId = 1;
        String carTypeName = "Sport";
        int carTypeExtraPrice = 1000;

        ActivityResponse exceptActivityResponse = ActivityResponse.builder().id(activityId).name(preferredConditionActivity).extraPrice(activityExtraPrice).build();
        mockServer
                .expect(manyTimes(), requestTo(String.format("%s/api/activities/name/%s", USER_SERVICE_API, preferredConditionActivity)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(toJson(exceptActivityResponse)));

        CarTypeResponse exceptCarTypeResponse = CarTypeResponse.builder().id(carTypeId).type(carTypeName).extraPrice(carTypeExtraPrice).build();
        mockServer
                .expect(manyTimes(), requestTo(String.format("%s/api/carType/name/%s", USER_SERVICE_API, carTypeName)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(toJson(exceptCarTypeResponse)));


        DriverDto[] exceptDrivers = {DriverDto.builder().id(1).build()};
        mockServer
                .expect(manyTimes(), requestTo(String.format("%s/api/activities/%d/carType/%d", USER_SERVICE_API, activityId, carTypeId)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(toJson(exceptDrivers)));

        int passengerId = 1;

        UserLocationDto exceptUserLocationDto = UserLocationDto.builder().userId(passengerId).latitude(25.047713).longitude(121.5174007).build();
        mockServer
                .expect(manyTimes(), requestTo(String.format("%s/api/users/%d/location", USER_SERVICE_API, passengerId)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(toJson(exceptUserLocationDto)));

        String PreferredConditionCarType = "Sport";
        LocationDto location = LocationDto.builder().latitude(25.047713).longitude(121.5174007).build();
        MatchPreferredConditionDto preferredCondition = MatchPreferredConditionDto.builder()
                .activityName(preferredConditionActivity).carType(PreferredConditionCarType).startLocation(location).build();
        mockMvc.perform(jsonRequest(post(String.format("%s/users/%d/match", URL_PREFIX, passengerId)), preferredCondition))
                .andExpect(status().isOk())
                .andExpect(jsonPath("matchId").isNumber())
                .andReturn();
    }

    @Test
    public void should_return_waiting_confirm_match_trip_when_wait_for_match() throws Exception {

        int activityId = 1;
        int carTypeId = 1;
        int passengerId = 1;
        int driverId = 2;

        DriverDto driver = DriverDto.builder().id(2).carTypeId(carTypeId).build();
        mockServer
                .expect(manyTimes(), requestTo(String.format("%s/api/users/%d", USER_SERVICE_API, driverId)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(toJson(driver)));

        DriverDto[] drivers = {DriverDto.builder().id(1).build()};
        mockServer
                .expect(manyTimes(), requestTo(String.format("%s/api/activities/%d/carType/%d", USER_SERVICE_API, activityId, carTypeId)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(toJson(drivers)));


        UserLocationDto userLocationDto = UserLocationDto.builder().userId(passengerId).latitude(25.047713).longitude(121.5174007).build();
        mockServer
                .expect(manyTimes(), requestTo(String.format("%s/api/users/%d/location", USER_SERVICE_API, passengerId)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(toJson(userLocationDto)));

        int matchId1 = 1;
        mockMvc.perform(get(String.format("%s/users/%d/match/%d", URL_PREFIX, passengerId, matchId1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("completed").value(true))
                .andExpect(jsonPath("id").value(1));

        int matchId2 = 2;
        mockMvc.perform(get(String.format("%s/users/%d/match/%d", URL_PREFIX, passengerId, matchId2)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("completed").value(false));

    }

}
