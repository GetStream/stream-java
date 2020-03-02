package io.getstream.core.options;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.collect.Lists;
import io.getstream.core.http.Request;
import java.util.List;

public final class EnrichmentFlags implements RequestOption {
  enum OpType {
    OWN_CHILDREN("with_own_children"),
    OWN_REACTIONS("with_own_reactions"),
    REACTION_COUNTS("with_reaction_counts"),
    // XXX: move it to a separate option???
    REACTION_KINDS("reaction_kinds_filter"),
    RECENT_REACTIONS("with_recent_reactions"),
    RECENT_REACTIONS_LIMIT("recent_reactions_limit");

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
    String value;

    OpEntry(OpType type, String value) {
      this.type = type.toString();
      this.value = value;
    }
  }

  private final List<OpEntry> ops = Lists.newArrayList();
  private String userID;

  public EnrichmentFlags withOwnReactions() {
    ops.add(new OpEntry(OpType.OWN_REACTIONS, "true"));
    return this;
  }

  public EnrichmentFlags withUserReactions(String userID) {
    checkNotNull(userID, "No user ID");
    checkArgument(!userID.isEmpty(), "No user ID");
    ops.add(new OpEntry(OpType.OWN_REACTIONS, "true"));
    this.userID = userID;
    return this;
  }

  public EnrichmentFlags withRecentReactions() {
    ops.add(new OpEntry(OpType.RECENT_REACTIONS, "true"));
    return this;
  }

  public EnrichmentFlags withRecentReactions(int limit) {
    checkArgument(limit > 0, "Limit should be a positive value");
    ops.add(new OpEntry(OpType.RECENT_REACTIONS_LIMIT, Integer.toString(limit)));
    return withRecentReactions();
  }

  public EnrichmentFlags reactionKindFilter(String... kinds) {
    checkNotNull(kinds, "No kinds to filter by");
    checkArgument(kinds.length > 0, "No kinds to filter by");
    ops.add(new OpEntry(OpType.REACTION_KINDS, String.join(",", kinds)));
    return this;
  }

  public EnrichmentFlags withReactionCounts() {
    ops.add(new OpEntry(OpType.REACTION_COUNTS, "true"));
    return this;
  }

  public EnrichmentFlags withOwnChildren() {
    ops.add(new OpEntry(OpType.OWN_CHILDREN, "true"));
    return this;
  }

  public EnrichmentFlags withUserChildren(String userID) {
    checkNotNull(userID, "No user ID");
    checkArgument(!userID.isEmpty(), "No user ID");
    ops.add(new OpEntry(OpType.OWN_CHILDREN, "true"));
    this.userID = userID;
    return this;
  }

  @Override
  public void apply(Request.Builder builder) {
    for (OpEntry op : ops) {
      builder.addQueryParameter(op.type, op.value);
    }
    if (userID != null) {
      builder.addQueryParameter("user_id", userID);
    }
  }
}
