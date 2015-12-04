package app;

import server.ManageServer;

import java.util.Scanner;

/**
 * Created by F.Fang on 2015/5/27.
 * Version :2015/5/27
 */
public class ManageServer1 {

    public static void main(String[] args) {
        ManageServer server = new ManageServer();
        Scanner scanner = new Scanner(System.in);
        while(true){
            String text = scanner.nextLine();
            server.execute(text);
            if("exit".equals(text.toLowerCase())){
                break;
            }
        }
    }

}
