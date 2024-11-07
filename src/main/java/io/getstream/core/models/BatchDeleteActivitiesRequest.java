package io.getstream.core.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class BatchDeleteActivitiesRequest {

    private final List<ActivityToDelete> activities;

    public BatchDeleteActivitiesRequest(List<ActivityToDelete> activities) {
        this.activities = activities;
    }

    public List<ActivityToDelete> getActivities() {
        return activities;
    }

    public static class ActivityToDelete {
        private final String id;
        private final List<String> removeFromFeeds;

        public ActivityToDelete(
                @JsonProperty("id") String id,
                @JsonProperty("remove_from_feeds") List<String> removeFromFeeds) {
            this.id = id;
            this.removeFromFeeds = removeFromFeeds;
        }

        public String getId() {
            return id;
        }

        public List<String> getRemoveFromFeeds() {
            return removeFromFeeds;
        }
    }
}