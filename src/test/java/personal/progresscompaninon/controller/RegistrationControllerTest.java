package personal.progresscompaninon.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import personal.progresscompaninon.model.User;
import personal.progresscompaninon.service.UserService;

import java.util.ArrayList;


import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RegistrationController.class)
class RegistrationControllerTest {

    @Autowired
    private MockMvc mvc;
    @MockBean
    private AuthenticationManager authenticationManager;
    @MockBean
    private UserService userService;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldRegisterUserAndReturnIt() throws Exception {
        User user = User.builder()
                .firstName("fn")
                .lastName("ln")
                .email("test@gmail.com")
                .password("testing123")
                .roles(new ArrayList<>())
                .build();
        //Converts user to Json
        String expectedJson = objectMapper.writeValueAsString(user);
        mvc.perform(MockMvcRequestBuilders
                        .post("/auth/signup")
                        .content(objectMapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                //checks if response is equal to expected Json
                .andExpect(content().string(expectedJson));
    }

}