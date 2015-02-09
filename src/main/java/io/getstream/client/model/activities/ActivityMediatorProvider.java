package io.getstream.client.model.activities;

import io.getstream.client.service.AbstractActivityService;

public interface ActivityMediatorProvider {
    <T extends BaseActivity> AbstractActivityService<T> newActivityMediator(Class<T> clazz);
}
