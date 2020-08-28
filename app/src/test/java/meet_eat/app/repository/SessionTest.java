package meet_eat.app.repository;

import org.junit.Test;

import meet_eat.data.LoginCredential;
import meet_eat.data.entity.user.Email;
import meet_eat.data.entity.user.Password;
import meet_eat.data.entity.user.User;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class SessionTest extends RepositoryTestEnvironment{

    @Test
    public void testGetInstance() {
        // Execution
        Session session = Session.getInstance();
        Session sessionTwo = Session.getInstance();

        // Assertions
        assertNotNull(session);
        assertEquals(session, sessionTwo);
    }

    @Test(expected = NullPointerException.class)
    public void testGetUserNotLoggedIn() {
        // Execution
        Session.getInstance().getUser();
    }

    @Test
    public void testGetUserLoggedIn() throws RequestHandlerException {
        // Execution
        Session session = Session.getInstance();
        session.login(getRegisteredLoginCredential());
        User loggedInUser = session.getUser();

        // Assertions
        assertNotNull(loggedInUser);
        assertEquals(getRegisteredUser(), loggedInUser);
    }

    @Test
    public void testGetTokenNotLoggedIn() {
        // Assertions
        assertNull(Session.getInstance().getToken());
    }

    @Test
    public void testLogin() throws RequestHandlerException {
        Session session = Session.getInstance();
        assertNull(session.getToken());

        // Execution
        session.login(getRegisteredLoginCredential());

        // Assertions
        assertNotNull(session.getToken());
    }

    @Test(expected = RequestHandlerException.class)
    public void testLoginWrongCredential() throws RequestHandlerException {
        // Test data
        Email email = new Email("abcdefghijklmnopqrstuvwxyz@example.com");
        Password password = Password.createHashedPassword("wv584zn958v5vZ6b9b2v!");

        // Execution
        Session.getInstance().login(new LoginCredential(email, password));
    }

    @Test(expected = NullPointerException.class)
    public void testLoginNullCredential() throws RequestHandlerException {
        // Execution
        Session.getInstance().login(null);
    }

    @Test
    public void testLogout() throws RequestHandlerException {
        // Execution
        Session session = Session.getInstance();
        session.login(getRegisteredLoginCredential());

        // Assertions
        assertNotNull(session.getToken());

        session.logout();

        assertNull(session.getToken());
    }

    @Test(expected = IllegalStateException.class)
    public void testLogoutWhenLoggedOut() throws RequestHandlerException {
        // Execution
        Session session = Session.getInstance();

        // Assertions
        assertNull(session.getToken());
        session.logout();
    }

    @Test(expected = IllegalStateException.class)
    public void testLoginWhenLoggedIn() throws RequestHandlerException {
        // Execution
        Session session = Session.getInstance();
        assertNull(session.getToken());
        session.login(getRegisteredLoginCredential());

        // Assertions
        assertNotNull(session.getToken());
        session.login(getRegisteredLoginCredential());
    }

    @Test
    public void testLogoutAfterDeleteUser() throws RequestHandlerException {
        // Execution
        Session session = Session.getInstance();
        session.login(getRegisteredLoginCredential());
        new UserRepository().deleteEntity(session.getUser());

        // Assertions
        assertNotNull(session.getToken());
        session.logout();
        assertNull(session.getToken());

        setUpClass();
    }

    @Test
    public void testLoginAndLogoutMultipleTimes() throws RequestHandlerException {
        // Execution
        Session session = Session.getInstance();

        // Assertions
        assertNull(session.getToken());
        for (int i = 0; i < 2; i++) {
            session.login(getRegisteredLoginCredential());
            assertNotNull(session.getToken());
            session.logout();
            assertNull(session.getToken());
        }
    }

    @Test
    public void testIsLoggedInFalse() throws RequestHandlerException {
        // Execution
        Session session = Session.getInstance();

        // Assertions
        assertFalse(session.isLoggedIn());
    }

    @Test
    public void testIsLoggedInTrue() throws RequestHandlerException {
        // Execution
        Session session = Session.getInstance();
        session.login(getRegisteredLoginCredential());

        // Assertions
        assertTrue(session.isLoggedIn());
    }

    @Test
    public void testIsLoggedInAfterDeleteUser() throws RequestHandlerException {
        // Execution
        Session session = Session.getInstance();
        session.login(getRegisteredLoginCredential());
        assertTrue(session.isLoggedIn());
        new UserRepository().deleteEntity(session.getUser());

        // Assertions
        assertFalse(session.isLoggedIn());

        setUpClass();
    }
}
