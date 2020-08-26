package meet_eat.app.fragment;

/**
 * Represents the types of keys that are exchanged in navigation arguments.
 */
public enum NavigationArgumentKey {

    /**
     * The offer key means, that the argument is an {@link meet_eat.data.entity.Offer} object.
     */
    OFFER,

    /**
     * The user key means, that the argument is an {@link meet_eat.data.entity.user.User} object.
     */
    USER,

    /**
     * The list type key means, that the argument is a {@link ListType} enum.
     */
    LIST_TYPE
}