package com.spx.containment.core.actions;


import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.concurrent.Callable;
import org.junit.Before;
import org.junit.Test;


public class ActionExecutorTest {


    private final ActionExecutor subject = new ActionExecutor();
    private Callable<Object> callable;

    @Before
    public void setupMoocks() {
        callable = mock(Callable.class);
    }


    @Test
    public void basic() throws Exception {
        when(callable.call()).thenReturn(null);
        Object response = subject.call(callable);
        assertTrue(response == null);
        verify(callable).call();
    }

    @Test
    public void basicReturnValue() throws Exception {
        String returnValue = "return value";
        when(callable.call()).thenReturn(returnValue);
        Object response = subject.call(callable);
        assertTrue(response.equals(returnValue));
        verify(callable).call();
    }

    @Test
    public void exceptionInCall() throws Exception {

        try {
            when(callable.call()).thenThrow(new Exception("wrong"));
            subject.call(callable);
            fail();
        } catch (Exception e) {
            assertTrue(RuntimeException.class.isInstance(e));
            assertTrue(e.getCause()
                .getMessage()
                .equals("wrong"));
        }

    }

    @Test
    public void runtimeExceptionInCall() throws Exception {

        try {
            when(callable.call()).thenThrow(new RuntimeException("wrong"));
            subject.call(callable);
            fail();
        } catch (Exception e) {
            assertTrue(RuntimeException.class.isInstance(e));
            assertTrue(e.getMessage()
                .equals("wrong"));
        }

    }
}
