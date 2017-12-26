package com.wuxp.study.custom.codec;

import org.jboss.marshalling.*;

/**
 * Marshalling 工厂
 */
public final class MarshallingCodeFactory {


    /**
     * @return
     * @throws Exception
     */
    public static Marshaller buildMarshalling() throws Exception {
        //通过MarshallerFactory工具获取marshalling实例对象，参数serial标识创建的是java的序列化工厂对象
        final MarshallerFactory marshallerFactory = Marshalling.getProvidedMarshallerFactory("serial");

        //创建 MarshallingConfiguration对象配置了版本号为5
        final MarshallingConfiguration marshallingConfiguration = new MarshallingConfiguration();
        marshallingConfiguration.setVersion(5);

        Marshaller marshaller = marshallerFactory.createMarshaller(marshallingConfiguration);

        return marshaller;
    }


    /**
     * @return
     * @throws Exception
     */
    public static Unmarshaller buildUnMarshalling() throws Exception {
        final MarshallerFactory marshallerFactory = Marshalling.getProvidedMarshallerFactory("serial");

        final MarshallingConfiguration marshallingConfiguration = new MarshallingConfiguration();
        marshallingConfiguration.setVersion(5);
        Unmarshaller unmarshaller = marshallerFactory.createUnmarshaller(marshallingConfiguration);
        return unmarshaller;
    }
}
