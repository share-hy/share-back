package com.share;

import static org.junit.Assert.assertTrue;

import com.share.hy.utils.SnowflakeIdWorker;
import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        long id = SnowflakeIdWorker.nextId();
        System.out.println(id);
    }
}
