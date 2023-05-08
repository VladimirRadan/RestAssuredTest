package tests;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class UserTests {

    @BeforeClass
    public void beforeClassSetup(){
        System.out.println("Ovo jer before class");
    }

    @BeforeMethod
    public void preSvakeMetode(){
        System.out.println("Ovo jer before method");
    }

    @Test
    public void firstTest(){
        System.out.println("First test");
    }

    @Test
    public void firstTest2(){
        System.out.println("First test");
    }

    @Test
    public void firstTest3(){
        System.out.println("First test");
    }

    @Test
    public void firstTest4(){
        System.out.println("First test");
    }

    @Test
    public void firstTest5(){
        System.out.println("First test");
    }




}
