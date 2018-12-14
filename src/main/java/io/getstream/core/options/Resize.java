package io.getstream.core.options;

import io.getstream.core.http.Request;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public final class Resize implements RequestOption {
    public enum Type {
        CLIP("clip"),
        CROP("crop"),
        SCALE("scale"),
        FILL("fill");

        private final String type;

        Type(String type) {
            this.type = type;
        }

        @Override
        public String toString() {
            return type;
        }
    }

    private final Type type;
    private final int width;
    private final int height;

    public Resize(int width, int height, Type type) {
        checkNotNull(type, "Missing resize type");
        checkArgument(width > 0, "Width should be a positive number");
        checkArgument(height > 0, "Height should be a positive number");
        this.type = type;
        this.width = width;
        this.height = height;

    }

    public Resize(int width, int height) {
        this(width, height, Type.CLIP);
    }

    public Type getType() {
        return type;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    @Override
    public void apply(Request.Builder builder) {
        builder.addQueryParameter("resize", type.toString());
        builder.addQueryParameter("w", Integer.toString(width));
        builder.addQueryParameter("h", Integer.toString(height));
    }
}
