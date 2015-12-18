package org.wit.ff.serializer;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.runtime.RuntimeSchema;

/**
 * Created by F.Fang on 2015/10/23.
 * Version :2015/10/23
 */
public class ProtoStuffSerializer {

    public <T> byte[] serialize(T obj) {
        if (obj == null) {
            throw new RuntimeException("serialize (" + obj + ") can't be null!");
        }
        Schema schema = RuntimeSchema.getSchema(obj.getClass());
        LinkedBuffer buffer = LinkedBuffer.allocate(1024 * 1024);
        byte[] bytes;
        try {
            bytes = ProtostuffIOUtil.toByteArray(obj, schema, buffer);
        } catch (Exception e) {
            throw new RuntimeException("serialize(obj=" + obj + ",class=" + obj.getClass() + ")  catch exception!", e);
        } finally {
            buffer.clear();
        }
        return bytes;
    }

    public <T> T deserialize(byte[] bytes, Class<T> targetClass) {
        if (bytes == null || bytes.length == 0 || targetClass==null) {
            throw new RuntimeException("deserialize bytes is empty or targetClass is null!");
        }
        T instance = null;
        try {
            instance = targetClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("deserialize catch exception!", e);
        }
        Schema<T> schema = RuntimeSchema.getSchema(targetClass);
        ProtostuffIOUtil.mergeFrom(bytes, instance, schema);
        return instance;
    }
}
