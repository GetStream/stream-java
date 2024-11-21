package io.getstream.core.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReactionBatch {

    @JsonProperty("reactions")
    private List<Reaction> reactions;

    @JsonProperty("duration")
    private String duration;

    public ReactionBatch() {
    }

    public ReactionBatch(List<Reaction> reactions) {
        this.reactions = reactions;
    }

    public List<Reaction> getReactions() {
        return reactions;
    }

    public void setReactions(List<Reaction> reactions) {
        this.reactions = reactions;
    }
}