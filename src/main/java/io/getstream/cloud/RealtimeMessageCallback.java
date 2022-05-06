package io.getstream.cloud;

import io.getstream.core.models.RealtimeMessage;

public interface RealtimeMessageCallback {
  void onMessage(RealtimeMessage message);
}
