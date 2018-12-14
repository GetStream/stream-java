package io.getstream.core;

public enum KeepHistory {
    YES(true),
    NO(false);

    private final boolean flag;

    KeepHistory(boolean flag) {
        this.flag = flag;
    }

    public boolean getFlag() {
        return flag;
    }
}
