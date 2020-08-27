package meet_eat.app.repository;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;

import java.time.LocalDate;
import java.time.Month;

import meet_eat.data.LoginCredential;
import meet_eat.data.entity.relation.Report;
import meet_eat.data.entity.user.Email;
import meet_eat.data.entity.user.Password;
import meet_eat.data.entity.user.User;
import meet_eat.data.location.CityLocation;
import meet_eat.data.location.Localizable;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class ReportRepositoryTest extends EntityRepositoryTest<ReportRepository, Report, String> {

    private static User persistentUser;
    private static LoginCredential persistentLoginCredential;

    public ReportRepositoryTest() throws RequestHandlerException {
        super(new ReportRepository(), getPersistentReport(), getNewReport());
    }

    private static Report getPersistentReport() throws RequestHandlerException {
        Session.getInstance().login(getRegisteredLoginCredential());
        Report fetchedReport = new ReportRepository().addEntity(
                new Report(getRegisteredUser(), persistentUser, "Insulting."));
        Session.getInstance().logout();
        return fetchedReport;
    }

    private static Report getNewReport() throws RequestHandlerException {
        return new Report(getRegisteredUser(), persistentUser, "Not politically correct.");
    }

    @BeforeClass
    public static void initializePersistentUser() throws RequestHandlerException {
        Email email = new Email("wergviuhgvt349tz@example.com");
        Password password = Password.createHashedPassword("Str0ngPassw0rd!");
        LocalDate birthDay = LocalDate.of(1998, Month.OCTOBER, 16);
        String name = "JUnit Test User";
        String phoneNumber = "0123456789";
        String description = "This is a test description";
        boolean isVerified = false;
        Localizable localizable = new CityLocation("Karlsruhe");
        persistentLoginCredential = new LoginCredential(email, password);
        persistentUser = new UserRepository().addEntity(
                new User(email, password, birthDay, name, phoneNumber, description, isVerified, localizable));
    }

    @AfterClass
    public static void deletePersistentUser() throws RequestHandlerException {
        Session.getInstance().login(persistentLoginCredential);
        new UserRepository().deleteEntity(persistentUser);
        Session.getInstance().logout();
    }

    @Override
    public void testAddEntityValid() throws RequestHandlerException {
        // Execution
        Session.getInstance().login(getRegisteredLoginCredential());
        assertNotNull(Session.getInstance().getToken());
        assertNull(getNewReport().getIdentifier());
        Report fetchedReport = getEntityRepository().addEntity(getNewReport());

        // Assertions
        assertNotNull(fetchedReport.getIdentifier());
    }

    @Override
    public void testUpdateEntityValidWithoutChanges() {
    }

    @Override
    public void testDeleteEntityValid() {
    }

    @Override
    public void testGetEntityByIdValid() {
    }
}
