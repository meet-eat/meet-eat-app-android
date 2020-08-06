package meet_eat.app.repository;

import java.net.URI;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;

import meet_eat.data.ObjectJsonParser;
import meet_eat.data.Page;
import meet_eat.data.Report;
import meet_eat.data.RequestHeaderField;
import meet_eat.data.entity.Offer;
import meet_eat.data.predicate.OfferPredicate;

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
                                     Iterable<Comparator<Offer>> comparators) throws RequestHandlerException {
        LinkedMultiValueMap<String, String> headers = getTokenHeaders();
        headers.add(RequestHeaderField.PREDICATES, new ObjectJsonParser().parseObjectToJsonString(predicates));
        headers.add(RequestHeaderField.COMPARATORS, new ObjectJsonParser().parseObjectToJsonString(comparators));
        headers.add(RequestHeaderField.PAGE, new ObjectJsonParser().parseObjectToJsonString(page));
        RequestEntity<Void> requestEntity = new RequestEntity<Void>(headers, HttpMethod.GET,
                URI.create(RequestHandler.SERVER_PATH + BASE_URL));
        return new RequestHandler<Void, Offer>().handleIterable(requestEntity, HttpStatus.OK);
    }

    /**
     * Returns the offers of the user with the corresponding identifier from the repository.
     *
     * @param identifier the corresponding identifier of the user whose offers are to be returned
     * @return the offers of the user with the corresponding identifier from the repository
     * @throws RequestHandlerException if an error occurs when requesting the repository
     */
    public Iterable<Offer> getOffersByCreatorId(String identifier) throws RequestHandlerException {
        RequestEntity<Void> requestEntity = new RequestEntity<Void>(getTokenHeaders(), HttpMethod.GET,
                URI.create(RequestHandler.SERVER_PATH + BASE_URL + OWNER_ID_URL + Objects.requireNonNull(identifier)));
        return new RequestHandler<Void, Offer>().handleIterable(requestEntity, HttpStatus.OK);
    }

    /**
     * Reports an offer in the repository by submitting a report.
     *
     * @param offer the offer to be reported
     * @param report the report to be submitted
     * @throws RequestHandlerException if an error occurs when requesting the repository
     */
    public void report(Offer offer, Report report) throws RequestHandlerException {
        Objects.requireNonNull(offer);
        offer.addReport(Objects.requireNonNull(report));
        updateEntity(offer);
    }
}