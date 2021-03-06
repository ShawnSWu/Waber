package it;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.shawn.user.UserApplication;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

/**
 * @author swshawnwu@gmail.com(ShawnWu)
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = UserApplication.class)
@WebAppConfiguration
public abstract class BaseMvcTest {

    @Autowired
    protected ObjectMapper objectMapper;

    protected MockMvc mockMvc;

    protected abstract Object controller();


    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller()).build();
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
