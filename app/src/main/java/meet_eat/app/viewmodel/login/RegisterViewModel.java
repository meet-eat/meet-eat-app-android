package meet_eat.app.viewmodel.login;

import androidx.lifecycle.ViewModel;

import meet_eat.app.repository.RequestHandlerException;
import meet_eat.app.repository.UserRepository;
import meet_eat.data.entity.user.User;

/**
 * Manages the registration information of the
 * {@link meet_eat.app.fragment.login.RegisterFragment RegisterFragment}.
 */
public class RegisterViewModel extends ViewModel {

    private final UserRepository userRepository = new UserRepository();

    /**
     * Sends a user creation request to the
     * {@link meet_eat.app.repository.UserRepository UserRepository}.
     *
     * @param user The user that is to be registered
     */
    public void register(User user) throws RequestHandlerException {
        userRepository.addEntity(user);
    }
}