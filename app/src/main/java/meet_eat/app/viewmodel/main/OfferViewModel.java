package meet_eat.app.viewmodel.main;

import java.util.Comparator;
import java.util.function.Predicate;

import meet_eat.data.Report;
import meet_eat.data.entity.Offer;
import meet_eat.data.entity.Tag;
import meet_eat.data.entity.user.User;
import meet_eat.data.entity.user.contact.ContactData;
import meet_eat.data.entity.user.contact.ContactRequest;

/**
 * Manages offer-related information.
 */
public class OfferViewModel {

    /**
     * Requests the object of the user currently logged in to the device from the
     * {@link meet_eat.app.repository.Session Session}.
     *
     * @return The current user.
     */
    public User getCurrentUser() {
        return null;
    }

    /**
     * Sends an offer update request to the
     * {@link meet_eat.app.repository.OfferRepository OfferRepository}.
     *
     * @return A /LIST/ containing the updated offers.
     */
    public Iterable<Offer> fetchOffers() {
        return null;
    }

    /**
     * Sends a predicate update request to the
     * {@link meet_eat.app.repository.UserRepository UserRepository}.
     * Predicates are used to filter offers.
     *
     * @param predicates The predicates that are to be updated.
     */
    public void updatePredicates(Predicate<Offer>... predicates) {

    }

    /**
     * Requests the predicates currently stored in the
     * {@link meet_eat.app.repository.UserRepository UserRepository}.
     *
     * @return A /LIST/ containing the predicates.
     */
    public Iterable<Predicate<Offer>> getPredicates() {
        return null;
    }

    /**
     * Sends a comparator update request to the
     * {@link meet_eat.app.repository.UserRepository UserRepository}.
     * A comparator is used to sort a list of offers.
     *
     * @param comparators The comparators that are to be updated.
     */
    public void updateComparators(Comparator<Offer>... comparators) {

    }

    /**
     * Sends a request to the {@link meet_eat.app.repository.OfferRepository OfferRepository} to
     * add a new offer.
     *
     * @param offer The offer to be added.
     */
    public void add(Offer offer) {

    }

    /**
     * Sends a deletion request to the
     * {@link meet_eat.app.repository.OfferRepository OfferRepository}.
     *
     * @param offer The offer to be deleted.
     */
    public void delete(Offer offer) {

    }

    /**
     * Sends an update request for an existing offer to the
     * {@link meet_eat.app.repository.OfferRepository OfferRepository}.
     *
     * @param offer The offer that was modified.
     */
    public void edit(Offer offer) {

    }

    /**
     * TODO Is this not the same as edit(..)?
     * Adds the current user to an offer, then sends an offer update request to the
     * {@link meet_eat.app.repository.OfferRepository OfferRepository}.
     *
     * @param offer The offer, wherein the current user is to be added.
     */
    public void participate(Offer offer) {

    }

    /**
     * TODO Is this not the same as edit(..)?
     * Sends a request to the {@link meet_eat.app.repository.OfferRepository OfferRepository}
     * to update an offer after a {@param participant} was removed from it.
     *
     * @param participant The participant that is to be removed.
     * @param offer       The offer, whereof the participant is to be removed.
     */
    public void cancelParticipation(User participant, Offer offer) {

    }

    /**
     * Sends a "{@link ContactRequest ContactRequest}" request to the
     * {@link meet_eat.app.repository.UserRepository UserRepository}.
     *
     * @param request The contact request.
     */
    public void requestContact(ContactRequest request) {

    }

    /**
     * Sends a request including consent and the contact data to be given the Requester
     * to the {@link meet_eat.app.repository.UserRepository UserRepository}.
     *
     * @param contact The contact data that is to be sent.
     */
    public void sendContact(ContactData contact) {

    }

    /**
     * Sends a report request to the
     * {@link meet_eat.app.repository.UserRepository UserRepository}.
     *
     * @param user   The user that is to be reported.
     * @param report The report sent to the
     *               {@link meet_eat.app.repository.UserRepository UserRepository}.
     */
    public void report(User user, Report report) {

    }

    /**
     * Sends a user update request to the
     * {@link meet_eat.app.repository.UserRepository UserRepository}.
     *
     * @param offer The offer to be added to the users /LIST/ of bookmarks.
     */
    public void bookmark(Offer offer) {

    }

    /**
     * Requests every existing tag off of the
     * {@link meet_eat.app.repository.TagRepository TagRepository}.
     *
     * @return A /LIST/ containing the tags.
     */
    public Iterable<Tag> getAllTags() {
        return null;
    }
}
