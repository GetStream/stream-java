package io.getstream.client.model.activities;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SimpleActivity extends BaseActivity {

	@JsonProperty("foo_id")
	private String fooId;

	public String getFooId() {
		return fooId;
	}

	public void setFooId(String fooId) {
		this.fooId = fooId;
	}
}
