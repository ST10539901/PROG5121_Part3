package TestClass;

import com.mycompany.chatapp.Login;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;

public class LoginTest {

    private Login validLogin;
    private Login invalidLogin;

    @BeforeEach
    public void setUp() {
        validLogin   = new Login("kyl_1", "Ch&&sec@ke99!");
        invalidLogin = new Login("kyle!!!!!!", "password");
    }

    //Username Tests 

    @Test
    public void testUsernameSuccessfullyFormatted() {
        assertTrue(validLogin.checkUserName(),
            "Username 'kyl_1' should pass: contains underscore and ≤5 chars.");
    }

    @Test
    public void testUsernameIncorrectlyFormatted() {
        assertFalse(invalidLogin.checkUserName(),
            "Username 'kyle!!!!!!' should fail: no underscore and >5 chars.");
    }

    //Password Tests 

    @Test
    public void testPasswordMeetsComplexityRequirements() {
        assertTrue(validLogin.checkPasswordComplexity(),
            "Password 'Ch&&sec@ke99!' should pass all complexity checks.");
    }

    @Test
    public void testPasswordDoesNotMeetComplexityRequirements() {
        assertFalse(invalidLogin.checkPasswordComplexity(),
            "Password 'password' should fail: no capital, no number, no special char.");
    }

    //Login Tests ─

    @Test
    public void testLoginSuccessful() {
        String result = validLogin.loginUser("kyl_1", "Ch&&sec@ke99!");
        assertEquals("Welcome kyl_1, it is great to see you again.", result,
            "Correct credentials should return welcome message.");
    }

    @Test
    public void testLoginFailed() {
        String result = validLogin.loginUser("wrongUser", "wrongPass");
        assertEquals("Username or password incorrect, please try again.", result,
            "Wrong credentials should return failure message.");
    }

    @Test
    public void testIsLoggedInAfterSuccessfulLogin() {
        validLogin.loginUser("kyl_1", "Ch&&sec@ke99!");
        assertTrue(validLogin.isLoggedIn(), "User should be logged in after successful login.");
    }

    @Test
    public void testIsNotLoggedInAfterFailedLogin() {
        validLogin.loginUser("wrongUser", "wrongPass");
        assertFalse(validLogin.isLoggedIn(), "User should not be logged in after failed login.");
    }

    //Registration Message Tests 

    @Test
    public void testRegisterUserSuccessMessages() {
        String result = validLogin.registerUser();
        assertTrue(result.contains("Username successfully captured."),
            "Valid username should produce success message.");
        assertTrue(result.contains("Password successfully captured."),
            "Valid password should produce success message.");
    }

    @Test
    public void testRegisterUserFailureMessages() {
        String result = invalidLogin.registerUser();
        assertTrue(result.contains("Username is not correctly formatted"),
            "Invalid username should produce formatted error message.");
        assertTrue(result.contains("Password is not correctly formatted"),
            "Invalid password should produce formatted error message.");
    }
}
