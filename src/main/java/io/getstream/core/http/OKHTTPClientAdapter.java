package io.getstream.core.http;

import io.getstream.core.utils.Info;
import okhttp3.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.concurrent.CompletableFuture;

import static com.google.common.base.Preconditions.checkNotNull;

public final class OKHTTPClientAdapter extends HTTPClient {
    private static final String userAgentTemplate = "okhttp3 stream-java2 %s v%s";

    private final OkHttpClient client;

    public OKHTTPClientAdapter() {
        this.client = new OkHttpClient.Builder()
                .followRedirects(false)
                .followSslRedirects(false)
                .build();
    }

    public OKHTTPClientAdapter(OkHttpClient client) {
        checkNotNull(client);
        this.client = client;
    }

    @Override
    public <T> T getImplementation() {
        return (T) client;
    }

    private okhttp3.RequestBody buildOkHttpRequestBody(io.getstream.core.http.RequestBody body) {
        okhttp3.RequestBody okBody = null;
        MediaType mediaType;
        switch (body.getType()) {
            case JSON:
                mediaType = MediaType.parse(body.getType().toString());
                okBody = okhttp3.RequestBody.create(mediaType, body.getBytes());
                break;
            case MULTI_PART:
                String mimeType = URLConnection.guessContentTypeFromName(body.getFileName());
                mediaType = MediaType.parse(mimeType);
                MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
                if (body.getBytes() != null) {
                    builder.addFormDataPart("file", body.getFileName(), okhttp3.RequestBody.create(mediaType, body.getBytes()));
                } else {
                    builder.addFormDataPart("file", body.getFileName(), okhttp3.RequestBody.create(mediaType, body.getFile()));
                }
                okBody = builder.build();
                break;
        }
        return okBody;
    }

    private okhttp3.Request buildOkHttpRequest(io.getstream.core.http.Request request) {
        String version = Info.getProperties().getProperty(Info.VERSION);
        String userAgent = String.format(userAgentTemplate, System.getProperty("os.name"), version);
        okhttp3.Request.Builder builder = new okhttp3.Request.Builder()
                .url(request.getURL())
                .addHeader("Stream-Auth-Type", "jwt")
                .addHeader("Authorization", request.getToken().toString())
                .addHeader("User-Agent", userAgent);

        MediaType mediaType;
        switch (request.getMethod()) {
            case GET:
                builder.get();
                break;
            case DELETE:
                builder.delete();
                break;
            case PUT:
                builder.put(buildOkHttpRequestBody(request.getBody()));
                break;
            case POST:
                builder.post(buildOkHttpRequestBody(request.getBody()));
                break;
        }
        return builder.build();
    }

    private io.getstream.core.http.Response buildResponse(okhttp3.Response response) {
        final InputStream body = response.body() != null ? response.body().byteStream() : null;
        return new io.getstream.core.http.Response(response.code(), body);
    }

    @Override
    public CompletableFuture<io.getstream.core.http.Response> execute(io.getstream.core.http.Request request) {
        final CompletableFuture<io.getstream.core.http.Response> result = new CompletableFuture<>();

        client.newCall(buildOkHttpRequest(request)).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                result.completeExceptionally(e);
            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) {
                io.getstream.core.http.Response httpResponse = buildResponse(response);
                try (InputStream ignored = httpResponse.getBody()) {
                    result.complete(httpResponse);
                } catch (Exception e) {
                    result.completeExceptionally(e);
                }
            }
        });

        return result;
    }
}

