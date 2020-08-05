package meet_eat.app.viewmodel.main;

import androidx.lifecycle.ViewModel;

import com.google.common.collect.Lists;

import java.util.Collection;
import java.util.Comparator;

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

    public static final int PAGE_SIZE = 50;
    public static final int PAGE_INDEX = 0;

    private final OfferRepository offerRepository = new OfferRepository();
    private final UserRepository userRepository = new UserRepository();
    private final TagRepository tagRepository = new TagRepository();
    private final Session session = Session.getInstance();
    private final Page page = new Page(PAGE_INDEX, PAGE_SIZE);

    /**
     * Requests the object of the user currently logged in to the device from the
     * {@link meet_eat.app.repository.Session Session}.
     *
     * @return The current user.
     */
    public User getCurrentUser() {
        return session.getUser();
    }

    public Iterable<Offer> fetchSubscribedOffers() throws RequestHandlerException {
        // TODO sort + return offerRepository.getSubscribedOffers(getCurrentUser());
        return null;
    }

    public Iterable<Offer> fetchBookmarkedOffers() throws RequestHandlerException {
        // TODO sort + return getCurrentUser().getBookmarks();
        return null;
    }

    public Iterable<Offer> fetchOffers(User user) throws RequestHandlerException {
        // TODO sort with comparators
        return offerRepository.getOffersByCreatorId(user.getIdentifier());
    }

    /**
     * Sends an offer update request to the
     * {@link meet_eat.app.repository.OfferRepository OfferRepository}.
     *
     * @return A /LIST/ containing the updated offers.
     */
    public Iterable<Offer> fetchOffers(Collection<OfferPredicate> predicates) throws RequestHandlerException {
        // TODO sort with comparators
        predicates.addAll(getCurrentUser().getOfferPredicates());
        return offerRepository.getOffers(page, predicates, null);
    }

    /**
     * Sends a predicate update request to the
     * {@link meet_eat.app.repository.UserRepository UserRepository}.
     * Predicates are used to filter offers.
     *
     * @param predicates The predicates that are to be updated.
     */
    public void updatePredicates(Collection<OfferPredicate> predicates) throws RequestHandlerException {
        getCurrentUser().clearOfferPredicates();
        getCurrentUser().addManyOfferPredicates(predicates);
        userRepository.updateEntity(getCurrentUser());
    }

    /**
     * Requests the predicates currently stored in the
     * {@link meet_eat.app.repository.UserRepository UserRepository}.
     *
     * @return A /LIST/ containing the predicates.
     */
    public Iterable<OfferPredicate> getPredicates() {
        return session.getUser().getOfferPredicates();
    }

    /**
     * Sends a request to the {@link meet_eat.app.repository.OfferRepository OfferRepository} to
     * add a new offer.
     *
     * @param offer The offer to be added.
     */
    public void add(Offer offer) throws RequestHandlerException {
        offerRepository.addEntity(offer);
    }

    /**
     * Sends a deletion request to the
     * {@link meet_eat.app.repository.OfferRepository OfferRepository}.
     *
     * @param offer The offer to be deleted.
     */
    public void delete(Offer offer) throws RequestHandlerException {
        offerRepository.deleteEntity(offer);
    }

    /**
     * Sends an update request for an existing offer to the
     * {@link meet_eat.app.repository.OfferRepository OfferRepository}.
     *
     * @param offer The offer that was modified.
     */
    public void edit(Offer offer) throws RequestHandlerException {
        offerRepository.updateEntity(offer);
    }

    /**
     * Adds the current user to an offer, then sends an offer update request to the
     * {@link meet_eat.app.repository.OfferRepository OfferRepository}.
     *
     * @param offer The offer, wherein the current user is to be added.
     */
    public void participate(Offer offer) throws RequestHandlerException {
        offer.addParticipant(session.getUser());
        offerRepository.updateEntity(offer);
    }

    /**
     * Sends a request to the {@link meet_eat.app.repository.OfferRepository OfferRepository}
     * to update an offer after a {@param participant} was removed from it.
     *
     * @param participant The participant that is to be removed.
     * @param offer       The offer, whereof the participant is to be removed.
     */
    public void cancelParticipation(User participant, Offer offer) throws RequestHandlerException {
        offer.removeParticipant(participant);
        offerRepository.updateEntity(offer);
    }

    /**
     * Sends a "{@link ContactRequest ContactRequest}" request to the
     * {@link meet_eat.app.repository.UserRepository UserRepository}.
     *
     * @param request The contact request.
     */
    public void requestContact(ContactRequest request) {
        userRepository.requestContact(request);
    }

    /**
     * Sends a request including consent and the contact data to be given to the requester
     * to the {@link meet_eat.app.repository.UserRepository UserRepository}.
     *
     * @param contact The contact data that is to be sent.
     */
    public void sendContact(ContactData contact) {
        userRepository.sendContactData(contact);
    }

    /**
     * Sends a report request to the
     * {@link meet_eat.app.repository.UserRepository UserRepository}.
     *
     * @param user   The user that is to be reported.
     * @param report The report sent to the
     *               {@link meet_eat.app.repository.UserRepository UserRepository}.
     */
    public void report(User user, Report report) throws RequestHandlerException {
        userRepository.report(user, report);
    }

    /**
     * Sends a user update request to the
     * {@link meet_eat.app.repository.UserRepository UserRepository}.
     *
     * @param offer The offer to be added to the users /LIST/ of bookmarks.
     */
    public void addBookmark(Offer offer) throws RequestHandlerException {
        User currentUser = session.getUser();

        if (!isBookmarked(offer)) {
            currentUser.addBookmark(offer);
            userRepository.updateEntity(currentUser);
        }
    }

    /**
     * Sends a user update request to the
     * {@link meet_eat.app.repository.UserRepository UserRepository}.
     *
     * @param offer The offer to be added to the users /LIST/ of bookmarks.
     */
    public void removeBookmark(Offer offer) throws RequestHandlerException {
        User currentUser = session.getUser();

        if (isBookmarked(offer)) {
            currentUser.removeBookmark(offer);
            userRepository.updateEntity(currentUser);
        }
    }

    /**
     * Checks if offer is already bookmarked by user.
     *
     * @param offer The offer to be checked.
     * @return true if the offer is already bookmarked, else false.
     */
    public boolean isBookmarked(Offer offer) {
        return session.getUser().getBookmarks().contains(offer);
    }

    /**
     * Requests every existing tag off of the
     * {@link meet_eat.app.repository.TagRepository TagRepository}.
     *
     * @return A /LIST/ containing the tags.
     */
    public Iterable<Tag> getAllTags() throws RequestHandlerException {
        return tagRepository.getTags();
    }
}
