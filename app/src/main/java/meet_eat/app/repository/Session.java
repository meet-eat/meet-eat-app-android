package meet_eat.app.repository;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;

import java.net.URI;
import java.util.Objects;

import meet_eat.data.EndpointPath;
import meet_eat.data.LoginCredential;
import meet_eat.data.entity.Token;
import meet_eat.data.entity.user.User;

/**
 * Represents and manages the singleton instance of a {@link User user}'s {@link Session session} in the application.
 */
public class Session {

    private static Session session;
    private static final String ERROR_MESSAGE_NOT_LOGGED_IN = "Cannot logout session with null token.";

    private Token token;

    /**
     * Creates a {@link Session session}.
     */
    private Session() {
    }

    /**
     * Returns the singleton instance of the {@link Session session}.
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
     * Returns the {@link User user} associated with the {@link Session session}.
     *
     * @return the user associated with the session
     */
    public User getUser() {
        return Objects.requireNonNull(token).getUser();
    }

    /**
     * Returns the {@link Token token} associated with the {@link Session session}.
     *
     * @return the token associated with the session
     */
    public Token getToken() {
        return token;
    }

    /**
     * Logs in the {@link User user} with the corresponding {@link LoginCredential login credentials}.
     *
     * @param loginCredential the login credentials of the user to be logged in
     * @throws RequestHandlerException if an error occurs when requesting the repository
     */
    public void login(LoginCredential loginCredential) throws RequestHandlerException {
        RequestEntity<LoginCredential> requestEntity = new RequestEntity<LoginCredential>(Objects.requireNonNull(loginCredential),
                HttpMethod.POST, URI.create(RequestHandler.SERVER_PATH + EndpointPath.LOGIN));
        token = new RequestHandler<LoginCredential, Token>().handle(requestEntity, HttpStatus.CREATED);
    }

    /**
     * Logs out the {@link User user} associated with the {@link Session session}.
     *
     * @throws RequestHandlerException if an error occurs when requesting the repository
     */
    public void logout() throws RequestHandlerException {
        if (Objects.isNull(token)) {
            throw new IllegalStateException(ERROR_MESSAGE_NOT_LOGGED_IN);
        }
        RequestEntity<Token> requestEntity = new RequestEntity<Token>(token, HttpMethod.DELETE,
                URI.create(RequestHandler.SERVER_PATH + EndpointPath.LOGOUT));
        new RequestHandler<Token, Void>().handle(requestEntity, HttpStatus.NO_CONTENT);
        token = null;
    }
}