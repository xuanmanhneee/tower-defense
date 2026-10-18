package com.towerdefense.core;

/**
 * Cờ bật/tắt Object Pool lúc chạy, áp dụng cho mọi {@link ObjectPool}.
 * Tắt: xoá object đang rảnh, {@code obtain} luôn tạo object mới và object bị huỷ
 * không được giữ lại.
 * Mặc định bật.
 */
public final class PoolSettings {
    private static volatile boolean enabled = true;

    private PoolSettings() {
    }

    public static boolean isEnabled() {
        return enabled;
    }

    public static void setEnabled(boolean value) {
        if (enabled == value) {
            return;
        }
        enabled = value;
        if (!value) {
            ObjectPool.clearAll();
        }
    }
}
