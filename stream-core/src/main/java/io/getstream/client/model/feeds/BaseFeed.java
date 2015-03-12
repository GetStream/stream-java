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
import io.getstream.client.repo.StreamRepository;
import io.getstream.client.service.AggregatedActivityServiceImpl;
import io.getstream.client.service.FlatActivityServiceImpl;
import io.getstream.client.service.NotificationActivityServiceImpl;
import io.getstream.client.service.UserActivityServiceImpl;

import java.io.IOException;
import java.util.List;

/**
 * Provide basic operation to perform against a feed.
 */
public class BaseFeed implements Feed {

    protected final StreamRepository streamRepository;
    protected final String feedSlug;
    protected final String userId;
    private final String id;

    /**
     * Create a new feed using the given slug and user id.
     *
     * @param streamRepository Provide data repository to perform actual operation on a given feed.
     *                         It must implement {@link StreamRepository}.
     * @param feedSlug
     * @param userId
     */
    public BaseFeed(StreamRepository streamRepository, String feedSlug, String userId) {
        this.streamRepository = streamRepository;
        this.feedSlug = feedSlug;
        this.userId = userId;
        this.id = feedSlug.concat(":").concat(userId);
    }

    @Override
    public void follow(String feedSlug, String userId) throws IOException, StreamClientException {
        String feedId = String.format("%s:%s", feedSlug, userId);
        streamRepository.follow(this, feedId);
    }

    @Override
    public void unfollow(String feedSlug, String userId) throws IOException, StreamClientException {
        String feedId = String.format("%s:%s", feedSlug, userId);
        streamRepository.unfollow(this, feedId);
    }

    @Override
    public List<FeedFollow> getFollowers() throws IOException, StreamClientException {
        return streamRepository.getFollowers(this, new FeedFilter.Builder().build());
    }

    @Override
    public List<FeedFollow> getFollowers(FeedFilter filter) throws IOException, StreamClientException {
        return streamRepository.getFollowers(this, filter);
    }

    @Override
    public List<FeedFollow> getFollowing() throws IOException, StreamClientException {
        return streamRepository.getFollowing(this, new FeedFilter.Builder().build());
    }

    @Override
    public List<FeedFollow> getFollowing(FeedFilter filter) throws IOException, StreamClientException {
        return streamRepository.getFollowing(this, filter);
    }

    @Override
    public void deleteActivity(String activityId) throws IOException, StreamClientException {
        streamRepository.deleteActivityById(this, activityId);
    }

    @Override
    public void deleteActivities(List<String> activityIds) throws IOException, StreamClientException {
        for (String activityId : activityIds) {
            streamRepository.deleteActivityById(this, activityId);
        }
    }

    @Override
    public <T extends BaseActivity> AggregatedActivityServiceImpl<T> newAggregatedActivityService(Class<T> clazz) {
        return new AggregatedActivityServiceImpl<>(this, clazz, streamRepository);
    }

    @Override
    public <T extends BaseActivity> FlatActivityServiceImpl<T> newFlatActivityService(Class<T> clazz) {
        return new FlatActivityServiceImpl<>(this, clazz, streamRepository);
    }

    @Override
    public <T extends BaseActivity> UserActivityServiceImpl<T> newUserActivityService(Class<T> clazz) {
        return new UserActivityServiceImpl<>(this, clazz, streamRepository);
    }

    @Override
    public <T extends BaseActivity> NotificationActivityServiceImpl<T> newNotificationActivityService(Class<T> clazz) {
        return new NotificationActivityServiceImpl<>(this, clazz, streamRepository);
    }

    public String getFeedSlug() {
        return feedSlug;
    }

    public String getUserId() {
        return userId;
    }

    public String getId() {
        return id;
    }
}
