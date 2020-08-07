package meet_eat.app.viewmodel.login;

import org.junit.BeforeClass;
import org.junit.Test;

import meet_eat.app.repository.RequestHandlerException;

public class RegisterViewModelTest {

    private static RegisterViewModel registerVM;

    @BeforeClass
    public static void initialize() {
        registerVM = new RegisterViewModel();
    }

    // Registering a new valid user is tested in LoginViewModelTest

    @Test(expected = NullPointerException.class)
    public void testRegisterWithNullUser() throws RequestHandlerException {
        registerVM.register(null);
    }
}