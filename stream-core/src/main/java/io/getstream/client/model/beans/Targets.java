package io.getstream.client.model.beans;

import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.List;

/**
 * This class acts as container for a list of operations intended to alter the target(s) of a given activity.
 * A {@link Targets.Builder} is required in order to build a {@link Targets} object.
 *
 */
public class Targets {

    private ImmutableList<String> newTargets;
    private ImmutableList<String> addedTargets;
    private ImmutableList<String> removedTargets;

    public Targets(List<String> newTargets, List<String> addedTargets, List<String> removedTargets) {
        if (!newTargets.isEmpty()) {
            this.newTargets = ImmutableList.copyOf(newTargets);
        }

        if (!addedTargets.isEmpty()) {
            this.addedTargets = ImmutableList.copyOf(addedTargets);
        }

        if (!removedTargets.isEmpty()) {
            this.removedTargets = ImmutableList.copyOf(removedTargets);
        }
    }

    public List<String> getNewTargets() {
        return newTargets;
    }

    public List<String> getAddedTargets() {
        return addedTargets;
    }

    public List<String> getRemovedTargets() {
        return removedTargets;
    }

    /**
     * Builder class for the {@link Targets} object.
     */
    public static class Builder {

        private List<String> newTargets = new ArrayList<>();
        private List<String> addedTargets = new ArrayList<>();
        private List<String> removedTargets = new ArrayList<>();

        /**
         * Set a list of new target(s).
         * @param newTargets New target(s)
         * @return This builder
         */
        public Builder setNewTargets(List<String> newTargets) {
            this.newTargets = newTargets;
            return this;
        }

        /**
         * Set a list of target(s) to add.
         * @param addedTargets Target(s) to add.
         * @return This builder
         */
        public Builder setAddedTargets(List<String> addedTargets) {
            this.addedTargets = addedTargets;
            return this;
        }

        /**
         * Set a list of target(s) to be removed.
         * @param removedTargets Target(s) to be removed.
         * @return This builder
         */
        public Builder setRemovedTargets(List<String> removedTargets) {
            this.removedTargets = removedTargets;
            return this;
        }

        /**
         * Change the existing target by specifying a new one.
         * @param target New target.
         * @return This builder
         */
        public Builder addNewTarget(String target) {
            this.newTargets.add(target);
            return this;
        }

        /**
         * Add a new target to be added.
         * @param target Target to be added.
         * @return This builder
         */
        public Builder addTargetToAdd(String target) {
            this.addedTargets.add(target);
            return this;
        }

        /**
         * Add a target to be removed.
         * @param target Target to be removed.
         * @return This builder
         */
        public Builder addTargetToRemove(String target) {
            this.removedTargets.add(target);
            return this;
        }

        /**
         * Build a {@link Targets} object.
         * You can specify either new targets or added targets or removed targets or a combinations of added and removed targets.
         * @return A valid {@link Targets} object.
         */
        public Targets build() {
            if (!newTargets.isEmpty() && (!addedTargets.isEmpty() || !removedTargets.isEmpty())) {
                throw new IllegalArgumentException("You can specify either new targets or added targets or removed targets or a combinations of added and removed targets.");
            }
            return new Targets(newTargets, addedTargets, removedTargets);
        }
    }
}
