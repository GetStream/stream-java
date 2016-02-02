package io.getstream.client.apache.repo;

import com.google.common.base.Optional;
import io.getstream.client.config.AuthenticationHandlerConfiguration;
import io.getstream.client.util.HttpSignatureHandler;
import org.apache.http.Header;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.RequestLine;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HttpContext;
import org.tomitribe.auth.signatures.Signature;

import java.io.IOException;
import java.util.Collections;

public class HttpSignatureInterceptor extends HttpSignatureHandler implements HttpRequestInterceptor {

    /**
     * Create a {@link Signature} template object using the incoming credentials.
     *
     * @param authConfig Credentials container
     */
    public HttpSignatureInterceptor(AuthenticationHandlerConfiguration authConfig) {
        super(authConfig);
    }

    @Override
    public void process(HttpRequest httpRequest, HttpContext httpContext) throws HttpException, IOException {
        Optional<Header> appAuthHeader = Optional.fromNullable(httpRequest.getFirstHeader(X_API_KEY_HEADER));
        if (appAuthHeader.isPresent()) {
            final String today = getTodayDate();
            httpRequest.addHeader(DATE_HEADER, today);
            RequestLine requestLine = httpRequest.getRequestLine();
            Signature signature = getSigner().sign(
                    requestLine.getMethod(),
                    requestLine.getUri(),
                    Collections.singletonMap(DATE_HEADER, today)
            );
            httpRequest.addHeader(new BasicHeader(AUTHORIZATION_HEADER, signature.toString()));
        }
    }
}
