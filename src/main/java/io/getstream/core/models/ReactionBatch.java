package io.getstream.core.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReactionBatch {

    @JsonProperty("reactions")
    private Reaction[] reactions;

    public ReactionBatch() {
    }

    public ReactionBatch(Reaction[] reactions) {
        this.reactions = reactions;
    }

    public Reaction[] getReactions() {
        return reactions;
    }

    public void setReactions(Reaction[] reactions) {
        this.reactions = reactions;
    }
}