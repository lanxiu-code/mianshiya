package com.lanxiu.mianshiya;


import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;



/**
 * 主类测试
 *
 * 蓝朽
 * 
 */
@SpringBootTest
class MainApplicationTests {

    @Test
    void contextLoads() {
        int i = Math.floorMod(-3, 2);
        System.out.println(-3%2);
        System.out.println(i);
    }

}
