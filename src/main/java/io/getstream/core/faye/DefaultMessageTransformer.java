package io.getstream.core.faye;

public class DefaultMessageTransformer extends MessageTransformer {
  @Override
  public Message transformRequest(Message message) {
    return message;
  }

  @Override
  public Message transformResponse(Message message) {
    return message;
  }
}
