package io.getstream.client.okhttp.repo;

import com.google.common.base.Optional;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import io.getstream.client.config.AuthenticationHandlerConfiguration;
import io.getstream.client.util.HttpSignatureHandler;
import org.tomitribe.auth.signatures.Signature;

import java.io.IOException;
import java.util.Collections;

public class HttpSignatureinterceptor extends HttpSignatureHandler implements Interceptor{

    /**
     * Create a {@link Signature} template object using the incoming credentials.
     *
     * @param authConfig Credentials container
     */
    public HttpSignatureinterceptor(AuthenticationHandlerConfiguration authConfig) {
        super(authConfig);
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Optional<String> appAuthHeader = Optional.fromNullable(request.header(X_API_KEY_HEADER));
        if (appAuthHeader.isPresent()) {
            final Request.Builder newRequest = request.newBuilder();
            final String today = getTodayDate();
            newRequest.addHeader(DATE_HEADER, today);
            Signature signature = getSigner().sign(
                    request.method(),
                    request.urlString(),
                    Collections.singletonMap(DATE_HEADER, today)
            );
            newRequest.addHeader(AUTHORIZATION_HEADER, signature.toString());
            request = newRequest.build();
        }
        return chain.proceed(request);
    }
}
