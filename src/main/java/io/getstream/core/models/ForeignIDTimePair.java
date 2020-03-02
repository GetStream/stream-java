package io.getstream.core.models;

import com.google.common.base.MoreObjects;
import java.util.Date;
import java.util.Objects;

public final class ForeignIDTimePair {
  private final String foreignID;
  private final Date time;

  public ForeignIDTimePair(String foreignID, Date time) {
    this.foreignID = foreignID;
    this.time = time;
  }

  public String getForeignID() {
    return foreignID;
  }

  public Date getTime() {
    return time;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ForeignIDTimePair that = (ForeignIDTimePair) o;
    return Objects.equals(foreignID, that.foreignID) && Objects.equals(time, that.time);
  }

  @Override
  public int hashCode() {
    return Objects.hash(foreignID, time);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("foreignID", this.foreignID)
        .add("time", this.time)
        .toString();
  }
}
