package io.getstream.client.service;

import io.getstream.client.model.activities.BaseActivity;
import io.getstream.client.model.feeds.BaseFeed;
import io.getstream.client.model.activities.SimpleActivity;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.List;

public interface StreamRepository {

    /**
     * Add a new activity.
     * @param feed
     * @param activity
     * @param <T>
     * @throws IOException
     * @throws SignatureException
     * @throws InvalidKeyException
     * @throws NoSuchAlgorithmException
     */
    <T extends BaseActivity> void addActivity(BaseFeed feed, T activity) throws IOException, SignatureException, InvalidKeyException, NoSuchAlgorithmException;

    /**
     * Add a list of activities.
     * @param id
     * @return
     * @throws IOException
     */
    List<SimpleActivity> getActivities(String id) throws IOException;
}
