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
     * Requests the currently logged in user from the {@link Session}.
     *
     * @return the user that is currently logged in
     */
    public User getCurrentUser() {
        return session.getUser();
    }

    /**
     * Adds a new {@link Rating} to the current user, then updates the
     * current user in the {@link UserRepository}.
     *
     * @param rating the new rating
     */
    public void send(Rating rating) throws RequestHandlerException {
        User user = getCurrentUser();
        user.addRating(rating);
        new UserRepository().updateEntity(user);
    }

    /**
     * Adds each {@link Rating} to the current user, then updates the
     * current user in the {@link UserRepository}.
     *
     * @param ratings the new ratings
     */
    public void send(Rating... ratings) throws RequestHandlerException {

        for (Rating rating : ratings) {
            send(rating);
        }

    }
}
