package com.example.authentication.validate;

import com.example.authentication.exception.BadRequestException;
import com.example.authentication.request.RegisterRequest;
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
class RegisterValidateTest {

    @InjectMocks
    private RegisterValidate registerValidate;

    @Test
    @DisplayName("validate email null")
    void testValidate_givenEmail_whenNull_thenThrowBadRequestException() {
        RegisterRequest request = new RegisterRequest();
        request.setEmail(null);

        BadRequestException actual = assertThrows(
                BadRequestException.class,
                () -> registerValidate.validate(request)
        );

        assertEquals(HttpStatus.BAD_REQUEST, actual.getStatusCode());
        assertEquals("email must not null", actual.getReason());
    }

    @Test
    @DisplayName("validate email blank")
    void testValidate_givenEmail_whenBlank_thenThrowBadRequestException() {
        RegisterRequest request = new RegisterRequest();
        request.setEmail(" ");

        BadRequestException actual = assertThrows(
                BadRequestException.class,
                () -> registerValidate.validate(request)
        );

        assertEquals(HttpStatus.BAD_REQUEST, actual.getStatusCode());
        assertEquals("email must not blank", actual.getReason());
    }

    @Test
    @DisplayName("validate email invalid format")
    void testValidate_givenEmail_whenInvalidFormat_thenThrowBadRequestException() {
        RegisterRequest request = new RegisterRequest();
        request.setEmail("user_test");

        BadRequestException actual = assertThrows(
                BadRequestException.class,
                () -> registerValidate.validate(request)
        );

        assertEquals(HttpStatus.BAD_REQUEST, actual.getStatusCode());
        assertEquals("email invalid format", actual.getReason());
    }

    @Test
    @DisplayName("validate password null")
    void testValidate_givenPassword_whenNull_thenThrowBadRequestException() {
        RegisterRequest request = new RegisterRequest();
        request.setEmail("test@mail.co.th");
        request.setPassword(null);

        BadRequestException actual = assertThrows(
                BadRequestException.class,
                () -> registerValidate.validate(request)
        );

        assertEquals(HttpStatus.BAD_REQUEST, actual.getStatusCode());
        assertEquals("password must not null", actual.getReason());
    }

    @Test
    @DisplayName("validate password blank")
    void testValidate_givenPassword_whenBlank_thenThrowBadRequestException() {
        RegisterRequest request = new RegisterRequest();
        request.setEmail("test@mail.co.th");
        request.setPassword(" ");

        BadRequestException actual = assertThrows(
                BadRequestException.class,
                () -> registerValidate.validate(request)
        );

        assertEquals(HttpStatus.BAD_REQUEST, actual.getStatusCode());
        assertEquals("password must not blank", actual.getReason());
    }

    @Test
    @DisplayName("validate confirm password null")
    void testValidate_givenConfirmPassword_whenNull_thenThrowBadRequestException() {
        RegisterRequest request = new RegisterRequest();
        request.setEmail("test@mail.co.th");
        request.setPassword("secret");
        request.setConfirmPassword(null);

        BadRequestException actual = assertThrows(
                BadRequestException.class,
                () -> registerValidate.validate(request)
        );

        assertEquals(HttpStatus.BAD_REQUEST, actual.getStatusCode());
        assertEquals("confirm password must not null", actual.getReason());
    }

    @Test
    @DisplayName("validate confirm password blank")
    void testValidate_givenConfirmPassword_whenBlank_thenThrowBadRequestException() {
        RegisterRequest request = new RegisterRequest();
        request.setEmail("test@mail.co.th");
        request.setPassword("secret");
        request.setConfirmPassword(" ");

        BadRequestException actual = assertThrows(
                BadRequestException.class,
                () -> registerValidate.validate(request)
        );

        assertEquals(HttpStatus.BAD_REQUEST, actual.getStatusCode());
        assertEquals("confirm password must not blank", actual.getReason());
    }

    @Test
    @DisplayName("validate password not match")
    void testValidate_givenPasswordAndConfirmPassword_whenNotMatch_thenThrowBadRequestException() {
        RegisterRequest request = new RegisterRequest();
        request.setEmail("test@mail.co.th");
        request.setPassword("secret");
        request.setConfirmPassword("Secret");

        BadRequestException actual = assertThrows(
                BadRequestException.class,
                () -> registerValidate.validate(request)
        );

        assertEquals(HttpStatus.BAD_REQUEST, actual.getStatusCode());
        assertEquals("password not match", actual.getReason());
    }

    @Test
    @DisplayName("validate success")
    void testValidate_givenRegisterRequest_whenSuccess_thenDoesNotThrow() {
        RegisterRequest request = new RegisterRequest();
        request.setEmail("test@mail.co.th");
        request.setPassword("secret");
        request.setConfirmPassword("secret");

        assertDoesNotThrow(() -> registerValidate.validate(request));
    }

}