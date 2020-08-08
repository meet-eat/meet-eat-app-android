package meet_eat.app.fragment;

/**
 * Represents the types of the offer list page used in {@link meet_eat.app.fragment.main.offer.OfferListFragment}.
 */
public enum ListType {

    /**
     * The standard page type, which contains all offers.
     */
    STANDARD,

    /**
     * The own page type, which contains only offer the user created.
     */
    OWN,

    /**
     * The bookmarked page type, which contains only offers that have been bookmarked by the user.
     */
    BOOKMARKED,

    /**
     * The subscribed page type, which contains only offers from users that have been subscribed.
     */
    SUBSCRIBED
}
