package io.getstream.core.options;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Joiner;
import io.getstream.core.http.Request;
import java8.util.J8Arrays;

public final class Crop implements RequestOption {
  public enum Type {
    TOP("top"),
    BOTTOM("bottom"),
    LEFT("left"),
    RIGHT("right"),
    CENTER("center");

    private final String type;

    Type(String type) {
      this.type = type;
    }

    @Override
    public String toString() {
      return type;
    }
  }

  private final Type[] types;
  private final int width;
  private final int height;

  public Crop(int width, int height, Type... types) {
    checkNotNull(types, "Missing crop type");
    checkArgument(types.length > 0, "Missing crop type");
    checkArgument(width > 0, "Width should be a positive number");
    checkArgument(height > 0, "Height should be a positive number");
    this.types = types;
    this.width = width;
    this.height = height;
  }

  public Crop(int width, int height) {
    this(width, height, Type.CENTER);
  }

  public Type[] getTypes() {
    return types;
  }

  public int getWidth() {
    return width;
  }

  public int getHeight() {
    return height;
  }

  @Override
  public void apply(Request.Builder builder) {
    String[] cropTypes =
        J8Arrays.stream(this.types).map(type -> type.toString()).toArray(String[]::new);
    String types = Joiner.on(",").join(cropTypes);
    builder.addQueryParameter("crop", types);
    builder.addQueryParameter("w", Integer.toString(width));
    builder.addQueryParameter("h", Integer.toString(height));
  }
}
