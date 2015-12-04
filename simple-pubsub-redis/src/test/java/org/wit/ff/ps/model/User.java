package org.wit.ff.ps.model;


import org.wit.ff.ps.IMessage;

public class User implements IMessage<User> {
    
    private String name;
    
    private int age;
    
    public User(){
        
    }
    
    public User(String name, int age){
        this.name = name;
        this.age = age;
    }

    @Override
    public User getBody() {
        return this;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "User [name=" + name + ", age=" + age + "]";
    }
    
}
