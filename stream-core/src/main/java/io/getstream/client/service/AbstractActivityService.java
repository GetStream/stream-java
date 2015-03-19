/**

 Copyright (c) 2015, Alessandro Pieri
 All rights reserved.

 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions are met:

 1. Redistributions of source code must retain the above copyright notice, this
 list of conditions and the following disclaimer.
 2. Redistributions in binary form must reproduce the above copyright notice,
 this list of conditions and the following disclaimer in the documentation
 and/or other materials provided with the distribution.

 THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

 The views and conclusions contained in the software and documentation are those
 of the authors and should not be interpreted as representing official policies,
 either expressed or implied, of the FreeBSD Project.

 */
package io.getstream.client.service;

import io.getstream.client.exception.StreamClientException;
import io.getstream.client.model.activities.BaseActivity;
import io.getstream.client.model.beans.StreamResponse;
import io.getstream.client.model.feeds.BaseFeed;
import io.getstream.client.repo.StreamRepository;

import java.io.IOException;
import java.util.List;

/**
 * Provides operations to be performed against activities.
 *
 * @param <T>
 */
public abstract class AbstractActivityService<T extends BaseActivity> {
    protected Class<T> type;
    protected final BaseFeed feed;
    protected final StreamRepository streamRepository;

    public AbstractActivityService(BaseFeed feed, Class type, StreamRepository streamRepository) {
        this.type = type;
        this.feed = feed;
        this.streamRepository = streamRepository;
    }

    /**
     * Add a new activity of type {@link T}.
     *
     * @param activity Activity to add.
     * @return Response activity of type {@link T} coming from the server.<br/>
     *         The returning activity in the 'to' field contains the targetFeedId along
     *         with its signature (e.g: 'user:1 6mQhuzQ79e0rZ17bSq1CCxXoRac')
     * @throws IOException
     * @throws StreamClientException
     */
    public T addActivity(T activity) throws IOException, StreamClientException {
        return streamRepository.addActivity(this.feed, activity);
    }

    public StreamResponse<T> addActivities(List<T> activities) throws IOException, StreamClientException {
        return streamRepository.addActivities(this.feed, type, activities);
    }
}
