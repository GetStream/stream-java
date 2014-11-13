package io.getstream.client.service;

import com.google.common.base.Optional;
import io.getstream.client.StreamClient;
import io.getstream.client.model.BaseActivity;
import io.getstream.client.model.BaseFeed;
import io.getstream.client.model.Feed;

import javax.activity.ActivityCompletedException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;

public interface StreamRepository {

    <T extends BaseActivity> void addActivity(BaseFeed feed, T activity) throws IOException, SignatureException, InvalidKeyException, NoSuchAlgorithmException;
}
