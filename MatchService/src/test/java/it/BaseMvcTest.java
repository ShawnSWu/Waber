package it;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shawn.match.MatchApplication;
import com.shawn.match.controller.MatchController;
import com.shawn.match.service.MatchServiceImpl;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;

/**
 * @author swshawnwu@gmail.com(ShawnWu)
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = MatchApplication.class)
@WebAppConfiguration
public abstract class BaseMvcTest {

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected RestTemplate restTemplate;

    protected MockMvc mockMvc;

    protected MockRestServiceServer mockServer;


    protected abstract Object getController();


    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(getController()).build();
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    protected MockHttpServletRequestBuilder jsonRequest(MockHttpServletRequestBuilder requestBuilder, Object object)
            throws JsonProcessingException {
        return requestBuilder.content(toJson(object))
                .contentType(MediaType.APPLICATION_JSON);
    }

    protected String toJson(Object object) throws JsonProcessingException {
        return objectMapper.writeValueAsString(object);
    }

}
