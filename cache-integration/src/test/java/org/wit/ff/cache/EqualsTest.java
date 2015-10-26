package org.wit.ff.cache;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by F.Fang on 2015/10/22.
 * Version :2015/10/22
 */
public class EqualsTest {

    public static void main(String[] args) {
        List<String> list1 = new ArrayList<String>();
        list1.add("a");
        list1.add("b");

        List<String> list2 = new ArrayList<String>();
        list2.add("a");
        list2.add("b");

        System.out.println(list1.equals(list2));

        Object[] arr1 = new Object[]{1,2,3};
        Object[] arr2 = new Object[]{1,2,3};
        System.out.println(arr1.equals(arr2));
    }
}
