package org.wit.ff.jdbc;

import org.junit.Test;

import java.util.Arrays;
import java.util.Random;

/**
 * Created by F.Fang on 2015/11/24.
 */
public class ReadBagTest {

    @Test
    public void demo(){
        int n = 11;
        int amount = 20;

        int[] result = allocate(n, amount);
        System.out.println(Arrays.toString(result));
    }

    private int[] allocate(int n, int amount){
        int[] arr = new int[11];
        int max = getMax(n, amount);
        int remind = amount;
        Random random = new Random();
        for(int i=0;i<n; ++i){
            // 如果最后一个还没有分完,无论多少，算他运气好.
            if(i==(n-1)){
                arr[i] = remind;
                // 后面的逻辑不再执行,用continue也行.
                break;
            }

            // 如果分完了.
            if(remind == 0){
                break;
            }

            // 取.
            int nowBag = random.nextInt(max);
            // 剩下的大于当前取值.
            if(remind>nowBag){
                arr[i] = nowBag;
                // 红包剩下的减少.
                remind = remind-nowBag;
            } else {
                // 小于或等于,表示余额不足,就赋值给当前的用户.
                arr[i] = remind;
                remind = 0;
            }

        }
        return arr;
    }

    private int getMax(int n, int amount){
        // 僧多粥少.
        if(n>=amount){
            // 设置最大值,粥少的时候不应该出现过大的数字.
            return n/amount+1;
        } else {
            // 粥多.
            // n<amount
            // 取得倍数.
            int b = amount/n;
            // 如果倍数大于人数,说明粥很多.
            if(b>n){
                return 2*b;
            } else {
                // 否则粥不算太多.
                return n;
            }
        }
    }

}
