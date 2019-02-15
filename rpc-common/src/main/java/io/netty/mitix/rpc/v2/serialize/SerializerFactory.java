package io.netty.mitix.rpc.v2.serialize;


/**
 * @author oldflame-jm
 */
public class SerializerFactory {

    public static Serializer getSerializer() {
        return new ProtobufSerializer();
    }

}
