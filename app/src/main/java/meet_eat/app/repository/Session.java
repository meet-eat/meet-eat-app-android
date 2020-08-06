package meet_eat.app.repository;

import meet_eat.data.LoginCredential;
import meet_eat.data.entity.Token;
import meet_eat.data.entity.user.User;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;

import java.net.URI;
import java.util.Objects;

/**
 * Represents and manages the singleton instance of a user's session in the application.
 */
public class Session {

    private static Session session;
    private static final String URL_LOGIN = "/login"; //TODO
    private static final String URL_LOGOUT = "/logout"; //TODO
    private static final String ERROR_MESSAGE_NOT_LOGGED_IN = "Cannot logout session with null token.";

    private Token token;

    /**
     * Creates a session.
     */
    private Session() {
    }

    /**
     * Returns the singleton instance of the session.
     *
     * @return the singleton instance of the session
     */
    public static Session getInstance() {
        if (Objects.isNull(session)) {
            session = new Session();
        }
        return session;
    }

    /**
     * Returns the user associated with the session.
     *
     * @return the user associated with the session
     */
    public User getUser() {
        return token.getUser();
    }

    /**
     * Returns the token associated with the session.
     *
     * @return the token associated with the session
     */
    public Token getToken() {
        return token;
    }

    /**
     * Logs in the user with the corresponding login credentials.
     *
     * @param loginCredential the login credentials of the user to be logged in
     * @throws RequestHandlerException if an error occurs when requesting the repository
     */
    public void login(LoginCredential loginCredential) throws RequestHandlerException {
        RequestEntity<LoginCredential> requestEntity = new RequestEntity<LoginCredential>(Objects.requireNonNull(loginCredential),
                HttpMethod.POST, URI.create(RequestHandler.SERVER_PATH + URL_LOGIN));
        token = new RequestHandler<LoginCredential, Token>().handle(requestEntity, HttpStatus.CREATED);
    }

    /**
     * Logs out the user associated with the session.
     *
     * @throws RequestHandlerException if an error occurs when requesting the repository
     */
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