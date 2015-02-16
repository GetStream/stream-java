package io.getstream.client.model.activities;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.MoreObjects;

import java.util.Date;
import java.util.List;

/**
 * Activity on GetStream.io has mandatory fields which are used to define its behaviour.
 * Any custom activity must be a subclasses of BaseActivity.
 */
public abstract class BaseActivity {

	protected String id;
    protected String actor;
    protected String verb;
    protected String object;
    protected String target;
    protected Date time;

	@JsonDeserialize(contentUsing = ActivitySignedRecipientDeserializer.class)
    protected List<String> to;

	protected String origin;
	protected String duration;

	@JsonProperty("foreign_id")
    protected String foreignId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public String getActor() {
        return actor;
    }

    public void setActor(String actor) {
        this.actor = actor;
    }

    public String getVerb() {
        return verb;
    }

    public void setVerb(String verb) {
        this.verb = verb;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public List<String> getTo() {
        return to;
    }

    public void setTo(List<String> to) {
        this.to = to;
    }

    public String getForeignId() {
        return foreignId;
    }

    public void setForeignId(String foreignId) {
        this.foreignId = foreignId;
    }

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
				.add("id", this.id)
				.add("actor", this.actor)
				.add("verb", this.verb)
				.add("object", this.object)
				.add("target", this.target)
				.add("time", this.time)
				.add("to", this.to.toString())
				.add("origin", this.origin)
				.add("duration", this.duration).toString();
	}
}