package org.wit.ff.jdbc.id;

import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.reflection.property.PropertyTokenizer;
import org.apache.ibatis.reflection.wrapper.ObjectWrapper;

import java.util.List;

/**
 * Created by F.Fang on 2015/11/17.
 * Version :2015/11/17
 */
public class IdGeneratorObjectWrapper implements ObjectWrapper {

    private IdGenerator idGenerator;

    public IdGeneratorObjectWrapper(MetaObject metaObject, IdGenerator object) {
        this.idGenerator = object;
    }

    @Override
    public Object get(PropertyTokenizer prop) {
        System.out.println("get...");
        return null;
    }

    @Override
    public void set(PropertyTokenizer prop, Object value) {
        System.out.println(value);
        //
        idGenerator.parseGenKey(new Object[]{value});
    }

    @Override
    public String findProperty(String name, boolean useCamelCaseMapping) {
        return null;
    }

    @Override
    public String[] getGetterNames() {
        return new String[0];
    }

    @Override
    public String[] getSetterNames() {
        return new String[0];
    }

    @Override
    public Class<?> getSetterType(String name) {
        return String.class;
    }

    @Override
    public Class<?> getGetterType(String name) {
        return null;
    }

    @Override
    public boolean hasSetter(String name) {
        return true;
    }

    @Override
    public boolean hasGetter(String name) {
        return false;
    }

    @Override
    public MetaObject instantiatePropertyValue(String name, PropertyTokenizer prop, ObjectFactory objectFactory) {
        return null;
    }

    @Override
    public boolean isCollection() {
        return false;
    }

    @Override
    public void add(Object element) {

    }

    @Override
    public <E> void addAll(List<E> element) {

    }
}
