package io.getstream.client.model.activities;

public interface ActivityMediatorProvider {
    <T extends BaseActivity> AbstractActivityMediator<T> newActivityMediator(Class<T> clazz);
}
