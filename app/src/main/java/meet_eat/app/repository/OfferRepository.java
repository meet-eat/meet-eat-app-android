package meet_eat.app.repository;

import java.net.URI;
import java.util.Comparator;
import java.util.Objects;
import java.util.function.Predicate;

import meet_eat.data.Page;
import meet_eat.data.Report;
import meet_eat.data.RequestHeaderField;
import meet_eat.data.entity.Offer;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.util.LinkedMultiValueMap;

public class OfferRepository extends EntityRepository<Offer> {

    private static final String BASE_URL = "/offers"; //TODO
    private static final String OWNER_ID_URL = "?owner=";

    public OfferRepository() {
        super(BASE_URL);
    }

    public Iterable<Offer> getOffers(Page page, Iterable<Predicate<Offer>> predicates,
                                     Iterable<Comparator<Offer>> comparators) throws RequestHandlerException {
        LinkedMultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
        headers.add(RequestHeaderField.TOKEN, toJSON(Session.getInstance().getToken()));
        headers.add(RequestHeaderField.PREDICATES, toJSON(predicates));
        headers.add(RequestHeaderField.COMPARATORS, toJSON(comparators));
        headers.add(RequestHeaderField.PAGE, toJSON(page));
        RequestEntity<Void> requestEntity = new RequestEntity<Void>(headers, HttpMethod.GET, URI.create(BASE_URL));
        return new RequestHandler<Void, Iterable<Offer>>().handle(requestEntity, HttpStatus.OK);
    }

    public Iterable<Offer> getOffersByCreatorId(String identifier) throws RequestHandlerException {
        RequestEntity<Void> requestEntity = new RequestEntity<Void>(getTokenHeaders(), HttpMethod.GET,
                URI.create(RequestHandler.SERVER_PATH + BASE_URL + OWNER_ID_URL + Objects.requireNonNull(identifier)));
        return new RequestHandler<Void, Iterable<Offer>>().handle(requestEntity, HttpStatus.OK);
    }

    public void report(Offer offer, Report report) throws RequestHandlerException {
        Objects.requireNonNull(offer);
        offer.addReport(Objects.requireNonNull(report));
        updateEntity(offer);
    }
}