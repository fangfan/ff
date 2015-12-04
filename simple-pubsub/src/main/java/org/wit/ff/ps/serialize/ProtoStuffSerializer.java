package org.wit.ff.ps.serialize;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.runtime.RuntimeSchema;

/**
 * 
 * <pre>
 * 序列化与反序列化的工具.
 * </pre>
 *
 * @author F.Fang
 * @version $Id: ProtoStuffSerializer.java
 */
public final class ProtoStuffSerializer<M> implements ISerializer<M> {

    @Override
    public byte[] serialize(M obj) {
        if (obj == null) {
            throw new RuntimeException("序列化对象(" + obj + ")!");
        }
        @SuppressWarnings("unchecked")
        Schema<M> schema = (Schema<M>) RuntimeSchema.getSchema(obj.getClass());
        LinkedBuffer buffer = LinkedBuffer.allocate(1024 * 1024);
        byte[] protostuff = null;
        try {
            protostuff = ProtostuffIOUtil.toByteArray(obj, schema, buffer);
        } catch (Exception e) {
            throw new RuntimeException("序列化(" + obj.getClass() + ")对象(" + obj + ")发生异常!",
                e.getCause());
        } finally {
            buffer.clear();
        }
        return protostuff;
    }

    @Override
    public M deserialize(byte[] msg, Class<M> targetClass) {
        if (msg == null || msg.length == 0) {
            throw new RuntimeException("反序列化对象发生异常,byte序列为空!");
        }
        M instance = null;
        try {
            instance = targetClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("反序列化过程中依据类型创建对象失败!", e.getCause());
        }
        Schema<M> schema = RuntimeSchema.getSchema(targetClass);
        ProtostuffIOUtil.mergeFrom(msg, instance, schema);
        return instance;
    }

}
