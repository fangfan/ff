package org.wit.ff.jdbc.id;

import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.wrapper.ObjectWrapper;
import org.apache.ibatis.reflection.wrapper.ObjectWrapperFactory;

/**
 * Created by F.Fang on 2015/11/17.
 * Version :2015/11/17
 */
public class IdGeneratorObjectWrapperFactory implements ObjectWrapperFactory {
    @Override
    public boolean hasWrapperFor(Object object) {
        return null != object && IdGenerator.class.isAssignableFrom(object.getClass());
    }

    @Override
    public ObjectWrapper getWrapperFor(MetaObject metaObject, Object object) {
        return new IdGeneratorObjectWrapper(metaObject, (IdGenerator)object);
    }
}
