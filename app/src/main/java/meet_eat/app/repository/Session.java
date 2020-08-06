package meet_eat.app.repository;

import meet_eat.data.LoginCredential;
import meet_eat.data.entity.Token;
import meet_eat.data.entity.user.Email;
import meet_eat.data.entity.user.Password;
import meet_eat.data.entity.user.User;

import meet_eat.data.location.Localizable;
import meet_eat.data.location.SphericalLocation;
import meet_eat.data.location.SphericalPosition;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.time.LocalDate;
import java.util.Objects;

public class Session {

    private static final boolean DEBUG = false;

    private static Session session;
    private static final String URL_LOGIN = "/login"; //TODO
    private static final String URL_LOGOUT = "/logout"; //TODO
    private static final String ERROR_MESSAGE_NOT_LOGGED_IN = "Cannot logout session with null token.";

    private Token token;

    private Session() {
    }

    public static Session getInstance() {
        if (Objects.isNull(session)) {
            session = new Session();
        }
        return session;
    }

    public User getUser() {
        if (DEBUG) {
            Localizable validLocalizable = new SphericalLocation(new SphericalPosition(47, 8));
            User testUser1 = new User(new Email("tester@testi.de"), Password
                    .createHashedPassword("123abcABC!§%"), LocalDate.of(1999, 1, 21), "Tester " +
                    "Testi 1",
                    "+49160304050", "Testbeschreibung 1", true, validLocalizable);
            testUser1.addSubscription(new User(new Email("abcde@web.de"), Password
                    .createHashedPassword("123abcABC!§%"), LocalDate.of(1999, 1, 21), "Tester Testi 2",
                    "+49160323050", "", true, validLocalizable));
            return testUser1;
        }

        return token.getUser();
    }

    public Token getToken() {
        return token;
    }

    public void login(LoginCredential loginCredential) throws RequestHandlerException {
        RequestEntity<LoginCredential> requestEntity = new RequestEntity<LoginCredential>(Objects.requireNonNull(loginCredential),
                HttpMethod.POST, URI.create(RequestHandler.SERVER_PATH + URL_LOGIN));
        token = new RequestHandler<LoginCredential, Token>().handle(requestEntity, HttpStatus.CREATED);
    }

    public void logout() throws RequestHandlerException {
        if (Objects.isNull(token)) {
            throw new IllegalStateException(ERROR_MESSAGE_NOT_LOGGED_IN);
        }
        RequestEntity<Token> requestEntity = new RequestEntity<Token>(token, HttpMethod.DELETE,
                URI.create(RequestHandler.SERVER_PATH + URL_LOGOUT));
        new RequestHandler<Token, Void>().handle(requestEntity, HttpStatus.NO_CONTENT);
        token = null;
    }
}