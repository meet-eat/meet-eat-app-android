package meet_eat.app.viewmodel.login;


import androidx.lifecycle.ViewModel;

import meet_eat.app.repository.RequestHandlerException;
import meet_eat.app.repository.Session;
import meet_eat.app.repository.UserRepository;
import meet_eat.data.LoginCredential;
import meet_eat.data.entity.user.Email;
import meet_eat.data.entity.user.Password;

/**
 * Manages information on the login page.
 *
 * @see meet_eat.app.fragment.login.LoginFragment
 */
public class LoginViewModel extends ViewModel {

    private final UserRepository userRepository = new UserRepository();
    private final Session session = Session.getInstance();

    /**
     * Creates an {@link Email} and a {@link Password} object
     * and sends a login request to the
     * {@link UserRepository}.
     *
     * @param emailString    the email address of the user
     * @param passwordString the password of the user
     * @throws RequestHandlerException if an error occurs when requesting the repository
     */
    public void login(String emailString, String passwordString) throws RequestHandlerException {
        Email email = new Email(emailString);
        Password password = Password.createHashedPassword(passwordString);
        LoginCredential credential = new LoginCredential(email, password);
        session.login(credential);
    }

    /**
     * Creates an {@link Email} object and sends a
     * password reset request to the {@link UserRepository}.
     *
     * @param emailString the users email address
     */
    public void resetPassword(String emailString) throws RequestHandlerException {
        Email email = new Email(emailString);
        userRepository.resetPassword(email);
    }
}