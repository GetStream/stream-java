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
package io.getstream.client.model.feeds;

import io.getstream.client.exception.StreamClientException;
import io.getstream.client.model.activities.BaseActivity;
import io.getstream.client.model.beans.FeedFollow;
import io.getstream.client.model.beans.FollowMany;
import io.getstream.client.model.filters.FeedFilter;
import io.getstream.client.service.AggregatedActivityServiceImpl;
import io.getstream.client.service.FlatActivityServiceImpl;
import io.getstream.client.service.NotificationActivityServiceImpl;
import io.getstream.client.service.UserActivityServiceImpl;

import java.io.IOException;
import java.util.List;

/**
 * Feed class. It exposes operations to perform against a feed.
 */
public interface Feed {

    /**
     * Get the feed ID.
     *
     * @return Feed ID
     */
    String getId();

    /**
     * Get the feed token.
     *
     * @return Token
     */
    String getToken();

    /**
     * Generate a JWT token to perform readonly operations
     * @return Token
     */
    String getReadOnlyToken();

    /**
     * Follows the given target feed.
     *
     * @param feedSlug the slug of the target feed.
     * @param userId user id
     * @throws IOException in case of network/socket exceptions
     * @throws StreamClientException in case of functional or server-side exception
     */
    void follow(String feedSlug, String userId) throws IOException, StreamClientException;

    /**
     * Follows the given target feed.
     *
     * @param feedSlug the slug of the target feed.
     * @param userId user id
     * @param activityCopyLimit How many activities should be copied from the target feed
     * @throws IOException in case of network/socket exceptions
     * @throws StreamClientException in case of functional or server-side exception
     */
    void follow(String feedSlug, String userId, int activityCopyLimit) throws IOException, StreamClientException;

    /**
     * Follow many feed in one shot.
     *
     * @param follows A {@link FollowMany} object which contains a list of sources and targets
     * @param activityCopyLimit the maximum number of activities from a
     *                          source feed that must be copied to a target feed
     * @throws StreamClientException in case of functional or server-side exception
     * @throws IOException in case of network/socket exceptions
     */
    void followMany(FollowMany follows, int activityCopyLimit) throws IOException, StreamClientException;

    /**
     * Follow many feed in one shot.
     * Default activity copy limit is set to 300. Maximum 300 activities from a given source feed
     * will be copied to the target feed.
     *
     * @param follows A {@link FollowMany} object which contains a list of sources and targets
     * @throws StreamClientException in case of functional or server-side exception
     * @throws IOException in case of network/socket exceptions
     */
    void followMany(FollowMany follows) throws IOException, StreamClientException;

    /**
     * Unfollow the given target feed.
     *
     * @param feedSlug the slug of the target feed.
     * @param userId user id
     * @throws IOException in case of network/socket exceptions
     * @throws StreamClientException in case of functional or server-side exception
     */
    void unfollow(String feedSlug, String userId) throws IOException, StreamClientException;

    /**
     * Unfollow the given target feed.
     *
     * @param feedSlug the slug of the target feed.
     * @param userId user id
     * @param keepHistory Whether the activities from the unfollowed feed should be removed
     * @throws IOException in case of network/socket exceptions
     * @throws StreamClientException in case of functional or server-side exception
     */
    void unfollow(String feedSlug, String userId, boolean keepHistory) throws IOException, StreamClientException;

    /**
     * Lists the followers of the feed.
     *
     * @return List of followers
     * @throws IOException in case of network/socket exceptions
     * @throws StreamClientException in case of functional or server-side exception
     */
    List<FeedFollow> getFollowers() throws IOException, StreamClientException;

    /**
     * Lists the followers of the feed using the given filter.
     *
     * @param filter Filter out the followers.
     * @return List of followers
     * @throws IOException in case of network/socket exceptions
     * @throws StreamClientException in case of functional or server-side exception
     */
    List<FeedFollow> getFollowers(FeedFilter filter) throws IOException, StreamClientException;

    /**
     * List the feeds which this feed is following.
     *
     * @return List of following
     * @throws IOException in case of network/socket exceptions
     * @throws StreamClientException in case of functional or server-side exception
     */
    List<FeedFollow> getFollowing() throws IOException, StreamClientException;

    /**
     * List the feeds which this feed is following using the give filter.
     *
     * @param filter Filter out the list of following feeds.
     * @return List of following
     * @throws IOException in case of network/socket exceptions
     * @throws StreamClientException in case of functional or server-side exception
     */
    List<FeedFollow> getFollowing(FeedFilter filter) throws IOException, StreamClientException;

    /**
     * Removes an activity from the feed.
     *
     * @param activityId the activity id to remove from this feed.
     * @throws IOException in case of network/socket exceptions
     * @throws StreamClientException in case of functional or server-side exception
     */
    void deleteActivity(String activityId) throws IOException, StreamClientException;


    /**
     * Removes an activity from the feed.
     *
     * @param foreignId the activity id to remove from this feed.
     * @throws IOException in case of network/socket exceptions
     * @throws StreamClientException in case of functional or server-side exception
     */
    void deleteActivityByForeignId(String foreignId) throws IOException, StreamClientException;


    /**
     * Get mediator service to handle aggregated activities.
     *
     * @param clazz Subtype of {@link BaseActivity} representing the activity type to handle.
     * @param <T> Subtype of {@link BaseActivity} representing the activity type to handle.
     * @return New activity service
     */
    <T extends BaseActivity> FlatActivityServiceImpl<T> newFlatActivityService(Class<T> clazz);

    /**
     * Get mediator service to handle aggregated activities.
     *
     * @param clazz Subtype of {@link BaseActivity} representing the activity type to handle.
     * @param <T> Subtype of {@link BaseActivity} representing the activity type to handle.
     * @return New aggregated activity service
     */
    <T extends BaseActivity> AggregatedActivityServiceImpl<T> newAggregatedActivityService(Class<T> clazz);

    /**
     * Get mediator service to handle aggregated activities.
     *
     * @param clazz Subtype of {@link BaseActivity} representing the activity type to handle.
     * @param <T> Subtype of {@link BaseActivity} representing the activity type to handle.
     * @return New user activity service
     */
    <T extends BaseActivity> UserActivityServiceImpl<T> newUserActivityService(Class<T> clazz);

    /**
     * Get mediator service to handle aggregated activities.
     *
     * @param clazz Subtype of {@link BaseActivity} representing the activity type to handle.
     * @param <T> Subtype of {@link BaseActivity} representing the activity type to handle.
     * @return New notification activity service
     */
    <T extends BaseActivity> NotificationActivityServiceImpl<T> newNotificationActivityService(Class<T> clazz);
}
