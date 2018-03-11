package io.getstream.client.okhttp.repo;

import java.io.IOException;
import java.util.Collections;

import org.tomitribe.auth.signatures.Signature;

import com.google.common.base.Optional;
import io.getstream.client.config.AuthenticationHandlerConfiguration;
import io.getstream.client.util.HttpSignatureHandler;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class HttpSignatureinterceptor extends HttpSignatureHandler implements Interceptor {

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
                    request.url().toString(),
                    Collections.singletonMap(DATE_HEADER, today)
            );
            newRequest.addHeader(AUTHORIZATION_HEADER, signature.toString());
            request = newRequest.build();
        }
        return chain.proceed(request);
    }
}
