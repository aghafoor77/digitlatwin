package org.ri.se.platform;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({})
public class AllTests {
	@Test
    public void testcaseFirst()
    {
        System.out.println("First testcase executed");
    }
 
    @Test
    public void testcaseSecond()
    {
        System.out.println("Second testcase executed");
    }
 
    @Test
    public void testcaseThird()
    {
        System.out.println("Third testcase executed");
    }
 
    @Test
    public void otherTestcase()
    {
        System.out.println("Another testcase executed");
    }
}
