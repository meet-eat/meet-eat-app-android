package meet_eat.app.viewmodel.main;

import androidx.lifecycle.ViewModel;

import meet_eat.app.repository.RequestHandlerException;
import meet_eat.app.repository.Session;
import meet_eat.app.repository.UserRepository;
import meet_eat.data.entity.relation.rating.Rating;
import meet_eat.data.entity.user.User;

/**
 * Manages rating-related information.
 */
public class RatingViewModel extends ViewModel {

    private final Session session = Session.getInstance();
    private final UserRepository userRepository = new UserRepository();

    /**
     * Requests the currently logged in user from the {@link Session}.
     *
     * @return the user that is currently logged in
     */
    public User getCurrentUser() {
        return session.getUser();
    }

    /**
     * Adds a new {@link Rating} to the UserRepository.
     *
     * @param rating the new rating
     */
    public void send(Rating rating) throws RequestHandlerException {
        userRepository.addRating(rating);
    }

    /**
     * Adds each {@link Rating} to the UserRepository.
     *
     * @param ratings the new ratings
     */
    public void send(Rating... ratings) throws RequestHandlerException {
        for (Rating rating : ratings) {
            send(rating);
        }
    }
}
