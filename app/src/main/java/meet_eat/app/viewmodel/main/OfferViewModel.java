package meet_eat.app.viewmodel.main;

import androidx.lifecycle.ViewModel;

import com.google.common.collect.Sets;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

import meet_eat.app.repository.OfferRepository;
import meet_eat.app.repository.RequestHandlerException;
import meet_eat.app.repository.Session;
import meet_eat.app.repository.TagRepository;
import meet_eat.app.repository.UserRepository;
import meet_eat.data.Page;
import meet_eat.data.Report;
import meet_eat.data.entity.Offer;
import meet_eat.data.entity.Tag;
import meet_eat.data.entity.user.User;
import meet_eat.data.entity.user.contact.ContactData;
import meet_eat.data.entity.user.contact.ContactRequest;
import meet_eat.data.predicate.OfferPredicate;

/**
 * Manages offer-related information.
 */
public class OfferViewModel extends ViewModel {

    private static final int ZERO = 0;
    private static final int MAX_PAGE_SIZE = 1000;
    private static final Page PAGE = new Page(ZERO, MAX_PAGE_SIZE);

    private final OfferRepository offerRepository = new OfferRepository();
    private final UserRepository userRepository = new UserRepository();
    private final TagRepository tagRepository = new TagRepository();
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
     * Gets the specified user's offers.
     *
     * @param user the user whose offers should be fetched
     * @return the specified user's offers
     * @throws RequestHandlerException if an error occurs when requesting the repository
     */
    public Iterable<Offer> fetchOffers(User user) throws RequestHandlerException {
        return offerRepository.getOffersByCreator(user, PAGE, user.getOfferPredicates(), user.getOfferComparator());
    }

    /**
     * Gets offers based on the given predicates, together with the already
     * stored predicates.
     *
     * @param predicates the predicates to filter the offers
     * @return an {@link Iterable} containing the updated offers
     * @throws RequestHandlerException if an error occurs when requesting the repository
     * @see OfferRepository
     */
    public Iterable<Offer> fetchOffers(Collection<OfferPredicate> predicates) throws RequestHandlerException {
        getCurrentUser().addManyOfferPredicates(predicates);
        return offerRepository
                .getOffers(PAGE, getCurrentUser().getOfferPredicates(), getCurrentUser().getOfferComparator());
    }

    /**
     * Gets the user's bookmarked offers.
     *
     * @return the user's bookmarked offers
     */
    public Iterable<Offer> fetchBookmarkedOffers() {
        return getCurrentUser().getBookmarks();
    }

    /**
     * Gets the offers of the subscribed users.
     *
     * @return the offers of the subscribed users
     * @throws RequestHandlerException if an error occurs when requesting the repository
     */
    public Iterable<Offer> fetchOffersOfSubscriptions() throws RequestHandlerException {
        return offerRepository.getOffersBySubscriptions(getCurrentUser(), PAGE, getCurrentUser().getOfferPredicates(),
                getCurrentUser().getOfferComparator());
    }

    /**
     * Overwrites the user's predicates, then updates the user entity.
     *
     * @param predicates the predicates that are to be updated
     * @throws RequestHandlerException if an error occurs when requesting the repository
     */
    public User updatePredicates(Collection<OfferPredicate> predicates) throws RequestHandlerException {
        getCurrentUser().clearOfferPredicates();
        getCurrentUser().addManyOfferPredicates(predicates);
        return userRepository.updateEntity(getCurrentUser());
    }

    /**
     * Adds an {@link Offer} entity to the {@link OfferRepository}.
     *
     * @param offer the new offer
     * @throws RequestHandlerException if an error occurs when requesting the repository
     */
    public void add(Offer offer) throws RequestHandlerException {
        offerRepository.addEntity(offer);
    }

    /**
     * Removes an {@link Offer} entity from the {@link OfferRepository}.
     *
     * @param offer the offer to be removed
     * @throws RequestHandlerException if an error occurs when requesting the repository
     */
    public void delete(Offer offer) throws RequestHandlerException {
        offerRepository.deleteEntity(offer);
    }

    /**
     * Updates an edited {@link Offer} entity in the {@link OfferRepository}.
     *
     * @param offer the modified offer to be updated
     * @throws RequestHandlerException if an error occurs when requesting the repository
     */
    public void edit(Offer offer) throws RequestHandlerException {
        offerRepository.updateEntity(offer);
    }

    /**
     * Adds the current user to an offers participant list,
     * then updates the {@link Offer} entity in the {@link OfferRepository}.
     *
     * @param offer the offer wherein the current user is to be added
     * @return the updated offer
     * @throws RequestHandlerException if an error occurs when requesting the repository
     */
    public Offer participate(Offer offer) throws RequestHandlerException {
        removeBookmark(offer);
        return offerRepository.addParticipant(offer, getCurrentUser());
    }

    /**
     * Removes the current user from an offers participant list,
     * then updates the {@link Offer} entity in the {@link OfferRepository}.
     *
     * @param participant the participant that is to be removed
     * @param offer       the offer whereof the participant is to be removed
     * @return the updated offer
     * @throws RequestHandlerException if an error occurs when requesting the repository
     */
    public Offer cancelParticipation(User participant, Offer offer) throws RequestHandlerException {
        return offerRepository.removeParticipant(offer, participant);
    }

    /**
     * Sends a {@link ContactRequest} to the {@link UserRepository}.
     *
     * @param request the contact request
     */
    public void requestContact(ContactRequest request) {
        userRepository.requestContact(request);
    }

    /**
     * Sends a request with the contact data to be given to the requester
     * to the {@link UserRepository}.
     *
     * @param contact the contact data that is to be sent
     */
    public void sendContact(ContactData contact) {
        userRepository.sendContactData(contact);
    }

    /**
     * Sends a report request to the
     * {@link UserRepository}.
     *
     * @param user   the user who is to be reported
     * @param report the report sent to the {@link UserRepository}
     * @throws RequestHandlerException if an error occurs when requesting the repository
     */
    public void report(User user, Report report) throws RequestHandlerException {
        userRepository.report(user, report);
    }

    /**
     * Adds an {@link Offer} to the current user's bookmarks,
     * then sends a user update request to the {@link UserRepository}.
     *
     * @param offer the offer to be added to the users bookmarks
     * @throws RequestHandlerException if an error occurs when requesting the repository
     */
    public void addBookmark(Offer offer) throws RequestHandlerException {
        if (!isBookmarked(offer)) {
            getCurrentUser().addBookmark(offer);
            userRepository.updateEntity(getCurrentUser());
        }
    }

    /**
     * Removes an {@link Offer} from the current user's bookmarks, then sends a user update request to the
     * {@link UserRepository}.
     *
     * @param offer The offer to be added to the users bookmarks.
     * @throws RequestHandlerException if an error occurs when requesting the repository
     */
    public void removeBookmark(Offer offer) throws RequestHandlerException {
        if (isBookmarked(offer)) {
            getCurrentUser().removeBookmark(offer);
            userRepository.updateEntity(getCurrentUser());
        }
    }

    /**
     * Checks if the given {@link Offer} is already bookmarked by the current user.
     *
     * @param offer the offer to be checked
     * @return true if the offer is already bookmarked
     */
    public boolean isBookmarked(Offer offer) {
        Stream<Offer> bookmarks = getCurrentUser().getBookmarks().stream();
        String offerIdentifier = offer.getIdentifier();
        return bookmarks.anyMatch(bookmarkedOffer -> bookmarkedOffer.getIdentifier().equals(offerIdentifier));
    }

    /**
     * Checks if the current user is participating in the given {@link Offer}.
     *
     * @param offer the offer to be checked
     * @return true if the current user is participating
     */
    public boolean isParticipating(Offer offer) {
        Set<User> participantsWithNull = offer.getParticipants();
        Set<User> participantsWithoutNull = Sets.newHashSet();

        for (User participantOrNull : participantsWithNull) {
            if (Objects.nonNull(participantOrNull)) {
                participantsWithoutNull.add(participantOrNull);
            }
        }

        Stream<User> participants = participantsWithoutNull.stream();
        String userIdentifier = getCurrentUser().getIdentifier();
        return participants.anyMatch(participant -> participant.getIdentifier().equals(userIdentifier));
    }

    /**
     * Checks if the current user is the creator of the given {@link Offer}.
     *
     * @param offer the offer to be checked
     * @return true if the user is the creator
     */
    public boolean isCreator(Offer offer) {
        return getCurrentUser().getIdentifier().equals(offer.getCreator().getIdentifier());
    }

    /**
     * Requests every existing tag off of the- {@link TagRepository}.
     *
     * @return An {@link Iterable} containing the tags.
     * @throws RequestHandlerException if an error occurs when requesting the repository
     */
    public Iterable<Tag> getAllTags() throws RequestHandlerException {
        return tagRepository.getTags();
    }
}
