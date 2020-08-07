package meet_eat.app.viewmodel.login;

import androidx.lifecycle.ViewModel;

import meet_eat.app.repository.RequestHandlerException;
import meet_eat.app.repository.UserRepository;
import meet_eat.data.entity.user.User;

/**
 * Manages information on the registration page.
 *
 * @see meet_eat.app.fragment.login.RegisterFragment
 */
public class RegisterViewModel extends ViewModel {

    private final UserRepository userRepository = new UserRepository();

    /**
     * Sends a user creation request to the {@link UserRepository}.
     *
     * @param user the user to be registered
     */
    public void register(User user) throws RequestHandlerException {
        userRepository.addEntity(user);
    }
}