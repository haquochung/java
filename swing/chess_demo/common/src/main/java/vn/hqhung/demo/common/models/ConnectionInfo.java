package vn.hqhung.demo.common.models;

/**
 * @System: demo
 * @Title: Connection Info
 * @Version: 1.0.0
 * @Author: Hung-HQ
 * @CreateOn: 2022/03/16
 */
public enum ConnectionInfo {
    TABLE_IS_OK(0), ERR_TABLE_ID(1), ERR_TABLE_FULL(2);

    private final int value;

    ConnectionInfo(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
