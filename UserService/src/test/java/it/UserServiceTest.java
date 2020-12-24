package it;

import com.shawn.user.controller.UserController;
import com.shawn.user.model.dto.*;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Sql(scripts = {"classpath:clear.sql", "classpath:testData.sql"}, executionPhase = BEFORE_TEST_METHOD)
public class UserServiceTest extends BaseMvcTest {

    private final String URL_PREFIX = "/api";

    @Autowired
    private UserController userController;

    @Override
    protected Object controller() {
        return userController;
    }

    @Test
    public void should_return_http_status_ok_when_sign_up_as_passenger() throws Exception {
        String email = "johnny2@passenger.com";
        String password = "password";
        String name = "Johnny2";
        PassengerSignUpReq passengerSignUpReq = PassengerSignUpReq.builder()
                .email(email).name(name).password(password).build();
        mockMvc.perform(jsonRequest(post(URL_PREFIX + "/passengers"),
                passengerSignUpReq))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").isNumber())
                .andExpect(jsonPath("email").value(email));
    }

    @Test
    public void should_return_http_status_ok_when_sign_up_as_driver() throws Exception {
        String email = "Johnny2@driver.com";
        String password = "password";
        String name = "Johnny2";
        String carType = "Sport";
        DriverSignUpReq driverSignUpReq = DriverSignUpReq.builder()
                .email(email).password(password)
                .name(name).carType(carType).build();
        mockMvc.perform(jsonRequest(post(URL_PREFIX + "/drivers"),
                driverSignUpReq))
                .andExpect(status().isOk());
    }

    @Test
    public void should_return_account_email_when_sign_in_success() throws Exception {
        String email = "Johnny@passenger.com";
        String password = "password";
        SignInReq signInReq = SignInReq.builder().email(email).password(password).build();
        mockMvc.perform(jsonRequest(post(URL_PREFIX + "/users/signIn"), signInReq))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").isNumber())
                .andExpect(jsonPath("email").value(email))
                .andExpect(jsonPath("name").isString());
    }

    @Test
    public void should_return_status_ok_when__driver_participate_activity() throws Exception {
        String activity = "ValentinesDay";
        int driverId = 2;
        mockMvc.perform(post(String.format("%s/activities/%s/drivers/%d", URL_PREFIX, activity, driverId)))
                .andExpect(status().isOk());
    }

    @Test
    public void should_return_http_status_ok_when_update_passenger_location() throws Exception {
        int passengerId = 1;
        mockMvc.perform(put(String.format("%s/users/%d/location", URL_PREFIX, passengerId))
                .param("latitude", "25.047713")
                .param("longitude", "121.5174007"))
                .andExpect(status().isOk());
    }

    @Test
    public void should_return_http_status_ok_when_update_driver_location() throws Exception {
        int passengerId = 2;
        mockMvc.perform(put(String.format("%s/users/%d/location", URL_PREFIX, passengerId))
                .param("latitude", "25.03606")
                .param("longitude", "121.50653798"))
                .andExpect(status().isOk());
    }



    @Test
    public void should_return_participants_amount_when_get_valentines_day_participants() throws Exception {
        String activity = "ValentinesDay";
        MvcResult mvcResult = mockMvc.perform(get(String.format("%s/activities/%s/drivers", URL_PREFIX, activity)))
                .andExpect(status().isOk())
                .andReturn();
        assertEquals(mvcResult.getResponse().getContentLength(), 0);
    }
}