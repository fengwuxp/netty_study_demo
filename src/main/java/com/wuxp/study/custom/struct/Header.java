package com.wuxp.study.custom.struct;

import java.util.Objects;

public final class Header {

    /**
     * 唯一的通信标志
     */
    private int crcCode = 0xad82391;

    /**
     * 总消息的长度
     */
    private int length;

    /**
     * session Id
     */
    private long sessionId;

    /**
     * dto对象的类型
     */
    private byte type;

    /**
     * 优先级
     */
    private byte priority;

    public int getCrcCode() {
        return crcCode;
    }

    public void setCrcCode(int crcCode) {
        this.crcCode = crcCode;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public long getSessionId() {
        return sessionId;
    }

    public void setSessionId(long sessionId) {
        this.sessionId = sessionId;
    }

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public byte getPriority() {
        return priority;
    }

    public void setPriority(byte priority) {
        this.priority = priority;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Header header = (Header) o;
        return length == header.length &&
                sessionId == header.sessionId &&
                type == header.type &&
                priority == header.priority;
    }

    @Override
    public int hashCode() {

        return Objects.hash(length, sessionId, type, priority);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Header{");
        sb.append("crcCode=").append(crcCode);
        sb.append(", length=").append(length);
        sb.append(", sessionId=").append(sessionId);
        sb.append(", type=").append(type);
        sb.append(", priority=").append(priority);
        sb.append('}');
        return sb.toString();
    }
}
