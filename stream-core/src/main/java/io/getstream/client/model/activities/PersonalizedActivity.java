package io.getstream.client.model.activities;

/**
 * Personalized activities are subset of {@see io.getstream.client.model.activities.BaseActivity}.
 * Any custom personalized activity must be a subclasses of PersonalizedActivity.
 */
public class PersonalizedActivity extends BaseActivity {

    protected String tags;
    protected String text;

    public PersonalizedActivity() {
        super();
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
