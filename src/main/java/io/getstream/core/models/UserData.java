package io.getstream.core.models;

import com.google.common.base.MoreObjects;

import java.util.Objects;

public final class UserData {
    private final String id;
    private final String alias;

    public UserData(String id, String alias) {
        this.id = id;
        this.alias = alias;
    }

    public String getID() {
        return id;
    }

    public String getAlias() {
        return alias;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserData userData = (UserData) o;
        return Objects.equals(id, userData.id) &&
                Objects.equals(alias, userData.alias);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, alias);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", this.id)
                .add("alias", this.alias)
                .toString();
    }
}
