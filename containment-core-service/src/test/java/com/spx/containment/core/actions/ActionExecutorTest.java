package com.spx.containment.core.actions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
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
    public void setupMocks() {
        callable = mock(Callable.class);
    }


    @Test
    public void basic() throws Exception {
        when(callable.call()).thenReturn(null);
        Object response = subject.call(callable);
        assertNull(response);
        verify(callable).call();
    }

    @Test
    public void basicReturnValue() throws Exception {
        String returnValue = "return value";
        when(callable.call()).thenReturn(returnValue);
        Object response = subject.call(callable);
        assertEquals(response, returnValue);
        verify(callable).call();
    }

    @Test
    public void exceptionInCall() {

        try {
            when(callable.call()).thenThrow(new Exception("wrong"));
            subject.call(callable);
            fail();
        } catch (Exception e) {
            assertTrue(e instanceof RuntimeException);
            assertEquals("wrong", e.getCause()
                .getMessage());
        }

    }

    @Test
    public void runtimeExceptionInCall() {

        try {
            when(callable.call()).thenThrow(new RuntimeException("wrong"));
            subject.call(callable);
            fail();
        } catch (Exception e) {
            assertTrue(e instanceof RuntimeException);
            assertEquals("wrong", e.getMessage());
        }

    }
}
