package meet_eat.app.repository;

import meet_eat.data.LoginCredential;
import meet_eat.data.entity.Token;
import meet_eat.data.entity.user.Email;
import meet_eat.data.entity.user.Password;
import meet_eat.data.entity.user.User;

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

    private static final boolean DEBUG = true;

    private static Session session;
    private static final String URL_LOGIN = "/login"; //TODO
    private static final String URL_LOGOUT = "/logout"; //TODO
    private static final String ERROR_MESSAGE_NOT_LOGGED_IN = "Cannot logout session with null token.";

    private Token token;

    private class TokenRepository extends EntityRepository<Token> {

        protected TokenRepository(String entityPath) {
            super(entityPath);
        }
    }

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
            User testUser1 = new User(new Email("tester@testi.de"), Password
                    .createHashedPassword("123abcABC!§%"), LocalDate.of(1999, 1, 21), "Gregor " +
                    "Snelting",
                    "+49160304050", "Ich liebe PSE", true);
            testUser1.addSubscription(new User(new Email("abcde@web.de"), Password
                    .createHashedPassword("123abcABC!§%"), LocalDate.of(1999, 1, 21), "Stefan Kühnlein",
                    "+49160323050", "Ich liebe Mathe", true));
            return testUser1;
        }

        return token.getUser();
    }

    public Token getToken() {
        return token;
    }

    public void login(LoginCredential loginCredential) throws RequestHandlerException {
        RequestEntity<LoginCredential> requestEntity = new RequestEntity<LoginCredential>(loginCredential,
                HttpMethod.POST, URI.create(RequestHandler.SERVER_PATH + URL_LOGIN));
        token = new RequestHandler<LoginCredential, Token>().handle(requestEntity, HttpStatus.OK);
    }

    public void logout() throws RequestHandlerException {
        if (Objects.isNull(token)) {
            throw new IllegalStateException(ERROR_MESSAGE_NOT_LOGGED_IN);
        }
        RequestEntity<Token> requestEntity = new RequestEntity<Token>(token, HttpMethod.POST, URI.create(RequestHandler.SERVER_PATH + URL_LOGOUT));
        new RequestHandler<Token, Void>().handle(requestEntity, HttpStatus.NO_CONTENT);
        token = null;
    }
}