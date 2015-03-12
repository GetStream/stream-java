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
     * Follows the given target feed.
     *
     * @param feedSlug the slug of the target feed.
     * @param userId
     * @throws IOException
     * @throws StreamClientException
     */
    void follow(String feedSlug, String userId) throws IOException, StreamClientException;

    /**
     * Unfollow the given target feed.
     *
     * @param feedSlug the slug of the target feed.
     * @param userId
     * @throws IOException
     * @throws StreamClientException
     */
    void unfollow(String feedSlug, String userId) throws IOException, StreamClientException;

    /**
     * Lists the followers of the feed.
     *
     * @return
     * @throws IOException
     * @throws StreamClientException
     */
    List<FeedFollow> getFollowers() throws IOException, StreamClientException;

    /**
     * Lists the followers of the feed using the given filter.
     *
     * @param filter Filter out the followers.
     * @return
     * @throws IOException
     * @throws StreamClientException
     */
    List<FeedFollow> getFollowers(FeedFilter filter) throws IOException, StreamClientException;

    /**
     * List the feeds which this feed is following.
     *
     * @return
     * @throws IOException
     * @throws StreamClientException
     */
    List<FeedFollow> getFollowing() throws IOException, StreamClientException;

    /**
     * List the feeds which this feed is following using the give filter.
     *
     * @param filter Filter out the list of following feeds.
     * @return
     * @throws IOException
     * @throws StreamClientException
     */
    List<FeedFollow> getFollowing(FeedFilter filter) throws IOException, StreamClientException;

    /**
     * Removes an activity from the feed.
     *
     * @param activityId the activity id to remove from this feed.
     * @throws IOException
     * @throws StreamClientException
     */
    void deleteActivity(String activityId) throws IOException, StreamClientException;

    /**
     * Removes a list of activities from the feed.
     * It is not executed in batch fashion.
     *
     * @param activityIds
     * @throws IOException
     * @throws StreamClientException
     */
    void deleteActivities(List<String> activityIds) throws IOException, StreamClientException;

    /**
     * Get mediator service to handle aggregated activities.
     *
     * @param clazz Subtype of {@link BaseActivity} representing the activity type to handle.
     * @param <T>
     * @return
     */
    <T extends BaseActivity> FlatActivityServiceImpl<T> newFlatActivityService(Class<T> clazz);

    /**
     * Get mediator service to handle aggregated activities.
     *
     * @param clazz Subtype of {@link io.getstream.client.model.activities.BaseActivity} representing the activity type to handle.
     * @return
     */
    <T extends BaseActivity> AggregatedActivityServiceImpl<T> newAggregatedActivityService(Class<T> clazz);

    /**
     * Get mediator service to handle aggregated activities.
     *
     * @param clazz Subtype of {@link BaseActivity} representing the activity type to handle.
     * @param <T>
     * @return
     */
    <T extends BaseActivity> UserActivityServiceImpl<T> newUserActivityService(Class<T> clazz);

    /**
     * Get mediator service to handle aggregated activities.
     *
     * @param clazz Subtype of {@link BaseActivity} representing the activity type to handle.
     * @param <T>
     * @return
     */
    <T extends BaseActivity> NotificationActivityServiceImpl<T> newNotificationActivityService(Class<T> clazz);
}
