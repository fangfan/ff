package org.wit.ff.business;

import org.springframework.stereotype.Service;
import org.wit.ff.cache.Cache;
import org.wit.ff.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by F.Fang on 2015/10/22.
 * Version :2015/10/22
 */
@Service
public class UserBusiness {

    @Cache
    public List<User> getUser(int appId, List<Integer> userIds){
        System.out.println("do business, getUser, appId="+appId);
        User user1 = new User(1, "f.fang@adchina.com", "fangfan");
        User user2 = new User(2,"mm@adchina.com","mm");
        List<User> list = new ArrayList<>();
        list.add(user1);
        list.add(user2);
        return list;
    }

    @Cache
    public User findUnique(int appId, int id){
        System.out.println("do business, findUnique, appId="+appId);
        User user = new User(100, "am@gmail.com", "am");
        return user;
    }

    @Cache
    public void saveUser(int appId, User user){
        System.out.println("do business, saveUser");
    }

}
