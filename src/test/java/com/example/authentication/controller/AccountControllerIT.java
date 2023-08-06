package com.example.authentication.controller;

import com.example.authentication.entity.Account;
import com.example.authentication.repository.AccountRepository;
import com.example.authentication.request.LoginRequest;
import com.example.authentication.request.RegisterRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
class AccountControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private AccountRepository accountRepository;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @AfterEach
    public void teardown() {
        accountRepository.deleteAll();
    }

    @Test
    @DisplayName("register email null")
    public void testRegister_givenEmail_whenNull_thenReturnBadRequest() throws Exception {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setEmail(null);

        MockHttpServletRequestBuilder request = createRequest(registerRequest);

        mockMvc.perform(request)
                .andExpectAll(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("email must not null"));
    }

    @Test
    @DisplayName("register email blank")
    public void testRegister_givenEmail_whenBlank_thenReturnBadRequest() throws Exception {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setEmail("");

        MockHttpServletRequestBuilder request = createRequest(registerRequest);

        mockMvc.perform(request)
                .andExpectAll(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("email must not blank"));
    }

    @Test
    @DisplayName("register email invalid format")
    public void testRegister_givenEmail_whenInvalidFormat_thenReturnBadRequest() throws Exception {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setEmail("test");

        MockHttpServletRequestBuilder request = createRequest(registerRequest);

        mockMvc.perform(request)
                .andExpectAll(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("email invalid format"));
    }

    @Test
    @DisplayName("register password null")
    public void testRegister_givenPassword_whenNull_thenReturnBadRequest() throws Exception {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setEmail("test@mail.com");
        registerRequest.setPassword(null);

        MockHttpServletRequestBuilder request = createRequest(registerRequest);

        mockMvc.perform(request)
                .andExpectAll(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("password must not null"));
    }

    @Test
    @DisplayName("register password blank")
    public void testRegister_givenPassword_whenBlank_thenReturnBadRequest() throws Exception {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setEmail("test@mail.com");
        registerRequest.setPassword("");

        MockHttpServletRequestBuilder request = createRequest(registerRequest);

        mockMvc.perform(request)
                .andExpectAll(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("password must not blank"));
    }

    @Test
    @DisplayName("register confirm password null")
    public void testRegister_givenConfirmPassword_whenNull_thenReturnBadRequest() throws Exception {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setEmail("test@mail.com");
        registerRequest.setPassword("secret");
        registerRequest.setConfirmPassword(null);

        MockHttpServletRequestBuilder request = createRequest(registerRequest);

        mockMvc.perform(request)
                .andExpectAll(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("confirm password must not null"));
    }

    @Test
    @DisplayName("register confirm password blank")
    public void testRegister_givenConfirmPassword_whenBlank_thenReturnBadRequest() throws Exception {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setEmail("test@mail.com");
        registerRequest.setPassword("secret");
        registerRequest.setConfirmPassword("");

        MockHttpServletRequestBuilder request = createRequest(registerRequest);

        mockMvc.perform(request)
                .andExpectAll(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("confirm password must not blank"));
    }

    @Test
    @DisplayName("register password not match")
    public void testRegister_givenPasswordAndConfirmPassword_whenNotMatch_thenReturnBadRequest() throws Exception {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setEmail("test@mail.com");
        registerRequest.setPassword("secret");
        registerRequest.setConfirmPassword("Secret");

        MockHttpServletRequestBuilder request = createRequest(registerRequest);

        mockMvc.perform(request)
                .andExpectAll(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("password not match"));
    }

    @Test
    @DisplayName("register email duplicate")
    public void testRegister_givenEmail_whenDuplicate_thenReturnConflict() throws Exception {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setEmail("test@mail.com");
        registerRequest.setPassword("secret");
        registerRequest.setConfirmPassword("secret");

        Account account = new Account();
        account.setEmail(registerRequest.getEmail());
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String passwordEncoded = passwordEncoder.encode(registerRequest.getPassword());
        account.setPassword(passwordEncoded);
        accountRepository.save(account);

        MockHttpServletRequestBuilder request = createRequest(registerRequest);

        mockMvc.perform(request)
                .andExpectAll(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.error").value("Conflict"))
                .andExpect(jsonPath("$.message").value("email " + registerRequest.getEmail() + " duplicate"));
    }

    @Test
    @DisplayName("register success")
    public void testRegister_givenRegisterRequest_whenSaveSuccess_thenReturnRegisterResponse() throws Exception {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setEmail("test@mail.com");
        registerRequest.setPassword("secret");
        registerRequest.setConfirmPassword("secret");

        MockHttpServletRequestBuilder request = createRequest(registerRequest);

        mockMvc.perform(request)
                .andExpectAll(status().isCreated())
                .andExpect(jsonPath("$.email").value(registerRequest.getEmail()));
    }

    @Test
    @DisplayName("login email null")
    public void testLogin_givenEmail_whenNull_thenReturnBadRequest() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(null);

        MockHttpServletRequestBuilder request = createRequest(loginRequest);

        mockMvc.perform(request)
                .andExpectAll(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("email must not null"));
    }

    @Test
    @DisplayName("login email blank")
    public void testLogin_givenEmail_whenBlank_thenReturnBadRequest() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("");

        MockHttpServletRequestBuilder request = createRequest(loginRequest);

        mockMvc.perform(request)
                .andExpectAll(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("email must not blank"));
    }

    @Test
    @DisplayName("login email invalid format")
    public void testLogin_givenEmail_whenInvalidFormat_thenReturnBadRequest() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test");

        MockHttpServletRequestBuilder request = createRequest(loginRequest);

        mockMvc.perform(request)
                .andExpectAll(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("email invalid format"));
    }

    @Test
    @DisplayName("login password null")
    public void testLogin_givenPassword_whenNull_thenReturnBadRequest() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@mail.com");
        loginRequest.setPassword(null);

        MockHttpServletRequestBuilder request = createRequest(loginRequest);

        mockMvc.perform(request)
                .andExpectAll(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("password must not null"));
    }

    @Test
    @DisplayName("login password blank")
    public void testLogin_givenPassword_whenBlank_thenReturnBadRequest() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@mail.com");
        loginRequest.setPassword("");

        MockHttpServletRequestBuilder request = createRequest(loginRequest);

        mockMvc.perform(request)
                .andExpectAll(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("password must not blank"));
    }

    @Test
    @DisplayName("login email not found")
    public void testLogin_givenEmail_whenNotFound_thenReturnUnauthorized() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("unknow@mail.com");
        loginRequest.setPassword("random");

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String passwordEncoded = passwordEncoder.encode("secret");
        Account account = new Account();
        account.setEmail("test@mail.com");
        account.setPassword(passwordEncoded);
        accountRepository.save(account);

        MockHttpServletRequestBuilder request = createRequest(loginRequest);

        mockMvc.perform(request)
                .andExpectAll(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401))
                .andExpect(jsonPath("$.error").value("Unauthorized"))
                .andExpect(jsonPath("$.message").value("email or password invalid"));
    }

    @Test
    @DisplayName("login password not match")
    public void testLogin_givenPassword_whenNotMatch_thenReturnUnauthorized() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@mail.com");
        loginRequest.setPassword("random");

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String passwordEncoded = passwordEncoder.encode("secret");
        Account account = new Account();
        account.setEmail(loginRequest.getEmail());
        account.setPassword(passwordEncoded);
        accountRepository.save(account);

        MockHttpServletRequestBuilder request = createRequest(loginRequest);

        mockMvc.perform(request)
                .andExpectAll(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401))
                .andExpect(jsonPath("$.error").value("Unauthorized"))
                .andExpect(jsonPath("$.message").value("email or password invalid"));
    }

    @Test
    @DisplayName("login success")
    public void testLogin_givenLoginRequest_whenSuccess_thenReturnLoginResponse() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@mail.com");
        loginRequest.setPassword("secret");

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String passwordEncoded = passwordEncoder.encode(loginRequest.getPassword());
        Account account = new Account();
        account.setEmail(loginRequest.getEmail());
        account.setPassword(passwordEncoded);
        accountRepository.save(account);

        MockHttpServletRequestBuilder request = createRequest(loginRequest);

        mockMvc.perform(request)
                .andExpectAll(status().isOk())
                .andExpect(jsonPath("$.email").value(account.getEmail()));
    }

    private MockHttpServletRequestBuilder createRequest(RegisterRequest registerRequest) throws Exception {
        String json = mapper.writeValueAsString(registerRequest);
        return MockMvcRequestBuilders
                .post("/api/account/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);
    }

    private MockHttpServletRequestBuilder createRequest(LoginRequest loginRequest) throws Exception {
        String json = mapper.writeValueAsString(loginRequest);
        return MockMvcRequestBuilders
                .post("/api/account/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);
    }

}