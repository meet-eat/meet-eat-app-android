package meet_eat.app.repository;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

import meet_eat.data.LoginCredential;
import meet_eat.data.Page;
import meet_eat.data.comparator.OfferComparableField;
import meet_eat.data.comparator.OfferComparator;
import meet_eat.data.entity.Offer;
import meet_eat.data.entity.user.Email;
import meet_eat.data.entity.user.Password;
import meet_eat.data.location.SphericalLocation;
import meet_eat.data.location.SphericalPosition;

import static org.junit.Assert.assertNotNull;

public class OfferRepositoryTest {

    @Test
    public void getOffersUnfiltered() throws RequestHandlerException {
        // Test data
        Session session = Session.getInstance();
        session.login(new LoginCredential(
                new Email("moritz@gstuer.com"),
                Password.createHashedPassword("MoritzTest1!")
        ));
        OfferRepository offerRepository = new OfferRepository();
        Iterable<Offer> offers;
        OfferComparator offerComparator = new OfferComparator(OfferComparableField.TIME, new SphericalLocation(new SphericalPosition(0, 0)));

        // Execution
        offers = offerRepository.getOffers(new Page(0, 1000), new ArrayList<>(), offerComparator);

        // Assertions
        assertNotNull(offers);
    }
}
