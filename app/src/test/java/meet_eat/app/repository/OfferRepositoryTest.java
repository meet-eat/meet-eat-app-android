package meet_eat.app.repository;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import meet_eat.data.Report;
import meet_eat.data.entity.Offer;
import meet_eat.data.entity.Tag;
import meet_eat.data.entity.user.User;
import meet_eat.data.location.CityLocation;
import meet_eat.data.location.Localizable;

public class OfferRepositoryTest extends EntityRepositoryTest<OfferRepository, Offer, String> {

    public OfferRepositoryTest() {
        super(new OfferRepository(), getOfferWithId(), getOfferWithoutId());
    }

    private static Offer getOfferWithId() {
        String identifier = "gh30sifj02";
        Collection<Report> reports = new LinkedList<>();
        User creator = getRegisteredUser();
        Set<User> participants = new HashSet<>();
        Set<Tag> tags = new HashSet<>();
        String name = "JUnit Test Offer";
        String description = "This is a test offer";
        double price = 5d;
        int maxParticipants = 5;
        LocalDateTime dateTime = LocalDateTime.of(2020, Month.SEPTEMBER, 30, 18, 0);
        Localizable location = new CityLocation("Karlsruhe");
        return new Offer(identifier, reports, creator, participants, tags, name, description, price, maxParticipants,
                dateTime, location);
    }

    private static Offer getOfferWithoutId() {
        User creator = getRegisteredUser();
        Set<Tag> tags = new HashSet<>();
        String name = "JUnit Test Offer";
        String description = "This is a test offer";
        double price = 5d;
        int maxParticipants = 5;
        LocalDateTime dateTime = LocalDateTime.of(2020, Month.SEPTEMBER, 30, 18, 0);
        Localizable location = new CityLocation("Karlsruhe");
        return new Offer(creator, tags, name, description, price, maxParticipants, dateTime, location);
    }
}
