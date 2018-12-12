package io.getstream.core.options;

import com.google.common.collect.Lists;
import io.getstream.core.http.Request;

import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;

public final class EnrichmentFlags implements RequestOption {
    enum OpType {
        OWN_CHILDREN("with_own_children"),
        OWN_REACTIONS("with_own_reactions"),
        REACTION_COUNTS("with_reaction_counts"),
        RECENT_REACTIONS("with_recent_reactions");

        private String operator;

        OpType(String operator) {
            this.operator = operator;
        }

        @Override
        public String toString() {
            return operator;
        }
    }

    private static final class OpEntry {
        String type;
        boolean value;

        OpEntry(OpType type, boolean value) {
            this.type = type.toString();
            this.value = value;
        }
    }

    private final List<OpEntry> ops = Lists.newArrayList();

    public EnrichmentFlags withOwnReactions(boolean value) {
        ops.add(new OpEntry(OpType.OWN_REACTIONS, value));
        return this;
    }

    public EnrichmentFlags withRecentReactions(boolean value) {
        ops.add(new OpEntry(OpType.RECENT_REACTIONS, value));
        return this;
    }

    public EnrichmentFlags withReactionCounts(boolean value) {
        ops.add(new OpEntry(OpType.REACTION_COUNTS, value));
        return this;
    }

    public EnrichmentFlags withOwnChildren(boolean value) {
        ops.add(new OpEntry(OpType.OWN_CHILDREN, value));
        return this;
    }

    @Override
    public void apply(Request.Builder builder) {
        for (OpEntry op : ops) {
            builder.addQueryParameter(op.type, Boolean.toString(op.value));
        }
    }
}
