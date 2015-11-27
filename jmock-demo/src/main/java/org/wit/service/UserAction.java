package org.wit.service;

public class UserAction {
    
    private UserService userService;
    
    public void execute(String something){
        System.out.println(userService.sayHello(something));
    }
    
    public void executeForPublic(String something){
        userService.sayHi(something);
        System.out.println(userService.sayHello(something));
    }
    
    public void executeForReal(String something){
        userService.secreteSay(something);
    }
    
    public void executeForPrivate(String something){
        userService.secreteSay(something);
    }
    
    public void executeForPublicStatic1(String something){
        System.out.println(StaticUserService.sayHello(something));
    }
    
    public void executeForPublicStatic(String something){
        StaticUserService.sayHi(something);
        System.out.println(StaticUserService.sayHello(something));
    }
    
    public void executeForPrivateStatic(String something){
        StaticUserService.secreteSay(something);
    }
    
    public void executeForConstructor(String arg){
        System.out.println(new ConstructorService(arg).doNoting());
    }
    
    public void executeForPrivateConstrutor(String arg){
        System.out.println(PrivateConstructorService.createInstance().getDetail(arg));
    }
    
    public void executeForFinal(String arg){
        System.out.println(userService.sayFinal(arg));
    }
    
    public void executeForStaticFinal(String arg){
        System.out.println(StaticUserService.sayFinal(arg));
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

}
