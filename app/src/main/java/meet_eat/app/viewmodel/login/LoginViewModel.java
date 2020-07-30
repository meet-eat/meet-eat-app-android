package meet_eat.app.viewmodel.login;


import android.widget.Toast;

import androidx.lifecycle.ViewModel;

import meet_eat.app.repository.RequestHandlerException;
import meet_eat.app.repository.Session;
import meet_eat.app.repository.UserRepository;
import meet_eat.data.LoginCredential;
import meet_eat.data.entity.user.Email;
import meet_eat.data.entity.user.Password;

/**
 * Manages login-related information.
 */
public class LoginViewModel extends ViewModel {

    private final UserRepository userRepository = new UserRepository();
    private final Session session = Session.getInstance();

    /**
     * Check the parameters for semantic correctness
     * and sends a login request to the
     * {@link meet_eat.app.repository.UserRepository UserRepository}.
     *
     * @param emailString    The email address of the user.
     * @param passwordString The password of the user.
     */
    public void login(String emailString, String passwordString) throws RequestHandlerException {
        // TODO Exception handling for non-runtime exceptions
        final Email email = new Email(emailString);
        final Password password = Password.createHashedPassword(passwordString);
        final LoginCredential credential = new LoginCredential(email, password);

        session.login(credential);
    }

    /**
     * Check the parameters for semantic correctness
     * and sends a password reset request to the
     * {@link meet_eat.app.repository.UserRepository UserRepository}..
     *
     * @param emailString The email address of the user.
     */
    public void resetPassword(String emailString) throws RequestHandlerException {
        if (!Email.isLegalEmailAddress(emailString)) {
            return;
        }
        // TODO email as string?
        userRepository.resetPassword(emailString);
    }
}