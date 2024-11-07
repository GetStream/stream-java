package io.getstream.client;

import io.getstream.core.BatchDeleteActivities;
import io.getstream.core.exceptions.StreamException;
import io.getstream.core.http.Token;
import io.getstream.core.models.BatchDeleteActivitiesRequest;
import io.getstream.core.utils.Auth;
import java8.util.concurrent.CompletableFuture;

import static io.getstream.core.utils.Auth.buildDataPrivacyToken;
import static io.getstream.core.utils.Auth.buildModerationToken;

public class BatchDeleteActivitiesClient {
    private final String secret;
    private final BatchDeleteActivities batchDeleteActivities;

    public BatchDeleteActivitiesClient(String secret, BatchDeleteActivities batchDeleteActivities) {
        this.secret = secret;
        this.batchDeleteActivities = batchDeleteActivities;
    }

    public CompletableFuture<Object> deleteActivities(BatchDeleteActivitiesRequest request) throws StreamException {
        final Token token =buildDataPrivacyToken(secret, Auth.TokenAction.WRITE); // Assuming Token can be created with a secret

        return batchDeleteActivities.deleteActivities(token, request);
    }
}