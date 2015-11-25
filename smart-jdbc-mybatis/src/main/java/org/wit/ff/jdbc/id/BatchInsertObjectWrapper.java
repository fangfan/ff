package org.wit.ff.jdbc.id;

import java.util.Iterator;
import java.util.List;

import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.reflection.property.PropertyTokenizer;
import org.apache.ibatis.reflection.wrapper.ObjectWrapper;

/**
 * Wrap the collection object for batch insert.
 * https://github.com/jactive/java
 */
public class BatchInsertObjectWrapper implements ObjectWrapper {

    private final BatchInsertEntities<IdGenerator> entity;

    public BatchInsertObjectWrapper(MetaObject metaObject, BatchInsertEntities<IdGenerator> object) {
        this.entity = object;
    }

    @Override
    public void set(PropertyTokenizer prop, Object value) {
        // check the primary key type existed or not when setting PK by reflection.
        BatchInsertEntityPrimaryKeys pks = (BatchInsertEntityPrimaryKeys) value;
        if (pks.getPrimaryKeys().size() == entity.getEntities().size()) {

            Iterator<String> iterPks = pks.getPrimaryKeys().iterator();
            Iterator<IdGenerator> iterEntities = entity.getEntities().iterator();

            while (iterPks.hasNext()) {
                String id = iterPks.next();
                IdGenerator entity = iterEntities.next();
                //System.out.println(id + "|" + entity);
                entity.parseGenKey(new Object[]{id});
            }
        }
    }

    @Override
    public Object get(PropertyTokenizer prop) {
        // Only the entities or parameters property of BatchInsertEntities
        // can be accessed by mapper.
        // 这一段是决定最终返回数据结果.
        if ("entities".equals(prop.getName()) ||
                "parameters".equals(prop.getName())) {
            return entity.getEntities();
        }

        return null;
    }

    @Override
    public String findProperty(String name, boolean useCamelCaseMapping) {
        return null;
    }

    @Override
    public String[] getGetterNames() {
        return null;
    }

    @Override
    public String[] getSetterNames() {
        return null;
    }


    /**
     * 此函数返回类型和BatchInsertEntitiesTypeHandler的泛型类型一致.
     * Jdbc3KeyGenerator.
     * Class<?> keyPropertyType = metaParam.getSetterType(keyProperties[i]);
     * TypeHandler<?> th = typeHandlerRegistry.getTypeHandler(keyPropertyType);
     *
     * @param name
     * @return
     * @see org.apache.ibatis.reflection.wrapper.ObjectWrapper#getSetterType(java.lang.String)
     */
    @Override
    public Class<?> getSetterType(String name) {
        // Return the primary key setter type.
        // Here, we return the BatchInsertEntityPrimaryKeys because
        // there are several primary keys  in the result set of
        // INSERT statement.
        return BatchInsertEntityPrimaryKeys.class;
    }

    @Override
    public Class<?> getGetterType(String name) {
        return null;
    }

    @Override
    public boolean hasSetter(String name) {
        // In BatchInsertObjectWrapper, name is the primary key property name.
        // Always return true here without checking if there is such property
        // in BatchInsertEntities#getEntities().get(0) . The verification be
        // postphone until setting the PK value at this.set method.
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