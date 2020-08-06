package meet_eat.app.repository;

import com.google.common.collect.Lists;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
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

    /**
     * Creates an offer repository.
     */
    public OfferRepository() {
        super(BASE_URL);
    }

    public Iterable<Offer> getOffers(Page page, Iterable<OfferPredicate> predicates,
                                     OfferComparator comparator) throws RequestHandlerException {
        OfferPredicate timePredicate = new LocalDateTimePredicate(ChronoLocalDateTimeOperation.AFTER, LocalDateTime.now());
        List<OfferPredicate> predicateList = Lists.newArrayList(predicates);
        predicateList.add(timePredicate);
        predicates = predicateList;

        LinkedMultiValueMap<String, String> headers = getTokenHeaders();
        headers.add(RequestHeaderField.PREDICATES, new ObjectJsonParser().parseObjectToJsonString(predicates));
        headers.add(RequestHeaderField.COMPARATORS, new ObjectJsonParser().parseObjectToJsonString(comparator));
        headers.add(RequestHeaderField.PAGE, new ObjectJsonParser().parseObjectToJsonString(page));

        RequestEntity<Void> requestEntity = new RequestEntity<Void>(headers, HttpMethod.GET,
                URI.create(RequestHandler.SERVER_PATH + BASE_URL));
        return new RequestHandler<Void, Offer>().handleIterable(requestEntity, HttpStatus.OK);
    }

    /**
     * Returns the offers from the repository that were created by the corresponding creator.
     *
     * @param creator the corresponding creator whose offers are to be returned
     * @return the offers from the repository that were created by the corresponding creator
     * @throws RequestHandlerException if an error occurs when requesting the repository
     */
    public Iterable<Offer> getOffersByCreator(User creator, Page page, Iterable<OfferPredicate> predicates,
                                              OfferComparator comparator) throws RequestHandlerException {
        RequestEntity<Void> requestEntity = new RequestEntity<Void>(getTokenHeaders(), HttpMethod.GET,
                URI.create(RequestHandler.SERVER_PATH + BASE_URL + OWNER_ID_URL + Objects.requireNonNull(creator.getIdentifier())));
        return new RequestHandler<Void, Offer>().handleIterable(requestEntity, HttpStatus.OK);
    }

    public Iterable<Offer> getOffersBySubscriptions(User user, Page page, Iterable<OfferPredicate> predicates,
                                                OfferComparator comparator) throws RequestHandlerException {
        RequestEntity<Void> requestEntity = new RequestEntity<Void>(getTokenHeaders(), HttpMethod.GET,
                URI.create(RequestHandler.SERVER_PATH + BASE_URL + OWNER_ID_URL + Objects.requireNonNull(user.getIdentifier())));
        return new RequestHandler<Void, Offer>().handleIterable(requestEntity, HttpStatus.OK);
    }

    /**
     * Reports an offer in the repository by submitting a report.
     *
     * @param offer the offer to be reported
     * @param report the report to be submitted
     * @return the offer that was reported within the repository
     * @throws RequestHandlerException if an error occurs when requesting the repository
     */
    public Offer report(Offer offer, Report report) throws RequestHandlerException {
        Objects.requireNonNull(offer);
        offer.addReport(Objects.requireNonNull(report));
        return updateEntity(offer);
    }
}