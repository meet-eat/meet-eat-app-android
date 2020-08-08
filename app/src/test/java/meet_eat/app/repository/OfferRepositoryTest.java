package meet_eat.app.repository;

import meet_eat.data.entity.Offer;

public class OfferRepositoryTest extends EntityRepositoryTest<OfferRepository, Offer, String> {

    public OfferRepositoryTest() {
        super(new OfferRepository(), null, null); //TODO
    }
}
