package org.wit.ff.ps;

import org.wit.ff.ps.serialize.ProtoStuffSerializer;

import java.util.Arrays;


public class SerializerTest {
    
    public static void main(String[] args) {
        ProtoStuffSerializer<String> ser = new ProtoStuffSerializer<String>();
        byte[] data = ser.serialize("我爱我家");
        System.out.println(Arrays.toString(data));
        System.out.println(ser.deserialize(data, String.class));
    }

}
