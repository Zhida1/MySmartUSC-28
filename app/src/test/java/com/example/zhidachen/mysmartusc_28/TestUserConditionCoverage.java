package com.example.zhidachen.mysmartusc_28;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class TestUserConditionCoverage {
    User usr;

    @Before
    public void setup() {
        usr = new User("New User");
        for(int i = 0; i < 20; i++) {
            usr.addKeyword(Integer.toString(i+1), "test");
        }
    }

    @Test
    public void testCheckKeyword1() {
        boolean flag = false;
        flag = usr.checkKeyword("1", "test");
        assertEquals(true, flag);
    }
    @Test
    public void testCheckKeyword2() {
        boolean flag = false;
        flag = usr.checkKeyword("1", "test1");
        assertEquals(false, flag);
    }
    @Test
    public void testCheckKeyword3() {
        boolean flag = false;
        flag = usr.checkKeyword("21", "test");
        assertEquals(false, flag);
    }
    @Test
    public void testCheckKeyword4() {
        boolean flag = false;
        flag = usr.checkKeyword("21", "test1");
        assertEquals(false, flag);
    }

    @Test
    public void testRemoveKeyword1() {
        usr.removeKeyword("1", "test");
        assertEquals(19, usr.getKeywords().size());
    }
    @Test
    public void testRemoveKeyword2() {
        usr.removeKeyword("1", "test1");
        assertEquals(20, usr.getKeywords().size());
    }
    @Test
    public void testRemoveKeyword3() {
        usr.removeKeyword("21", "test");
        assertEquals(20, usr.getKeywords().size());
    }
    @Test
    public void testRemoveKeyword4() {
        usr.removeKeyword("21", "test1");
        assertEquals(20, usr.getKeywords().size());
    }
}
