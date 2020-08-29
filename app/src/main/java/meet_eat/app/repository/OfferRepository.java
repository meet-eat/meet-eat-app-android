package meet_eat.app.repository;

import com.google.common.collect.Lists;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.util.LinkedMultiValueMap;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import meet_eat.data.EndpointPath;
import meet_eat.data.ObjectJsonParser;
import meet_eat.data.Page;
import meet_eat.data.RequestHeaderField;
import meet_eat.data.comparator.OfferComparator;
import meet_eat.data.entity.Offer;
import meet_eat.data.entity.relation.Participation;
import meet_eat.data.entity.user.User;
import meet_eat.data.predicate.OfferPredicate;
import meet_eat.data.predicate.chrono.LocalDateTimeOperation;
import meet_eat.data.predicate.chrono.LocalDateTimePredicate;

/**
 * Represents the administrative unit that controls access and manipulation of {@link Offer offers} within a repository.
 */
public class OfferRepository extends EntityRepository<Offer> {

    private static final String OWNER_ID_URL = "?owner=";
    private static final String SUBSCRIBER_ID_URL = "?subscriber=";

    /**
     * Creates an {@link Offer offer} repository.
     */
    public OfferRepository() {
        super(EndpointPath.OFFERS);
    }

    /**
     * Returns {@link Offer offers} {@link Page page}-based from the repository. The returned offers are filtered
     * according to given given {@link OfferPredicate predicates} and sorted using the {@link OfferComparator
     * comparator}.
     *
     * @param page       the page object used for page-based addressing
     * @param predicates the predicates used to filter the offers
     * @param comparator the comparator used to sort the offers
     * @return the offers matching the given specifications
     * @throws RequestHandlerException if an error occurs when requesting the repository
     */
    public Iterable<Offer> getOffers(Page page, Iterable<OfferPredicate> predicates,
                                     OfferComparator comparator) throws RequestHandlerException {
        return fetchOffers(getEntityPath(), Objects.requireNonNull(page), Objects.requireNonNull(predicates),
                Objects.requireNonNull(comparator));
    }

    /**
     * Returns the {@link Offer offers} created by the corresponding {@link User creator} from the repository.
     * Here, offers are returned {@link Page page}-based, filtered according to given {@link OfferPredicate predicates}
     * and sorted using the {@link OfferComparator comparator}.
     *
     * @param creator    the corresponding creator whose offers are to be returned from the repository
     * @param page       the page object used for page-based addressing
     * @param predicates the predicates used to filter the offers
     * @param comparator the comparator used to sort the offers
     * @return the offers matching the given specifications
     * @throws RequestHandlerException if an error occurs when requesting the repository
     */
    public Iterable<Offer> getOffersByCreator(User creator, Page page, Iterable<OfferPredicate> predicates,
                                              OfferComparator comparator) throws RequestHandlerException {
        Objects.requireNonNull(creator);
        return fetchOffers(getEntityPath() + OWNER_ID_URL + Objects.requireNonNull(creator.getIdentifier()),
                Objects.requireNonNull(page), Objects.requireNonNull(predicates), Objects.requireNonNull(comparator));
    }

    /**
     * Returns the {@link Offer offers} created by the {@link User subscribers} of the {@link User subscriber}.
     * Here, offers are returned {@link Page page}-based, filtered according to given {@link OfferPredicate predicates}
     * and sorted using the {@link OfferComparator comparator}.
     *
     * @param subscriber the subscriber from whom the offers created by the subscribers he subscribed
     *                   are to be returned from the repository
     * @param page       the page object used for page-based addressing
     * @param predicates the predicates used to filter the offers
     * @param comparator the comparator used to sort the offers
     * @return the offers matching the given specifications
     * @throws RequestHandlerException if an error occurs when requesting the repository
     */
    public Iterable<Offer> getOffersBySubscriptions(User subscriber, Page page, Iterable<OfferPredicate> predicates,
                                                    OfferComparator comparator) throws RequestHandlerException {
        Objects.requireNonNull(subscriber);
        return fetchOffers(getEntityPath() + SUBSCRIBER_ID_URL + Objects.requireNonNull(subscriber.getIdentifier()),
                Objects.requireNonNull(page), Objects.requireNonNull(predicates), Objects.requireNonNull(comparator));
    }

    /**
     * Creates a persistent {@link Participation participation} by adding it to the repository.
     *
     * @param participation the participation to be added to the repository
     * @return the participation added to the repository
     * @throws RequestHandlerException if an error occurs when requesting the repository
     */
    public Participation addParticipation(Participation participation) throws RequestHandlerException {
        String uriOfferIdentifier = "/" + Objects.requireNonNull(participation.getTarget().getIdentifier());

        // Handle request
        RequestEntity<Participation> requestEntity = new RequestEntity<>(participation, getTokenHeaders(), HttpMethod.POST,
                URI.create(RequestHandler.SERVER_PATH + getEntityPath() + uriOfferIdentifier + EndpointPath.PARTICIPATIONS));
        return new RequestHandler<Participation, Participation>().handle(requestEntity, HttpStatus.CREATED);
    }

    /**
     * Removes a {@link Participation participation} from the repository.
     *
     * @param participation the participation to be removed from the repository
     * @throws RequestHandlerException if an error occurs when requesting the repository
     */
    public void removeParticipation(Participation participation) throws RequestHandlerException {
        String uriOfferIdentifier = "/" + Objects.requireNonNull(participation.getTarget().getIdentifier());

        // Handle request
        RequestEntity<Participation> requestEntity = new RequestEntity<>(participation, getTokenHeaders(), HttpMethod.DELETE,
                URI.create(RequestHandler.SERVER_PATH + getEntityPath() + uriOfferIdentifier + EndpointPath.PARTICIPATIONS));
        new RequestHandler<Participation, Participation>().handle(requestEntity, HttpStatus.NO_CONTENT);
    }

    /**
     * Returns the {@link Participation participations} of an {@link Offer offer} from the repository.
     *
     * @param offer the offer of which participations are to be returned from the repository
     * @return the participations of an offer in the repository
     * @throws RequestHandlerException if an error occurs when requesting the repository
     */
    public Iterable<Participation> getParticipationsByOffer(Offer offer) throws RequestHandlerException {
        String uriOfferIdentifier = "/" + Objects.requireNonNull(offer.getIdentifier());

        // Handle request
        RequestEntity<Void> requestEntity = new RequestEntity<>(getTokenHeaders(), HttpMethod.GET,
                URI.create(RequestHandler.SERVER_PATH + getEntityPath() + uriOfferIdentifier + EndpointPath.PARTICIPATIONS));
        return new RequestHandler<Void, Participation>().handleIterable(requestEntity, HttpStatus.OK);
    }

    /**
     * Returns the {@link Offer offers} provided by the server at the {@link EndpointPath endpoint} with the given URI
     * path segment. Here, offers are returned {@link Page page}-based, filtered according to given
     * {@link OfferPredicate predicates} and sorted using the {@link OfferComparator comparator}.
     *
     * @param uriPathSegment the uri path segment of the server endpoint from which the offers are to be requested
     * @param page           the page object used for page-based addressing
     * @param predicates     the predicates used to filter the offers
     * @param comparator     the comparator used to sort the offers
     * @return the offers matching the given specifications
     * @throws RequestHandlerException if an error occurs when requesting the repository
     */
    private Iterable<Offer> fetchOffers(String uriPathSegment, Page page, Iterable<OfferPredicate> predicates,
                                        OfferComparator comparator) throws RequestHandlerException {
        LocalDateTimePredicate timePredicate = new LocalDateTimePredicate(LocalDateTimeOperation.AFTER,
                LocalDateTime.now());
        List<OfferPredicate> predicateList = Lists.newLinkedList(Objects.requireNonNull(predicates));
        predicateList.add(timePredicate);

        // Create request header map
        LinkedMultiValueMap<String, String> headers = getTokenHeaders();
        headers.add(RequestHeaderField.PREDICATES, new ObjectJsonParser().parseObjectToJsonString(predicateList));
        headers.add(RequestHeaderField.COMPARATORS,
                new ObjectJsonParser().parseObjectToJsonString(Objects.requireNonNull(comparator)));
        headers.add(RequestHeaderField.PAGE,
                new ObjectJsonParser().parseObjectToJsonString(Objects.requireNonNull(page)));

        // Handle request
        RequestEntity<Void> requestEntity = new RequestEntity<>(headers, HttpMethod.GET,
                URI.create(RequestHandler.SERVER_PATH + Objects.requireNonNull(uriPathSegment)));
        return new RequestHandler<Void, Offer>().handleIterable(requestEntity, HttpStatus.OK);
    }
}