package io.getstream.client;

import com.google.common.collect.Iterables;
import io.getstream.core.StreamBatch;
import io.getstream.core.exceptions.StreamException;
import io.getstream.core.http.Token;
import io.getstream.core.models.Activity;
import io.getstream.core.models.FeedID;
import io.getstream.core.models.FollowRelation;
import io.getstream.core.models.ForeignIDTimePair;
import io.getstream.core.utils.DefaultOptions;
import java8.util.concurrent.CompletableFuture;

import java.util.List;

import static io.getstream.core.utils.Auth.*;

public final class BatchClient {
    private final String secret;
    private final StreamBatch batch;

    BatchClient(String secret, StreamBatch batch) {
        this.secret = secret;
        this.batch = batch;
    }

    public CompletableFuture<Void> addToMany(Activity activity, FeedID... feeds) throws StreamException {
        final Token token = buildFeedToken(secret, TokenAction.WRITE);
        return batch.addToMany(token, activity, feeds);
    }

    public CompletableFuture<Void> followMany(int activityCopyLimit, FollowRelation... follows) throws StreamException {
        final Token token = buildFollowToken(secret, TokenAction.WRITE);
        return batch.followMany(token, activityCopyLimit, follows);
    }

    public CompletableFuture<Void> followMany(int activityCopyLimit, Iterable<FollowRelation> follows) throws StreamException {
        return followMany(activityCopyLimit, Iterables.toArray(follows, FollowRelation.class));
    }

    public CompletableFuture<Void> followMany(FollowRelation... follows) throws StreamException {
        return followMany(DefaultOptions.DEFAULT_ACTIVITY_COPY_LIMIT, follows);
    }

    public CompletableFuture<Void> followMany(Iterable<FollowRelation> follows) throws StreamException {
        return followMany(Iterables.toArray(follows, FollowRelation.class));
    }

    public CompletableFuture<Void> unfollowMany(FollowRelation... follows) throws StreamException {
        final Token token = buildFollowToken(secret, TokenAction.WRITE);
        return batch.unfollowMany(token, follows);
    }

    public CompletableFuture<List<Activity>> getActivitiesByID(Iterable<String> activityIDs) throws StreamException {
        return getActivitiesByID(Iterables.toArray(activityIDs, String.class));
    }

    public CompletableFuture<List<Activity>> getActivitiesByID(String... activityIDs) throws StreamException {
        final Token token = buildActivityToken(secret, TokenAction.READ);
        return batch.getActivitiesByID(token, activityIDs);
    }

    public CompletableFuture<List<Activity>> getActivitiesByForeignID(Iterable<ForeignIDTimePair> activityIDTimePairs) throws StreamException {
        return getActivitiesByForeignID(Iterables.toArray(activityIDTimePairs, ForeignIDTimePair.class));
    }

    public CompletableFuture<List<Activity>> getActivitiesByForeignID(ForeignIDTimePair... activityIDTimePairs) throws StreamException {
        final Token token = buildActivityToken(secret, TokenAction.READ);
        return batch.getActivitiesByForeignID(token, activityIDTimePairs);
    }

    public CompletableFuture<Void> updateActivities(Iterable<Activity> activities) throws StreamException {
        return updateActivities(Iterables.toArray(activities, Activity.class));
    }

    public CompletableFuture<Void> updateActivities(Activity... activities) throws StreamException {
        final Token token = buildActivityToken(secret, TokenAction.WRITE);
        return batch.updateActivities(token, activities);
    }
}
