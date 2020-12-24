package it;

import com.shawn.trip.controller.TripController;
import com.shawn.trip.model.dto.MatchTripResponse;
import com.shawn.trip.model.dto.PickUpResponse;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Sql(scripts = {"classpath:clear.sql", "classpath:testData.sql"}, executionPhase = BEFORE_TEST_METHOD)
public class TripServiceTest extends BaseMvcTest {

    private final String URL_PREFIX = "/api";

    @Value("${trip.service.url}")
    String MATCH_SERVICE_API;

    @Autowired
    TripController tripController;

    @Override
    protected Object controller() {
        return tripController;
    }

    @Test
    public void should_return_matchId_when_pick_up_a_trip() throws Exception {
        int passengerId = 1;
        int matchId = 1;
        int tripId = 2;

        PickUpResponse exceptPickUpResponse = PickUpResponse.builder().id(tripId).build();
        mockServer
                .expect(manyTimes(), requestTo(String.format("%s/api/users/%d/match/%d/accept", MATCH_SERVICE_API, passengerId, matchId)))
                .andExpect(method(HttpMethod.PUT))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(toJson(exceptPickUpResponse)));

        int driverId = 2, activityId = 1, matchStatus = 1, carTypeId = 1;
        int distance = 700;
        double startPositionLatitude = 25.047713;
        double startPositionLongitude = 121.5174007;
        double destinationPositionLatitude = 25.03606;
        double destinationPositionLongitude = 121.50653798;
        String date = "2020-02-14", time = "01:31:03";

        MatchTripResponse exceptMatchTrip = MatchTripResponse.builder().id(matchId).activity(activityId).carType(carTypeId)
                .driverId(driverId).distance(distance).passengerId(passengerId)
                .startPositionLongitude(startPositionLatitude).startPositionLatitude(startPositionLongitude)
                .destinationLatitude(destinationPositionLatitude).destinationLongitude(destinationPositionLongitude)
                .date(date).time(time)
                .matchStatus(matchStatus).build();
        mockServer
                .expect(manyTimes(), requestTo(String.format("%s/api/users/%d/match/%d", MATCH_SERVICE_API, passengerId, matchId)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(toJson(exceptMatchTrip)));

        mockMvc.perform(post(String.format("%s/users/%d/match/%d/trip", URL_PREFIX, passengerId, matchId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(tripId));

    }

    @Test
    public void should_return_status_ok_when_arrive_destination() throws Exception {
        int passengerId = 1;
        int matchId = 1;
        int tripId = 1;

        int tripDistance = 700;
        MatchTripResponse exceptMatchTripResponse = MatchTripResponse.builder().distance(tripDistance).build();
        mockServer
                .expect(manyTimes(), requestTo(String.format("%s/api/users/%d/match/%d", MATCH_SERVICE_API, passengerId, matchId)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(toJson(exceptMatchTripResponse)));

        mockMvc.perform(post(String.format("%s/users/%d/match/%d/trip/%d/arrive", URL_PREFIX, passengerId, matchId, tripId)))
                .andExpect(status().isOk());
    }

}
