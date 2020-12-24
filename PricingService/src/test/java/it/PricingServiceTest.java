package it;

import com.shawn.match.controller.PricingController;
import com.shawn.match.model.dto.ActivityResponse;
import com.shawn.match.model.dto.CarTypeResponse;
import com.shawn.match.model.dto.MatchTripResponse;
import com.shawn.match.model.dto.TripResponse;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import static org.springframework.test.web.client.ExpectedCount.manyTimes;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PricingServiceTest extends BaseMvcTest {

    private final String URL_PREFIX = "/api";

    @Value("${trip.service.url}")
    private String TRIP_SERVICE_API;

    @Value("${match.service.url}")
    private String MATCH_SERVICE_API;

    @Value("${user.service.url}")
    private String USER_SERVICE_API;

    @Autowired
    PricingController pricingController;

    @Override
    protected Object controller() {
        return pricingController;
    }

    @Test
    public void should_calculate_price_1100_when_distance_is_700_and_carType_is_sport_and_activity_is_valentineDay() throws Exception {
        int passengerId = 1, matchId = 1, tripId = 1;
        int activityId = 1, carTypeId = 1;
        int tripDistance = 700;
        int carTypeExtraPrice = 1000, activityExtraPrice = 30;

        TripResponse exceptTripResponse = TripResponse.builder().tripDistance(tripDistance).build();
        mockServer
                .expect(manyTimes(), requestTo(String.format("%s/api/users/%d/match/%d/trip/%d", TRIP_SERVICE_API, passengerId, matchId, tripId)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(toJson(exceptTripResponse)));

        MatchTripResponse exceptMatchTripResponse = MatchTripResponse.builder().activityId(activityId).carTypeId(carTypeId).build();
        mockServer
                .expect(manyTimes(), requestTo(String.format("%s/api/users/%d/match/%d", MATCH_SERVICE_API, passengerId, matchId)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(toJson(exceptMatchTripResponse)));

        ActivityResponse exceptActivityResponse = ActivityResponse.builder().extraPrice(activityExtraPrice).build();
        mockServer
                .expect(manyTimes(), requestTo(String.format("%s/api/activities/id/%d", USER_SERVICE_API, activityId)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(toJson(exceptActivityResponse)));

        CarTypeResponse carTypeResponse = CarTypeResponse.builder().extraPrice(carTypeExtraPrice).build();
        mockServer
                .expect(manyTimes(), requestTo(String.format("%s/api/carType/id/%d", USER_SERVICE_API, carTypeId)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(toJson(carTypeResponse)));

        mockMvc.perform(get(String.format("%s/users/%d/match/%d/trip/%d/price", URL_PREFIX, passengerId, matchId, tripId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("price").isNumber())
                .andExpect(jsonPath("price").value(1100));
    }

}
