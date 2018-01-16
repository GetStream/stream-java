package io.getstream.client.model.filters;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class FeedFilterTest {

    @Test
    public void shouldBuildFeedFilter() {
        int limit = 3;
        int offset = 4;
        String id_gt = "abc";
        String id_gte = "def";
        String id_lt = "ghi";
        String id_lte = "jkl";
        String ranking  = "ranking-method";
        String session = "session";
        List<String> feedIds = Arrays.asList("user:123", "user:456");

        FeedFilter filter = new FeedFilter.Builder()
            .withLimit(limit)
            .withOffset(offset)
            .withIdGreaterThan(id_gt)
            .withIdGreaterThanEquals(id_gte)
            .withIdLowerThan(id_lt)
            .withIdLowerThanEquals(id_lte)
            .withRanking(ranking)
            .withFeedIds(feedIds)
            .withSession(session)
            .build();

        assertThat(filter.getLimit(), is(limit));
        assertThat(filter.getOffset(), is(offset));
        assertThat(filter.getIdGreaterThan(), is(id_gt));
        assertThat(filter.getIdGreaterThanEquals(), is(id_gte));
        assertThat(filter.getIdLowerThan(), is(id_lt));
        assertThat(filter.getIdLowerThanEquals(), is(id_lte));
        assertThat(filter.getRanking(), is(ranking));
        assertThat(filter.getFeedIds(), is(feedIds));
        assertThat(filter.getSession(), is(session));
    }

    @Test
    public void shouldBuildFeedFilterDefaults() {

        FeedFilter filter = new FeedFilter.Builder().build();

        assertThat(filter.getLimit(), is(25));
        assertThat(filter.getOffset(), is(nullValue()));
        assertThat(filter.getIdGreaterThan(), is(nullValue()));
        assertThat(filter.getIdGreaterThanEquals(), is(nullValue()));
        assertThat(filter.getIdLowerThan(), is(nullValue()));
        assertThat(filter.getIdLowerThanEquals(), is(nullValue()));
        assertThat(filter.getRanking(), is(nullValue()));
        assertThat(filter.getFeedIds(), is(nullValue()));
    }

}
