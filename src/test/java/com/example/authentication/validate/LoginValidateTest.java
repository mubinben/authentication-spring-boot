package com.example.authentication.validate;

import com.example.authentication.exception.BadRequestException;
import com.example.authentication.request.LoginRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@ExtendWith(MockitoExtension.class)
class LoginValidateTest {

    @InjectMocks
    private LoginValidate loginValidate;

    @Test
    @DisplayName("validate email null")
    public void testValidate_givenEmail_whenNull_thenThrowBadRequestException() {
        LoginRequest request = new LoginRequest();
        request.setEmail(null);

        BadRequestException actual = assertThrows(
                BadRequestException.class,
                () -> loginValidate.validate(request)
        );

        assertEquals(HttpStatus.BAD_REQUEST, actual.getStatusCode());
        assertEquals("email must not null", actual.getReason());
    }

    @Test
    @DisplayName("validate email blank")
    public void testValidate_givenEmail_whenBlank_thenThrowBadRequestException() {
        LoginRequest request = new LoginRequest();
        request.setEmail("");

        BadRequestException actual = assertThrows(
                BadRequestException.class,
                () -> loginValidate.validate(request)
        );

        assertEquals(HttpStatus.BAD_REQUEST, actual.getStatusCode());
        assertEquals("email must not blank", actual.getReason());
    }

    @Test
    @DisplayName("validate email invalid format")
    public void testValidate_givenEmail_whenInvalidFormat_thenThrowBadRequestException() {
        LoginRequest request = new LoginRequest();
        request.setEmail("test");

        BadRequestException actual = assertThrows(
                BadRequestException.class,
                () -> loginValidate.validate(request)
        );

        assertEquals(HttpStatus.BAD_REQUEST, actual.getStatusCode());
        assertEquals("email invalid format", actual.getReason());
    }

    @Test
    @DisplayName("validate password null")
    public void testValidate_givenPassword_whenNull_thenThrowBadRequestException() {
        LoginRequest request = new LoginRequest();
        request.setEmail("test@mail.com");
        request.setPassword(null);

        BadRequestException actual = assertThrows(
                BadRequestException.class,
                () -> loginValidate.validate(request)
        );

        assertEquals(HttpStatus.BAD_REQUEST, actual.getStatusCode());
        assertEquals("password must not null", actual.getReason());
    }

    @Test
    @DisplayName("validate password blank")
    public void testValidate_givenPassword_whenBlank_thenThrowBadRequestException() {
        LoginRequest request = new LoginRequest();
        request.setEmail("test@mail.com");
        request.setPassword("");

        BadRequestException actual = assertThrows(
                BadRequestException.class,
                () -> loginValidate.validate(request)
        );

        assertEquals(HttpStatus.BAD_REQUEST, actual.getStatusCode());
        assertEquals("password must not blank", actual.getReason());
    }

    @Test
    @DisplayName("validate success")
    public void testValidate_givenLoginRequest_whenSuccess_thenDoesNotThrow() {
        LoginRequest request = new LoginRequest();
        request.setEmail("test@mail.com");
        request.setPassword("secret");

        assertDoesNotThrow(() -> loginValidate.validate(request));
    }

}