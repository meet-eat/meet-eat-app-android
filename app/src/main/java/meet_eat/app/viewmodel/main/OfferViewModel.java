package meet_eat.app.viewmodel.main;

import androidx.lifecycle.ViewModel;

import com.google.common.collect.Streams;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import meet_eat.app.repository.OfferRepository;
import meet_eat.app.repository.ReportRepository;
import meet_eat.app.repository.RequestHandlerException;
import meet_eat.app.repository.Session;
import meet_eat.app.repository.TagRepository;
import meet_eat.app.repository.UserRepository;
import meet_eat.data.Page;
import meet_eat.data.entity.Offer;
import meet_eat.data.entity.Tag;
import meet_eat.data.entity.relation.Bookmark;
import meet_eat.data.entity.relation.EntityRelation;
import meet_eat.data.entity.relation.Participation;
import meet_eat.data.entity.relation.Report;
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
    private final ReportRepository reportRepository = new ReportRepository();
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
     * Gets the offer specified by its identifier.
     *
     * @param identifier the identifier of the offer that is to be fetched
     * @return The offer with the specified Identifier
     * @throws RequestHandlerException if an error occurs when requesting the repository
     */
    public Offer fetchOfferById(String identifier) throws RequestHandlerException {
        return offerRepository.getEntityById(identifier);
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
    public Iterable<Offer> fetchBookmarkedOffers() throws RequestHandlerException {
        return Streams.stream(fetchBookmarks()).map(Bookmark::getTarget).collect(Collectors.toList());
    }

    /**
     * Gets the {@link User user's} {@link Bookmark bookmarks}.
     *
     * @return the user's bookmarks
     */
    public Iterable<Bookmark> fetchBookmarks() throws RequestHandlerException {
        return userRepository.getBookmarksByUser(getCurrentUser());
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
     * TODO
     * Adds the current user to an offers participant list,
     * then updates the {@link Offer} entity in the {@link OfferRepository}.
     *
     * @param offer the offer wherein the current user is to be added
     * @return the updated offer
     * @throws RequestHandlerException if an error occurs when requesting the repository
     */
    public Participation participate(Offer offer) throws RequestHandlerException {
        Participation participation = new Participation(getCurrentUser(), offer);
        return offerRepository.addParticipation(participation);
    }

    /**
     * TODO
     * Removes the current user from an offers participant list,
     * then updates the {@link Offer} entity in the {@link OfferRepository}.
     *
     * @param participant the participant that is to be removed
     * @param offer       the offer whereof the participant is to be removed
     * @throws RequestHandlerException if an error occurs when requesting the repository
     */
    public void cancelParticipation(User participant, Offer offer) throws RequestHandlerException {
        Stream<Participation> participations = Streams.stream(offerRepository.getParticipationsByOffer(offer));
        Optional<Participation> optionalParticipation =
                participations.filter(x -> x.getSource().equals(participant)).findFirst();
        if (optionalParticipation.isPresent()) {
            offerRepository.removeParticipation(optionalParticipation.get());
        }
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
     * Sends a report request to the {@link ReportRepository}.
     *
     * @param report the report sent to the {@link UserRepository}
     * @throws RequestHandlerException if an error occurs when requesting the repository
     */
    public Report report(Report report) throws RequestHandlerException {
        return reportRepository.addEntity(report);
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
            Bookmark bookmark = new Bookmark(getCurrentUser(), Objects.requireNonNull(offer));
            userRepository.addBookmark(bookmark);
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
        Optional<Bookmark> optionalBookmark =
                Streams.stream(fetchBookmarks()).filter(x -> x.getTarget().equals(offer)).findFirst();

        if (optionalBookmark.isPresent()) {
            userRepository.removeBookmark(optionalBookmark.get());
        }
    }

    /**
     * Checks if the given {@link Offer} is already bookmarked by the current user.
     *
     * @param offer the offer to be checked
     * @return true if the offer is already bookmarked
     * @throws RequestHandlerException if an error occurs when requesting the repository
     */
    public boolean isBookmarked(Offer offer) throws RequestHandlerException {
        Stream<Offer> bookmarkedOffers = Streams.stream(fetchBookmarkedOffers());
        return bookmarkedOffers
                .anyMatch(bookmarkedOffer -> bookmarkedOffer.getIdentifier().equals(offer.getIdentifier()));
    }

    /**
     * Checks if the current user is participating in the given {@link Offer}.
     *
     * @param offer the offer to be checked
     * @return true if the current user is participating
     */
    public boolean isParticipating(Offer offer) throws RequestHandlerException {
        Stream<User> participants = getParticipants(offer).stream();
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

    /**
     * Returns the participants for the given offer.
     *
     * @param offer the offer whose participants are to be returned
     * @return The participants of the given offerr
     * @throws RequestHandlerException if an error occurs when requesting the repository
     */
    public Set<User> getParticipants(Offer offer) throws RequestHandlerException {
        return Streams.stream(offerRepository.getParticipationsByOffer(offer)).map(EntityRelation::getSource)
                .collect(Collectors.toSet());
    }

}
