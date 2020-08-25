package meet_eat.app.repository;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;

import java.io.IOException;
import java.net.URI;
import java.util.Objects;

import meet_eat.data.EndpointPath;
import meet_eat.data.LoginCredential;
import meet_eat.data.ObjectJsonParser;
import meet_eat.data.entity.Token;
import meet_eat.data.entity.user.User;

/**
 * Represents and manages the singleton instance of a {@link User user}'s {@link Session session} in the application.
 */
public class Session {

    private static final String ERROR_MESSAGE_NOT_LOGGED_IN = "Cannot logout session with null token.";
    private static final String ERROR_MESSAGE_ALREADY_LOGGED_IN = "Already logged in.";
    private static final String TOKEN_FILE_NAME = "token";

    private static Session session;

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
            try {
                if (FileHandler.fileExists(TOKEN_FILE_NAME)) {
                    String tokenRepresentation = FileHandler.readFileToString(TOKEN_FILE_NAME);
                    //session.token = new ObjectJsonParser().parseJsonStringToObject(tokenRepresentation, Token.class);
                    // TODO delete comment after fix login screen
                    //TODO check if token is valid
                }
            } catch (IOException e) {
                // The session must not be interrupted. It should always continue even if the file cannot be read.
            }
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
        if (Objects.nonNull(token)) {
            throw new IllegalStateException(ERROR_MESSAGE_ALREADY_LOGGED_IN);
        }
        RequestEntity<LoginCredential> requestEntity = new RequestEntity<>(Objects.requireNonNull(loginCredential), HttpMethod.POST,
                        URI.create(RequestHandler.SERVER_PATH + EndpointPath.LOGIN));
        token = new RequestHandler<LoginCredential, Token>().handle(requestEntity, HttpStatus.CREATED);
        try {
            FileHandler.saveStringToFile(new ObjectJsonParser().parseObjectToJsonString(token), TOKEN_FILE_NAME);
        } catch (IOException e) {
            // The session must not be interrupted. It should always continue even if the file cannot be saved.
        }
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
        RequestEntity<Token> requestEntity = new RequestEntity<>(token, HttpMethod.DELETE,
                URI.create(RequestHandler.SERVER_PATH + EndpointPath.LOGOUT));
        try {
            new RequestHandler<Token, Void>().handle(requestEntity, HttpStatus.NO_CONTENT);
        } catch (RequestHandlerException exception) {
            // Check if the token is not existing on the server
            if (!exception.getMessage().contains(Integer.toString(HttpStatus.NOT_FOUND.value()))) {
                throw exception;
            }
        }
        try {
            FileHandler.deleteFile(TOKEN_FILE_NAME);
        } catch (IOException exception) {
            // The session must not be interrupted. It should always continue even if the file cannot be deleted.
        }
        token = null;
    }

    /**
     * Checks if the user is logged in with a valid token.
     *
     * @return {@code true} if the user is logged in with a valid token; {@code false} otherwise
     */
    public boolean isLoggedIn() {
        // Could not be logged in with null token
        if (Objects.isNull(token)) {
            return false;
        }
        RequestEntity<Token> requestEntity = new RequestEntity<>(token, HttpMethod.POST,
                URI.create(EndpointPath.TOKENS + EndpointPath.VALIDITY));
        boolean isValidToken;

        // Request server to fetch if token is valid
        try {
            isValidToken = new RequestHandler<Token, Boolean>().handle(requestEntity, HttpStatus.OK);
        } catch (RequestHandlerException exception) {
            token = null;
            return false;
        }
        if (isValidToken) {
            return true;
        }
        token = null;
        return false;
    }
}