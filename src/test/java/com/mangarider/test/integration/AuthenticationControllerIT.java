package com.mangarider.test.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.mangarider.test.model.LoginRequestTest;
import com.mangarider.test.model.RegistrationRequestTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.mangarider.test.ServiceEndpoints.AuthenticationController;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class AuthenticationControllerIT extends BasicIntegrationTest {
    private static final String DEFAULT_USERNAME = "dummyusername";
    private static final String DEFAULT_EMAIL = "dummymail@mail.com";
    private static final String DEFAULT_PASSWORD = "password";
    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new ParameterNamesModule())
            .registerModule(new Jdk8Module())
            .registerModule(new JavaTimeModule());;


    @Test
    public void UserRegistration_ValidData_Success() throws Exception {
        RegistrationRequestTest content = RegistrationRequestTest.builder()
                .username(DEFAULT_USERNAME)
                .email(DEFAULT_EMAIL)
                .password(DEFAULT_PASSWORD)
                .build();

        mockMvc.perform(
                MockMvcRequestBuilders.post(AuthenticationController.REGISTRATION.value)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(content))
        ).andExpectAll(
                status().is2xxSuccessful()
        );
    }

    @Test
    public void UserRegistration_InvalidData_BadRequest() throws Exception {
        RegistrationRequestTest content = RegistrationRequestTest.builder()
                .username("")
                .email("not-mail")
                .password("")
                .build();

        mockMvc.perform(
                MockMvcRequestBuilders.post(AuthenticationController.REGISTRATION.value)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(content))
        ).andExpectAll(
                status().is4xxClientError()
        );
    }

    @Test
    public void UserRegistration_UserAlreadyExists_Conflict() throws Exception {
        RegistrationRequestTest content = RegistrationRequestTest.builder()
                .username(DEFAULT_USERNAME)
                .email(DEFAULT_EMAIL)
                .password(DEFAULT_PASSWORD)
                .build();

        mockMvc.perform(
                MockMvcRequestBuilders.post(AuthenticationController.REGISTRATION.value)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(content))
        ).andExpectAll(
                status().is2xxSuccessful()
        );

        mockMvc.perform(
                MockMvcRequestBuilders.post(AuthenticationController.REGISTRATION.value)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(content))
        ).andExpectAll(
                status().is4xxClientError()
        );
    }

    @Test
    public void LogIn_ValidData_Ok() throws Exception {
        RegistrationRequestTest content = RegistrationRequestTest.builder()
                .username(DEFAULT_USERNAME)
                .email(DEFAULT_EMAIL)
                .password(DEFAULT_PASSWORD)
                .build();

        mockMvc.perform(
                MockMvcRequestBuilders.post(AuthenticationController.REGISTRATION.value)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(content))
        ).andExpectAll(
                status().is2xxSuccessful()
        );

        LoginRequestTest loginRequest = LoginRequestTest.builder()
                .email(DEFAULT_EMAIL)
                .password(DEFAULT_PASSWORD)
                .build();

        mockMvc.perform(
                MockMvcRequestBuilders.post(AuthenticationController.LOGIN.value)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest))
        ).andExpectAll(
                status().is2xxSuccessful()
        );
    }

    @Test
    public void LogIn_InvalidData_BadRequest() throws Exception {
        RegistrationRequestTest content = RegistrationRequestTest.builder()
                .username(DEFAULT_USERNAME)
                .email(DEFAULT_EMAIL)
                .password(DEFAULT_PASSWORD)
                .build();

        mockMvc.perform(
                MockMvcRequestBuilders.post(AuthenticationController.REGISTRATION.value)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(content))
        ).andExpectAll(
                status().is2xxSuccessful()
        );

        LoginRequestTest loginRequest = LoginRequestTest.builder()
                .email("")
                .password("")
                .build();

        mockMvc.perform(
                MockMvcRequestBuilders.post(AuthenticationController.LOGIN.value)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest))
        ).andExpectAll(
                status().is4xxClientError()
        );
    }

    @Test
    public void LogIn_ValidDataUserNotFound_NotFound() throws Exception {
        LoginRequestTest loginRequest = LoginRequestTest.builder()
                .email(DEFAULT_EMAIL)
                .password(DEFAULT_PASSWORD)
                .build();

        mockMvc.perform(
                MockMvcRequestBuilders.post(AuthenticationController.LOGIN.value)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest))
        ).andExpectAll(
                status().is4xxClientError()
        );
    }

}
