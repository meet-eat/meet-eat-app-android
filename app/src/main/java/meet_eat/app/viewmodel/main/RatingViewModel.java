package meet_eat.app.viewmodel.main;

import androidx.lifecycle.ViewModel;

import meet_eat.app.repository.RequestHandlerException;
import meet_eat.app.repository.Session;
import meet_eat.app.repository.UserRepository;
import meet_eat.data.entity.user.User;
import meet_eat.data.entity.user.rating.Rating;

/**
 * Manages rating-related information.
 */
public class RatingViewModel extends ViewModel {

    private final Session session = Session.getInstance();

    /**
     * Requests the object of the user currently logged in to the device from the
     * {@link meet_eat.app.repository.Session Session}.
     *
     * @return The current user.
     */
    public User getCurrentUser() {
        return session.getUser();
    }

    /**
     * Sends a request to the
     * {@link meet_eat.app.repository.UserRepository UserRepository} to add a new rating.
     *
     * @param rating The new rating.
     */
    public void send(Rating rating) throws RequestHandlerException {
        User user = session.getUser();
        user.addRating(rating);
        new UserRepository().updateEntity(user);
    }

    /**
     * Sends multiple requests to the
     * {@link meet_eat.app.repository.UserRepository UserRepository} to add new ratings.
     *
     * @param ratings The new ratings.
     */
    public void send(Rating... ratings) throws RequestHandlerException {

        for (Rating rating : ratings) {
            send(rating);
        }

    }
}
