package meet_eat.app.viewmodel.login;


import androidx.lifecycle.ViewModel;

import meet_eat.app.repository.Session;
import meet_eat.app.repository.UserRepository;
import meet_eat.data.LoginCredential;
import meet_eat.data.entity.user.Email;
import meet_eat.data.entity.user.Password;

/**
 * Manages login-related information.
 */
public class LoginViewModel extends ViewModel {

    /**
     * Check the parameters for semantic correctness
     * and sends a login request to the
     * {@link meet_eat.app.repository.UserRepository UserRepository}.
     *
     * @param emailString The email address of the user.
     * @param passwordString The password of the user.
     */
    public void login(String emailString, String passwordString) throws IllegalArgumentException {
        Email email = new Email(emailString);
        Password password = new Password(passwordString);
        LoginCredential credential = new LoginCredential(email, password);
        //TODO Session login
    }

    /**
     * Check the parameters for semantic correctness
     * and sends a password reset request to the
     * {@link meet_eat.app.repository.UserRepository UserRepository}..
     *
     * @param emailString The email address of the user.
     */
    public void resetPassword(String emailString) throws IllegalArgumentException {
        Email email = new Email(emailString);
        //TODO UserRepository resetPassword
    }
}
