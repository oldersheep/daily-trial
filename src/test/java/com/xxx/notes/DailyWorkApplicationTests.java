package com.xxx.notes;

import com.xxx.notes.entity.Source;
import com.xxx.notes.vo.Dest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
public class DailyWorkApplicationTests {

    @Test
    public void contextLoads() {
        System.out.println("aaa");
    }


    public static void main(String[] args) {
        Source source = new Source();
        source.setId("111");
        source.setName("2222");
        source.setPath("33333");
        source.setXxx("xxxxxx");
        Dest dest = new Dest();

        BeanUtils.copyProperties(source, dest);

        System.out.println(dest);
    }
}
