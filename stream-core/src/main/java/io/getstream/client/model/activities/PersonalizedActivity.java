package io.getstream.client.model.activities;

import java.util.List;

/**
 * Personalized activities are subset of {@link io.getstream.client.model.activities.BaseActivity}.
 * Any custom personalized activity must be a subclasses of PersonalizedActivity.
 */
public class PersonalizedActivity extends BaseActivity {

    protected List<String> tags;
    protected String text;

    public PersonalizedActivity() {
        super();
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
