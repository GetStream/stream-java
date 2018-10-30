package io.getstream.client.model.beans;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.getstream.client.model.activities.BaseActivity;

import java.util.List;

/**
 * This custom activity is required to perform the <b>update_to_targets</b> operation.
 */
public class UpdateTo extends BaseActivity {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("new_targets")
    private List<String> newTargets;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("added_targets")
    private List<String> addedTargets;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("removed_targets")
    private List<String> removedTargets;

    public List<String> getNewTargets() {
        return newTargets;
    }

    public void setNewTargets(List<String> newTargets) {
        this.newTargets = newTargets;
    }

    public List<String> getAddedTargets() {
        return addedTargets;
    }

    public void setAddedTargets(List<String> addedTargets) {
        this.addedTargets = addedTargets;
    }

    public List<String> getRemovedTargets() {
        return removedTargets;
    }

    public void setRemovedTargets(List<String> removedTargets) {
        this.removedTargets = removedTargets;
    }
}
