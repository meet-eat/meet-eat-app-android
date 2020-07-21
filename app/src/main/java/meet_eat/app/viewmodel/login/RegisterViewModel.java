package meet_eat.app.viewmodel.login;

import androidx.lifecycle.ViewModel;

import meet_eat.data.entity.user.User;

/**
 * Manages the registration information of the
 * {@link meet_eat.app.fragment.login.RegisterFragment RegisterFragment}.
 */
public class RegisterViewModel extends ViewModel {

    /**
     * Sends a user creation request to the
     * {@link meet_eat.app.repository.UserRepository UserRepository}.
     *
     * @param user The user that is to be registered
     */
    public void register(User user) {
        /* TODO UserRepository register
        UserRepository.addEntity(user);
         */
    }
}