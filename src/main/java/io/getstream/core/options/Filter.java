package io.getstream.core.options;

import com.google.common.collect.Lists;
import io.getstream.core.http.Request;
import java.util.List;

public final class Filter implements RequestOption {
  enum OpType {
    ID_GREATER_THAN_OR_EQUAL("id_gte"),
    ID_GREATER_THAN("id_gt"),
    ID_LESS_THAN_OR_EQUAL("id_lte"),
    ID_LESS_THAN("id_lt"),
    REFRESH("refresh"),
    DISCARD_DELETED_ACTIVITIES("discard_deleted_activities");

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

  public Filter idGreaterThan(String id) {
    ops.add(new OpEntry(OpType.ID_GREATER_THAN, id));
    return this;
  }

  public Filter idGreaterThanEqual(String id) {
    ops.add(new OpEntry(OpType.ID_GREATER_THAN_OR_EQUAL, id));
    return this;
  }

  public Filter discardDeletedActivities() {
    ops.add(new OpEntry(OpType.DISCARD_DELETED_ACTIVITIES, "true"));
    return this;
  }

  public Filter idLessThan(String id) {
    ops.add(new OpEntry(OpType.ID_LESS_THAN, id));
    return this;
  }

  public Filter idLessThanEqual(String id) {
    ops.add(new OpEntry(OpType.ID_LESS_THAN_OR_EQUAL, id));
    return this;
  }

  public Filter refresh() {
    ops.add(new OpEntry(OpType.REFRESH, "1"));
    return this;
  }

  @Override
  public void apply(Request.Builder builder) {
    for (OpEntry op : ops) {
      builder.addQueryParameter(op.type, op.value);
    }
  }
}
