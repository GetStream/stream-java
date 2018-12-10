package io.getstream.core.options;

import com.google.common.collect.Lists;
import io.getstream.core.http.Request;

import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;

public final class Pagination implements RequestOption {
    enum OpType {
        OFFSET("offset"),
        LIMIT("limit");

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
        int value;

        OpEntry(OpType type, int value) {
            this.type = type.toString();
            this.value = value;
        }
    }

    private final List<OpEntry> ops = Lists.newArrayList();

    public Pagination limit(int value) {
        checkArgument(value >= 0, "limit can't be negative");
        ops.add(new OpEntry(OpType.LIMIT, value));
        return this;
    }

    public Pagination offset(int value) {
        checkArgument(value >= 0, "offset can't be negative");
        ops.add(new OpEntry(OpType.OFFSET, value));
        return this;
    }

    @Override
    public void apply(Request.Builder builder) {
        for (OpEntry op : ops) {
            builder.addQueryParameter(op.type, Integer.toString(op.value));
        }
    }
}
