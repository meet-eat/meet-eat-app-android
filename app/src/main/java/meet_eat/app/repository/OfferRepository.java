package meet_eat.app.repository;

import com.google.common.collect.Iterables;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Objects;

import meet_eat.data.ObjectJsonParser;
import meet_eat.data.Page;
import meet_eat.data.Report;
import meet_eat.data.RequestHeaderField;
import meet_eat.data.comparator.OfferComparator;
import meet_eat.data.entity.Offer;
import meet_eat.data.entity.user.User;
import meet_eat.data.predicate.OfferPredicate;
import meet_eat.data.predicate.chrono.ChronoLocalDateTimeOperation;
import meet_eat.data.predicate.chrono.LocalDateTimePredicate;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.util.LinkedMultiValueMap;

/**
 * Represents the administrative unit that controls access and manipulation of offers within a repository.
 */
public class OfferRepository extends EntityRepository<Offer> {

    private static final String BASE_URL = "/offers"; //TODO
    private static final String OWNER_ID_URL = "?owner=";
    private static final String SUBSCRIBER_ID_URL = "?subscriber=";

    /**
     * Creates an offer repository.
     */
    public OfferRepository() {
        super(BASE_URL);
    }

    /**
     * Returns offers page-based from the repository. The returned offers are filtered according to
     * given given predicates and sorted using the comparator.
     *
     * @param page       the page object used for page-based addressing
     * @param predicates the predicates used to filter the offers
     * @param comparator the comparator used to sort the offers
     * @return the offers matching the given specifications
     * @throws RequestHandlerException if an error occurs when requesting the repository
     */
    public Iterable<Offer> getOffers(Page page, Iterable<OfferPredicate> predicates,
                                     OfferComparator comparator) throws RequestHandlerException {
        return fetchOffers(BASE_URL, Objects.requireNonNull(page), Objects.requireNonNull(predicates),
                Objects.requireNonNull(comparator));
    }

    /**
     * Returns the offers created by the corresponding creator from the repository. Here, offers
     * are returned page-based, filtered according to given predicates and sorted using the comparator.
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
        return fetchOffers(BASE_URL + OWNER_ID_URL + Objects.requireNonNull(creator).getIdentifier(),
                Objects.requireNonNull(page), Objects.requireNonNull(predicates), Objects.requireNonNull(comparator));
    }

    /**
     * Returns the offers created by the users who are subscribed by the subscriber. Here, offers
     * are returned page-based, filtered according to given predicates and sorted using the comparator.
     *
     * @param subscriber the subscriber from whom the offers which are created by the users he subscribed
     *                   are to be returned from the repository
     * @param page       the page object used for page-based addressing
     * @param predicates the predicates used to filter the offers
     * @param comparator the comparator used to sort the offers
     * @return the offers matching the given specifications
     * @throws RequestHandlerException if an error occurs when requesting the repository
     */
    public Iterable<Offer> getOffersBySubscriptions(User subscriber, Page page, Iterable<OfferPredicate> predicates,
                                                    OfferComparator comparator) throws RequestHandlerException {
        return fetchOffers(BASE_URL + SUBSCRIBER_ID_URL + Objects.requireNonNull(subscriber).getIdentifier(),
                Objects.requireNonNull(page), Objects.requireNonNull(predicates), Objects.requireNonNull(comparator));
    }

    /**
     * Reports an offer in the repository by submitting a report.
     *
     * @param offer  the offer to be reported
     * @param report the report to be submitted
     * @return the offer that was reported within the repository
     * @throws RequestHandlerException if an error occurs when requesting the repository
     */
    public Offer report(Offer offer, Report report) throws RequestHandlerException {
        Objects.requireNonNull(offer);
        offer.addReport(Objects.requireNonNull(report));
        return updateEntity(offer);
    }

    /**
     * Returns the offers provided by the server at the endpoint with the given uri path segment. Here, offers
     * are returned page-based, filtered according to given predicates and sorted using the comparator.
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
        LocalDateTimePredicate timePredicate = new LocalDateTimePredicate(ChronoLocalDateTimeOperation.AFTER,
                LocalDateTime.now());
        predicates = Iterables.concat(Objects.requireNonNull(predicates), Collections.singletonList(timePredicate));

        //create headers
        LinkedMultiValueMap<String, String> headers = getTokenHeaders();
        headers.add(RequestHeaderField.PREDICATES, new ObjectJsonParser().parseObjectToJsonString(predicates));
        headers.add(RequestHeaderField.COMPARATORS,
                new ObjectJsonParser().parseObjectToJsonString(Objects.requireNonNull(comparator)));
        headers.add(RequestHeaderField.PAGE,
                new ObjectJsonParser().parseObjectToJsonString(Objects.requireNonNull(page)));

        //server request
        RequestEntity<Void> requestEntity = new RequestEntity<Void>(headers, HttpMethod.GET,
                URI.create(RequestHandler.SERVER_PATH + Objects.requireNonNull(uriPathSegment)));
        return new RequestHandler<Void, Offer>().handleIterable(requestEntity, HttpStatus.OK);
    }
}