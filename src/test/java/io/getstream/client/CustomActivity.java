package io.getstream.client;

import io.getstream.client.model.activities.BaseActivity;

public class CustomActivity extends BaseActivity {

    private long shopId;

    public long getShopId() {
        return shopId;
    }

    public void setShopId(long shopId) {
        this.shopId = shopId;
    }

    @Override
    public String toString() {
        return new StringBuilder(super.toString()).append(" shopId: " + shopId).toString();
    }
}
