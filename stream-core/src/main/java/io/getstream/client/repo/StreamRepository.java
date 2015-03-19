/**

 Copyright (c) 2015, Alessandro Pieri
 All rights reserved.

 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions are met:

 1. Redistributions of source code must retain the above copyright notice, this
 list of conditions and the following disclaimer.
 2. Redistributions in binary form must reproduce the above copyright notice,
 this list of conditions and the following disclaimer in the documentation
 and/or other materials provided with the distribution.

 THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

 The views and conclusions contained in the software and documentation are those
 of the authors and should not be interpreted as representing official policies,
 either expressed or implied, of the FreeBSD Project.

 */
package io.getstream.client.repo;

import io.getstream.client.exception.StreamClientException;
import io.getstream.client.model.activities.AggregatedActivity;
import io.getstream.client.model.activities.BaseActivity;
import io.getstream.client.model.activities.NotificationActivity;
import io.getstream.client.model.beans.FeedFollow;
import io.getstream.client.model.beans.MarkedActivity;
import io.getstream.client.model.beans.StreamResponse;
import io.getstream.client.model.feeds.BaseFeed;
import io.getstream.client.model.filters.FeedFilter;

import java.io.IOException;
import java.util.List;

/**
 * Provide low-level access to the GetStream.io REST API.
 */
public interface StreamRepository {

    /**
     * Delete activity by activity id.
     *
     * @param feed       Feed that contains the activity to be deleted.
     * @param activityId Activity-id to be deleted.
     * @throws IOException
     * @throws StreamClientException
     */
    void deleteActivityById(BaseFeed feed, String activityId) throws IOException, StreamClientException;

    /**
     * Delete activity by foreign id.
     *
     * @param feed       Feed that contains the activity to be deleted.
     * @param foreignId foreignId to be deleted.
     * @throws IOException
     * @throws StreamClientException
     */
    void deleteActivityByForeignId(BaseFeed feed, String foreignId) throws IOException, StreamClientException;

    /**
     * Follow a feed.
     *
     * @param feed         Feed that wants to follow a target feed.
     * @param targetFeedId Feed to follow.
     * @throws StreamClientException
     * @throws IOException
     */
    void follow(BaseFeed feed, String targetFeedId) throws StreamClientException, IOException;

    /**
     * Unfollow a feed.
     *
     * @param feed
     * @param targetFeedId Feed to unfollow.
     * @throws StreamClientException
     * @throws IOException
     */
    void unfollow(BaseFeed feed, String targetFeedId) throws StreamClientException, IOException;

    /**
     * List the feeds which the given feed is following.
     *
     * @param feed
     * @param filter Filter out the following list. Limited to 25 items by default.
     * @return
     * @throws StreamClientException
     * @throws IOException
     */
    List<FeedFollow> getFollowing(BaseFeed feed, FeedFilter filter) throws StreamClientException, IOException;

    /**
     * Lists the followers for the given feed.
     *
     * @param feed
     * @param filter Filter out the followers list. Limited to 25 items by default.
     * @return
     * @throws StreamClientException
     * @throws IOException
     */
    List<FeedFollow> getFollowers(BaseFeed feed, FeedFilter filter) throws StreamClientException, IOException;

    /**
     * Lists the activities in the given feed.
     *
     * @param feed   Feed which the activities belong to
     * @param type   Type of the activity. Must be a subtype of {@link BaseActivity}
     * @param filter Filter out the activities. Limited to 25 items by default.
     * @param <T>
     * @return
     * @throws IOException
     * @throws StreamClientException
     */
    <T extends BaseActivity> StreamResponse<T> getActivities(BaseFeed feed, Class<T> type, FeedFilter filter) throws IOException, StreamClientException;

    /**
     * Add a new activity to the given feed.
     *
     * @param feed     Feed which the activities belong to
     * @param activity Activity to add.
     * @param <T>
     * @return
     * @throws StreamClientException
     * @throws IOException
     */
    <T extends BaseActivity> T addActivity(BaseFeed feed, T activity) throws StreamClientException, IOException;

    /**
     * List aggregated activities.
     *
     * @param feed   Feed which the activities belong to
     * @param type   Type of the activity. Must be a subtype of {@link BaseActivity}
     * @param filter Filter out the activities. Limited to 25 items by default.
     * @param <T>
     * @return
     * @throws IOException
     * @throws StreamClientException
     */
    <T extends BaseActivity> StreamResponse<AggregatedActivity<T>> getAggregatedActivities(BaseFeed feed, Class<T> type, FeedFilter filter) throws IOException, StreamClientException;

    /**
     * List notification activities.
     *
     * @param feed
     * @param type
     * @param filter Filter out the activities.
     * @param <T>
     * @return
     * @throws IOException
     * @throws StreamClientException
     */
    <T extends BaseActivity> StreamResponse<NotificationActivity<T>> getNotificationActivities(BaseFeed feed, Class<T> type, FeedFilter filter) throws IOException, StreamClientException;

    /**
     * List notifications marking the activities as read and/or as seen.
     *
     * @param feed
     * @param type
     * @param filter     Filter the activities.
     * @param markAsRead Mark all the activities as read.
     * @param markAsSeen Mark all the activities as seen.
     * @param <T>
     * @return
     */
    <T extends BaseActivity> StreamResponse<NotificationActivity<T>> getNotificationActivities(BaseFeed feed, Class<T> type, FeedFilter filter, boolean markAsRead, boolean markAsSeen) throws IOException, StreamClientException;

    /**
     * List notifications marking some of them as read and/or as seen.
     * An immutable list of activities to be marked as read and/or as seen can be
     * built using the {@link MarkedActivity} builder.
     *
     * @param feed
     * @param type
     * @param filter     Filter the activities.
     * @param markAsRead List of activities to be marked as read.
     * @param markAsSeen List of activities to be marked as seen.
     * @param <T>
     * @return
     */
    <T extends BaseActivity> StreamResponse<NotificationActivity<T>> getNotificationActivities(BaseFeed feed, Class<T> type, FeedFilter filter, MarkedActivity markAsRead, MarkedActivity markAsSeen) throws IOException, StreamClientException;

    /**
     * Send the shutdown signal to the client.
     *
     * @throws IOException
     */
    void shutdown() throws IOException;
}
