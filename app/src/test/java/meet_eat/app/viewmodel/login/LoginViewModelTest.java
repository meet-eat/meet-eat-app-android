package meet_eat.app.viewmodel.login;

import org.junit.BeforeClass;
import org.junit.Test;

import meet_eat.app.repository.RequestHandlerException;

public class LoginViewModelTest {
    @BeforeClass
    public static void initialize() {}

    @Test
    public void testLoginWithNull() throws RequestHandlerException {
        new LoginViewModel().login(null, null);
    }

    @Test
    public void testResetPasswordWithNull() throws RequestHandlerException {
        new LoginViewModel().resetPassword(null);
    }
}
