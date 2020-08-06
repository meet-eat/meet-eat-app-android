package meet_eat.app.repository;

import org.junit.Test;

import java.util.Collections;

import meet_eat.data.LoginCredential;
import meet_eat.data.Page;
import meet_eat.data.entity.Offer;
import meet_eat.data.entity.user.Email;
import meet_eat.data.entity.user.Password;

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

        // Execution
        offers = offerRepository.getOffers(new Page(0, 1000), Collections.EMPTY_LIST, Collections.EMPTY_LIST);

        // Assertions
        assertNotNull(offers);
    }
}
