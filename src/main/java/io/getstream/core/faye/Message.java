package io.getstream.core.faye;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.common.base.MoreObjects;
import java.util.Map;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Message {
  private String id;
  private final String channel;
  private String clientId;
  private String connectionType;
  private String version;
  private String minimumVersion;
  private String[] supportedConnectionTypes;
  private Advice advice;
  private Boolean successful;
  private String subscription;
  private Map<String, Object> data;
  private Map<String, Object> ext;
  private String error;

  // for deserialization
  public Message() {
    this.channel = null;
  }

  public Message(String channel) {
    this.channel = channel;
  }

  public void setId(String id) {
    this.id = id;
  }

  public void setClientId(String clientId) {
    this.clientId = clientId;
  }

  public void setConnectionType(String connectionType) {
    this.connectionType = connectionType;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public void setMinimumVersion(String minimumVersion) {
    this.minimumVersion = minimumVersion;
  }

  public void setSupportedConnectionTypes(String[] supportedConnectionTypes) {
    this.supportedConnectionTypes = supportedConnectionTypes;
  }

  public void setAdvice(Advice advice) {
    this.advice = advice;
  }

  public void setSuccessful(Boolean successful) {
    this.successful = successful;
  }

  public void setSubscription(String subscription) {
    this.subscription = subscription;
  }

  public void setData(Map<String, Object> data) {
    this.data = data;
  }

  public void setExt(Map<String, Object> ext) {
    this.ext = ext;
  }

  public void setError(String error) {
    this.error = error;
  }

  public String getId() {
    return id;
  }

  public String getChannel() {
    return channel;
  }

  public String getClientId() {
    return clientId;
  }

  public String getConnectionType() {
    return connectionType;
  }

  public String getVersion() {
    return version;
  }

  public String getMinimumVersion() {
    return minimumVersion;
  }

  public String[] getSupportedConnectionTypes() {
    return supportedConnectionTypes;
  }

  public Advice getAdvice() {
    return advice;
  }

  public Boolean isSuccessful() {
    return successful;
  }

  public String getSubscription() {
    return subscription;
  }

  public Map<String, Object> getData() {
    return data;
  }

  public Map<String, Object> getExt() {
    return ext;
  }

  public String getError() {
    return error;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Message that = (Message) o;
    return Objects.equals(id, that.id)
        && Objects.equals(channel, that.channel)
        && Objects.equals(clientId, that.clientId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, channel, clientId);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .omitNullValues()
        .add("id", this.id)
        .add("channel", this.channel)
        .add("clientId", this.clientId)
        .add("connectionType", this.connectionType)
        .add("version", this.version)
        .add("minimumVersion", this.minimumVersion)
        .add("supportedConnectionTypes", this.supportedConnectionTypes)
        .add("advice", this.advice)
        .add("successful", this.successful)
        .add("data", this.data)
        .add("ext", this.ext)
        .add("error", this.error)
        .toString();
  }
}
