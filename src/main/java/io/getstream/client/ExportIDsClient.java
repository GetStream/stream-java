package io.getstream.client;

import static io.getstream.core.utils.Auth.buildDataPrivacy;
import static io.getstream.core.utils.Serialization.deserialize;

import io.getstream.core.ExportIDs;
import io.getstream.core.exceptions.StreamException;
import io.getstream.core.http.Token;
import io.getstream.core.models.ExportIDsResponse;
import io.getstream.core.utils.Auth;
import java8.util.concurrent.CompletableFuture;

public class ExportIDsClient {
    private final String secret;
    private final ExportIDs exportIDs;

    ExportIDsClient(String secret, ExportIDs exportIDs) {
        this.secret = secret;
        this.exportIDs = exportIDs;
    }

    public CompletableFuture<ExportIDsResponse> exportUserActivities(String userId) throws StreamException {
        final Token token = buildDataPrivacy(secret, Auth.TokenAction.READ);
        return exportIDs.exportUserActivities(token, userId);
    }
}