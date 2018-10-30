package io.getstream.client.model.beans;

import org.junit.Test;

import java.util.Collections;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.junit.Assert.assertThat;

public class TargetsTest {

    @Test
    public void shouldBuildTheTargetWithNewTargets() {
        Targets targets = new Targets.Builder()
                .setNewTargets(Collections.singletonList("user:newUser1"))
                .build();

        assertThat(targets.getNewTargets(), hasItem("user:newUser1"));
    }

    @Test
    public void shouldBuildTheTargetWithAddAndRemoveTargets() {
        Targets targets = new Targets.Builder()
                .setAddedTargets(Collections.singletonList("user:newUser1"))
                .setRemovedTargets(Collections.singletonList("user:newUser2"))
                .build();

        assertThat(targets.getAddedTargets(), hasItem("user:newUser1"));
        assertThat(targets.getRemovedTargets(), hasItem("user:newUser2"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailBuildingTargets() {
        Targets targets = new Targets.Builder()
                .setNewTargets(Collections.singletonList("user:newUser1"))
                .setRemovedTargets(Collections.singletonList("user:newUser2"))
                .build();
    }
}