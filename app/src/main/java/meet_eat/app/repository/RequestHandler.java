package meet_eat.app.repository;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;

import javax.net.ssl.SSLContext;

import meet_eat.app.MeetEatApplication;
import meet_eat.data.ObjectJsonParser;

/**
 * Represents the unit for handling HTTP requests to the server.
 *
 * @param <T> the element type of the input
 * @param <S> the type of the output
 */
public class RequestHandler<T, S> {

    /**
     * The URL for the server path.
     */
    public static final String SERVER_PATH = "https://karlsruhe.gstuer.com:25565";

    private static final String ERROR_MESSAGE_REQUEST = "Request failed. ";
    private static final String ERROR_MESSAGE_STATUS_CODE = "Error code: ";
    private static final String ERROR_MESSAGE_CERTIFICATE = "Failed to load TLS certificate.";

    private static final String CERTIFICATE_DIRECTORY = "certificate/";
    private static final String TLS_CERTIFICATE = "public_certificate.p12";

    private final RestTemplate restTemplate;

    /**
     * Creates a new {@link RequestHandler} instance.
     */
    public RequestHandler() {
        // Configure REST template
        restTemplate = new RestTemplate(getRequestFactory());
        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
        messageConverter.setObjectMapper(ObjectJsonParser.getDefaultObjectMapper());
        restTemplate.setMessageConverters(Collections.singletonList(messageConverter));
    }

    /**
     * Handles a HTTP request to the server for a single element.
     *
     * @param requestEntity  the request entity to be transmitted to the server
     * @param expectedStatus the http status that is expected on success
     * @return the body of the response entity to the server request
     * @throws RequestHandlerException if an error occurs when requesting the server
     */
    protected S handle(RequestEntity<T> requestEntity, HttpStatus expectedStatus) throws RequestHandlerException {
        ParameterizedTypeReference<S> typeReference = new ParameterizedTypeReference<S>() {
        };
        return executeExchange(requestEntity, expectedStatus, typeReference);
    }

    /**
     * Handles a HTTP request to the server for multiple elements.
     *
     * @param requestEntity  the request entity to be transmitted to the server
     * @param expectedStatus the http status that is expected on success
     * @return the body of the response entity to the server request
     * @throws RequestHandlerException if an error occurs when requesting the server
     */
    protected Iterable<S> handleIterable(RequestEntity<T> requestEntity, HttpStatus expectedStatus)
            throws RequestHandlerException {
        ParameterizedTypeReference<S[]> typeReference = new ParameterizedTypeReference<S[]>() {
        };
        return Arrays.asList(executeExchange(requestEntity, expectedStatus, typeReference));
    }

    /**
     * Executes the request to the server.
     *
     * @param requestEntity  the request entity to be transmitted to the server
     * @param expectedStatus the http status that is expected on success
     * @param typeReference  return type reference of the server request
     * @param <U>            the return type of the server request
     * @return the body of the response entity to the server request
     * @throws RequestHandlerException if an error occurs when requesting the server
     */
    private <U> U executeExchange(RequestEntity<T> requestEntity, HttpStatus expectedStatus,
                                  ParameterizedTypeReference<U> typeReference) throws RequestHandlerException {
        ResponseEntity<U> responseEntity;
        try {
            responseEntity = restTemplate.exchange(Objects.requireNonNull(requestEntity), typeReference);
        } catch (RestClientException exception) {
            throw new RequestHandlerException(ERROR_MESSAGE_REQUEST + exception.getMessage());
        }
        if (responseEntity.getStatusCode().equals(Objects.requireNonNull(expectedStatus))) {
            return responseEntity.getBody();
        }
        throw new RequestHandlerException(ERROR_MESSAGE_REQUEST + ERROR_MESSAGE_STATUS_CODE
                + responseEntity.getStatusCodeValue());
    }

    /**
     * Gets a new instance of {@link HttpComponentsClientHttpRequestFactory} used for TLS communication.
     *
     * @return the {@link HttpComponentsClientHttpRequestFactory} instance
     */
    private HttpComponentsClientHttpRequestFactory getRequestFactory() {
        // Create SSL context for valid HTTPS requests
        SSLContext sslContext = null;
        try {
            sslContext = SSLContextBuilder
                    .create()
                    .loadTrustMaterial(getCertificate())
                    .build();
        } catch (NoSuchAlgorithmException | IOException | CertificateException | KeyStoreException | KeyManagementException e) {
            e.printStackTrace();
        }

        // Create HTTP client with given SSL context for HTTP request execution
        CloseableHttpClient closeableHttpClient = HttpClients
                .custom()
                .setSSLContext(sslContext)
                .build();

        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setHttpClient(closeableHttpClient);
        return requestFactory;
    }

    /**
     * Gets the TLS certificate.
     *
     * @return the TLS certificate
     */
    private File getCertificate() {
        File certificate;
        AssetManager assetManager = MeetEatApplication.getAppContext().getAssets();
        try (AssetFileDescriptor assetFileDescriptor = assetManager.openFd(CERTIFICATE_DIRECTORY + TLS_CERTIFICATE)) {
            String path = FileHandler.readInputStreamToString(assetFileDescriptor.createInputStream());
            certificate = new File(path);
        } catch (IOException exception) {
            throw new IllegalStateException(ERROR_MESSAGE_CERTIFICATE);
        }
        return certificate;
    }
}
