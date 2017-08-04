package io.getstream.client.service;

import io.getstream.client.exception.StreamClientException;
import io.getstream.client.model.activities.NotificationActivity;
import io.getstream.client.model.activities.SimpleActivity;
import io.getstream.client.model.beans.MarkedActivity;
import io.getstream.client.model.beans.StreamResponse;
import io.getstream.client.model.feeds.BaseFeed;
import io.getstream.client.model.filters.FeedFilter;
import io.getstream.client.repo.StreamRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class NotificationActivityServiceImplTest {

    @Mock
    private BaseFeed feed;

    @Mock
    private StreamRepository repository;

    @Mock
    private StreamResponse response;

    private NotificationActivityService<SimpleActivity> notificationActivityService;

    @Before
    public void setUp() throws IOException, StreamClientException {
        when(repository.getNotificationActivities(any(BaseFeed.class), eq(SimpleActivity.class), any(FeedFilter.class))).thenReturn(response);
        when(repository.getNotificationActivities(any(BaseFeed.class), eq(SimpleActivity.class), any(FeedFilter.class), anyBoolean(), anyBoolean())).thenReturn(response);
        when(repository.getNotificationActivities(any(BaseFeed.class), eq(SimpleActivity.class), any(FeedFilter.class), any(MarkedActivity.class), any(MarkedActivity.class))).thenReturn(response);
        notificationActivityService = new NotificationActivityServiceImpl<>(feed, SimpleActivity.class, repository);
    }

    @Test
    public void shouldGetActivities() throws Exception {
        StreamResponse<NotificationActivity<SimpleActivity>> activities = notificationActivityService.getActivities();
        verify(repository).getNotificationActivities(any(BaseFeed.class), eq(SimpleActivity.class), any(FeedFilter.class));
        assertThat(activities, notNullValue());
    }

    @Test
    public void shouldGetActivitiesWithFilter() throws Exception {
        FeedFilter filter = new FeedFilter.Builder().build();
        StreamResponse<NotificationActivity<SimpleActivity>> activities = notificationActivityService.getActivities(filter, true, true);
        verify(repository).getNotificationActivities(any(BaseFeed.class), eq(SimpleActivity.class), any(FeedFilter.class), eq(true), eq(true));
        assertThat(activities, notNullValue());
    }

    @Test
    public void shouldGetActivitiesWithMark() throws Exception {
        FeedFilter filter = new FeedFilter.Builder().build();
        MarkedActivity asSeen = new MarkedActivity.Builder().withActivityId("1").build();
        MarkedActivity asRead = new MarkedActivity.Builder().withActivityId("1").build();
        StreamResponse<NotificationActivity<SimpleActivity>> activities = notificationActivityService.getActivities(filter, asSeen, asRead);
        verify(repository).getNotificationActivities(any(BaseFeed.class), eq(SimpleActivity.class), any(FeedFilter.class), eq(asSeen), eq(asRead));
        assertThat(activities, notNullValue());
    }
}