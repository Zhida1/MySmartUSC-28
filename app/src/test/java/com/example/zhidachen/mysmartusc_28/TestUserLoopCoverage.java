package com.example.zhidachen.mysmartusc_28;



import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;


public class TestUserLoopCoverage {
    User usr;

    @Before
    public void setup() {
        usr = new User("New User");
        for(int i = 0; i < 20; i++) {
            usr.addKeyword(Integer.toString(i+1), "test");
        }
    }

    @Test
    public void testAddKeyword1() {
        boolean flag = false;
        usr.addKeyword("", "test");
        flag = usr.checkKeyword("", "test");
        assertEquals(false, flag);
    }
    @Test
    public void testAddKeyword2() {
        boolean flag = false;
        String temp = "21";
        usr.addKeyword(temp, "test");
        flag = usr.checkKeyword(temp, "test");
        assertEquals(true, flag);
    }
    @Test
    public void testAddKeyword3() {
        boolean flag = false;
        String temp = "21 22";
        usr.addKeyword(temp, "test");
        flag = usr.checkKeyword("22", "test");
        assertEquals(true, flag);
    }
    @Test
    public void testAddKeyword4() {
        boolean flag = false;
        String temp = "21 22 23 24 25 26 27 28 29 30";
        usr.addKeyword(temp, "test");
        flag = usr.checkKeyword("25", "test");
        assertEquals(true, flag);
    }
    @Test
    public void testAddKeyword5() {
        boolean flag = false;
        boolean flag1 = false;
        boolean flag2 = false;
        boolean flag3 = false;
        String temp = "21 22 23 24 25 26 27 28 29 30";
        usr.addKeyword(temp, "test");
        flag1 = usr.checkKeyword("30", "test");
        flag2 = usr.checkKeyword("29", "test");
        flag3 = usr.checkKeyword("31", "test");
        if(flag1 && flag2 && !flag3) {
            flag = true;
        }
        assertEquals(true, flag);
    }

    @Test
    public void testCheckKeyword1() {
        boolean flag = false;
        flag = usr.checkKeyword("25", "test");
        assertEquals(false, flag);
    }
    @Test
    public void testCheckKeyword2() {
        boolean flag = false;
        flag = usr.checkKeyword("1", "test");
        assertEquals(true, flag);
    }
    @Test
    public void testCheckKeyword3() {
        boolean flag = false;
        flag = usr.checkKeyword("2", "test");
        assertEquals(true, flag);
    }
    @Test
    public void testCheckKeyword4() {
        boolean flag = false;
        flag = usr.checkKeyword("10", "test");
        assertEquals(true, flag);
    }
    @Test
    public void testCheckKeyword5() {
        boolean flag = false;
        boolean flag1 = false;
        boolean flag2 = false;
        boolean flag3 = false;
        flag1 = usr.checkKeyword("20", "test");
        flag2 = usr.checkKeyword("19", "test");
        flag3 = usr.checkKeyword("21", "test");
        if(flag1 && flag2 && !flag3) {
            flag = true;
        }
        assertEquals(true, flag);
    }

    @Test
    public void testRemoveKeyword1() {
        usr.removeKeyword("25", "test");
        assertEquals(20, usr.getKeywords().size());
    }
    @Test
    public void testRemoveKeyword2() {
        boolean flag = true;
        usr.removeKeyword("1", "test");
        flag = usr.checkKeyword("1", "test");
        assertEquals(false, flag);
    }
    @Test
    public void testRemoveKeyword3() {
        boolean flag = true;
        usr.removeKeyword("2", "test");
        flag = usr.checkKeyword("2", "test");
        assertEquals(false, flag);
    }
    @Test
    public void testRemoveKeyword4() {
        boolean flag = true;
        usr.removeKeyword("15", "test");
        flag = usr.checkKeyword("15", "test");
        assertEquals(false, flag);
    }
    @Test
    public void testRemoveKeyword5() {
        boolean flag = true;
        boolean flag1 = true;
        boolean flag2 = true;
        boolean flag3 = true;
        usr.removeKeyword("20", "test");
        usr.removeKeyword("19", "test");
        usr.removeKeyword("21", "test");
        flag1 = usr.checkKeyword("20", "test");
        flag2 = usr.checkKeyword("19", "test");
        flag3 = usr.checkKeyword("21", "test");
        if(flag1 || flag2 || flag3) {
            flag = false;
        }
        assertEquals(true, flag);
    }
}
