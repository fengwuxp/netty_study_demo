package com.wuxp.study.custom.struct;

import java.util.Objects;

public final class NettyMessage {

    private Header header;

    private Object body;

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NettyMessage that = (NettyMessage) o;
        return Objects.equals(header, that.header) &&
                Objects.equals(body, that.body);
    }

    @Override
    public int hashCode() {

        return Objects.hash(header, body);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("NettyMessage{");
        sb.append("header=").append(header);
        sb.append(", body=").append(body);
        sb.append('}');
        return sb.toString();
    }
}
