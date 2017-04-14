package io.getstream.client.model.activities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class NotificationActivity<T extends BaseActivity> extends WrappedActivity<T> {
 private boolean isRead;
 private boolean isSeen;

 @JsonIgnore
 public boolean getIsRead() {
  return isRead;
 }

 @JsonProperty("is_read")
 public void setIsRead(boolean isRead) {
  this.isRead = isRead;
 }

 @JsonIgnore
 public boolean getIsSeen() {
  return isSeen;
 }

 @JsonProperty("is_seen")
 public void setIsSeen(boolean isSeen) {
  this.isSeen = isSeen;
 }
}
