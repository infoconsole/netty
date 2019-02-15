package io.netty.mitix.rpc.v2.serialize;

public interface Serializer {

    <T> Object deserialize(byte[] bytes, Class<T> clazz);

    <T> byte[] serialize(T obj);
}
