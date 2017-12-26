package com.wuxp.study.serial;

import io.netty.handler.codec.marshalling.*;
import org.jboss.marshalling.MarshallerFactory;
import org.jboss.marshalling.Marshalling;
import org.jboss.marshalling.MarshallingConfiguration;

/**
 * Marshalling 工厂
 */
public final class MarshallingCodeFactory {


    /**
     * 创建 Jboss Marshalling 解码器 MyMarshallingDecoder
     *
     * @return MyMarshallingDecoder
     */
    public static MarshallingDecoder buildMarshallingDecoder() {
        //通过MarshallerFactory工具获取marshalling实例对象，参数serial标识创建的是java的序列化工厂对象
        final MarshallerFactory marshallerFactory = Marshalling.getProvidedMarshallerFactory("serial");

        //创建 MarshallingConfiguration对象配置了版本号为5
        final MarshallingConfiguration marshallingConfiguration = new MarshallingConfiguration();
        marshallingConfiguration.setVersion(5);

        //根据marshallerFactory和 configuration创建provider
        UnmarshallerProvider provider = new DefaultUnmarshallerProvider(marshallerFactory, marshallingConfiguration);

        //构建Netty的MarshallingDecoder 参数为 provider 和单个消息序列化后的最大长度
        MarshallingDecoder decoder = new MarshallingDecoder(provider, 1024 * 1024);

        return decoder;
    }


    /**
     * 创建 Jboss Marshalling 编码器 MarshallingEncoder
     *
     * @return MarshallingEncoder
     */
    public static MarshallingEncoder buildMarshallingEncoder() {
        final MarshallerFactory marshallerFactory = Marshalling.getProvidedMarshallerFactory("serial");

        final MarshallingConfiguration marshallingConfiguration = new MarshallingConfiguration();
        marshallingConfiguration.setVersion(5);
        MarshallerProvider provider = new DefaultMarshallerProvider(marshallerFactory, marshallingConfiguration);

        //构建MarshallingEncoder对象，MarshallingEncoder 用于实现序列化接口Pojo对象序列化为二进制数值
        MarshallingEncoder encoder = new MarshallingEncoder(provider);
        return encoder;
    }
}
