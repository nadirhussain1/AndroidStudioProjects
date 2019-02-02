package com.crossover.bicycleproject;

import com.crossover.bicycleproject.utils.CommonUtil;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by nadirhussain on 26/07/2016.
 */
public class CommonUtilTest {
    @Test
    public void validEmailTest(){
        assertTrue(CommonUtil.isValidEmail("nadirhussain1@gmail.com"));
    }
    @Test
    public void invalidEmailTest(){
        assertFalse(CommonUtil.isValidEmail("nad@email"));
    }
    @Test
    public void invalidEmptyEmailTest(){
        assertFalse(CommonUtil.isValidEmail(""));
    }
    @Test
    public void invalidDoubleDotEmailTest(){
        assertFalse(CommonUtil.isValidEmail("nadir@gmail..com"));
    }
    @Test
    public void invalidNoNameEmailTest(){
        assertFalse(CommonUtil.isValidEmail("@gmail.com"));
    }
    @Test
    public void invalidEmailNullTest(){
        assertFalse(CommonUtil.isValidEmail(null));
    }
}
