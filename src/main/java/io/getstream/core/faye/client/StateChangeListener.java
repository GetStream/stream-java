package io.getstream.core.faye.client;

public interface StateChangeListener {
  void onStateChanged(FayeClientState clientState);
}
