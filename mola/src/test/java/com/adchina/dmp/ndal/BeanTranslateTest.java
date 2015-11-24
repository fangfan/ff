package com.adchina.dmp.ndal;

import com.adchina.dmp.ndal.model.MutiAdUser;
import com.adchina.dmp.ndal.model.User;
import com.adchina.dmp.ndal.mongodb.Constants;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.junit.Test;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Created by F.Fang on 2015/2/28.
 * Version :2015/2/28
 */
public class BeanTranslateTest {

    @Test
    public void dbObjectToBean(){
        ObjectMapper mapper = new ObjectMapper();
        BasicDBList dbList = new BasicDBList();
        dbList.add(new BasicDBObject("country", "china").append("city", "shanghai"));
        dbList.add(new BasicDBObject("country", "china").append("city", "beijing"));

        BasicDBObject obj = new BasicDBObject("name", "himongo")
                .append("age", 18)
                .append("gender", 1)
                .append("address", dbList);
        System.out.println(mapper.convertValue(obj, MutiAdUser.class));
    }

    @Test
    public void dbObjectToMap(){
        BasicDBList dbList = new BasicDBList();
        dbList.add(new BasicDBObject("country", "china").append("city", "shanghai"));
        dbList.add(new BasicDBObject("country", "china").append("city", "beijing"));

        for(String key: dbList.keySet()){
            System.out.println(key+"|"+dbList.get(key));
        }

        BasicDBList dbList1 = new BasicDBList();
        dbList1.add(100);
        dbList1.add(200);
        for(String key: dbList1.keySet()){
            System.out.println(key);
        }
    }

    @Test
    public void demo() throws Exception {
        User user = new User();

        BasicDBObject obj = new BasicDBObject("name", "himongo")
                .append("age", 18)
                .append("gender", 1)
                .append("address", new BasicDBObject("country", "china").append("city", "shanghai"));

        Map<String, Method> writerMethods = new HashMap<String, Method>();

        BeanInfo info = Introspector.getBeanInfo(user.getClass());
//        for (PropertyDescriptor pd : info.getPropertyDescriptors()) {
//            Method reader = pd.getReadMethod();
//            if (reader != null)
//                objectAsMap.put(pd.getName(),reader.invoke(actionMethodNoteBook));
//        }
        for(PropertyDescriptor pd : info.getPropertyDescriptors()){

            Method writer = pd.getWriteMethod();

            writerMethods.put(pd.getName(), writer);
        }

        Object instance = User.class.newInstance();

        for (String key : obj.keySet()) {
            Object val = obj.get(key);
            Method writer = writerMethods.get(key);
            if(writer!=null && val!=null){
                if (val instanceof DBObject) {
                    // 说明此字段类型是具体的类型.
                    Class<?> subCls = writer.getParameterTypes()[0];
                    Object subInstance = subCls.newInstance();

                } else {
                    writer.invoke(instance, val);
                }
            }
        }

        System.out.println(writerMethods);

        System.out.println(instance);

    }

    @Test
    public void testTranslateGroupBy(){
        BasicDBObject obj = new BasicDBObject("_id", new BasicDBObject("name","ff").append("age",18))
                .append("gender", 1)
                .append("address","here");
        translateGroupFields(obj);
        System.out.println(obj);
    }

    @Test
    public void testCommon(){
        List<User> user = new ArrayList<User>();
        System.out.println(user.getClass().getGenericSuperclass().getClass().getTypeParameters());
    }

    private void translateGroupFields(DBObject obj){
        if(obj.containsField(Constants._ID)){
            Object id = obj.get(Constants._ID);
            if(id instanceof DBObject){
                DBObject idObj = (DBObject)id;
                Set<String> keys = ((DBObject)id).keySet();
                for(String key: keys){
                    obj.put(key,idObj.get(key));
                }
                // remove _id
                obj.removeField(Constants._ID);
            }
        }
    }

}
