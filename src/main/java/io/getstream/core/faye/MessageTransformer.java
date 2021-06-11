package io.getstream.core.faye;

public abstract class MessageTransformer {
    public abstract Message transformRequest(Message message);

    public abstract Message transformResponse(Message message);
}
