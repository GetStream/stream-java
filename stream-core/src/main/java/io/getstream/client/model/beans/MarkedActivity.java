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
package io.getstream.client.model.beans;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import io.getstream.client.model.activities.BaseActivity;

import java.util.List;

/**
 * Allow a list of {@link BaseActivity} to be marked.
 * The purpose of the mark is given by the context in which it used for.
 */
public class MarkedActivity {

    private final List<String> activityIds;

    private MarkedActivity(final List<String> ids) {
        this.activityIds = ids;
    }

    public List<String> getActivities() {
        return this.activityIds;
    }

    public boolean hasActivities() {
        if (activityIds == null) {
            return false;
        }
        return !activityIds.isEmpty();
    }

    public String joinActivities() {
        return Joiner.on(",").join(this.getActivities());
    }

    /**
     * Provide an easy way to build an immutable list of activity ids.
     */
    public static class Builder {
        private ImmutableList.Builder<String> markedActivity = new ImmutableList.Builder<>();

        public Builder withActivityId(final String id) {
            this.markedActivity.add(id);
            return this;
        }

        public Builder withActivityIds(final List<String> ids) {
            this.markedActivity.addAll(ids);
            return this;
        }

        /**
         * Build an immutable list of marked activities.
         *
         * @return
         */
        public MarkedActivity build() {
            return new MarkedActivity(markedActivity.build());
        }
    }
}
