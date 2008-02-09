/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.matchers;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.CustomMatcher;
import org.mockito.Mockito;
import org.mockito.TestBase;
import org.mockitousage.IMethods;

public class CustomMatchersTest extends TestBase {
    
    private final class ContainsFoo extends CustomMatcher<String> {
        public boolean matches(Object arg) {
            return ((String) arg).contains("foo");
        }
    }

    private final class IsAnyBoolean extends CustomMatcher<Boolean> {
        public boolean matches(Object arg) {
            return true;
        }
    }
    
    private final class IsSorZ extends CustomMatcher<Character> {
        public boolean matches(Object arg) {
            Character character = (Character) arg;
            return character.equals('s') || character.equals('z');
        }
    }

    private final class IsZeroOrOne<T extends Number> extends CustomMatcher<T> {
        public boolean matches(Object arg) {
            Number number = (Number) arg;
            if (number.intValue() == 0 || number.intValue() == 1) {
                return true;
            }
            return false;
        }
    }

    private IMethods mock;

    @Before
    public void setUp() {
        mock = Mockito.mock(IMethods.class);
    }

    @Test
    public void shouldUseCustomBooleanMatcher() {
        stub(mock.oneArg(booleanThat(new IsAnyBoolean()))).toReturn("foo");
        
        assertEquals("foo", mock.oneArg(true));
        assertEquals("foo", mock.oneArg(false));
        
        assertEquals(null, mock.oneArg("x"));
    }
    
  @Test
  public void shouldUseCustomCharMatcher() {
      stub(mock.oneArg(charThat(new IsSorZ()))).toReturn("foo");
      
      assertEquals("foo", mock.oneArg('s'));
      assertEquals("foo", mock.oneArg('z'));
      assertEquals(null, mock.oneArg('x'));
  }
    
    @Test
    public void shouldUseCustomPrimitiveNumberMatchers() {
        stub(mock.oneArg(byteThat(new IsZeroOrOne<Byte>()))).toReturn("byte");
        stub(mock.oneArg(shortThat(new IsZeroOrOne<Short>()))).toReturn("short");
        stub(mock.oneArg(intThat(new IsZeroOrOne<Integer>()))).toReturn("int");
        stub(mock.oneArg(longThat(new IsZeroOrOne<Long>()))).toReturn("long");
        stub(mock.oneArg(floatThat(new IsZeroOrOne<Float>()))).toReturn("float");
        stub(mock.oneArg(doubleThat(new IsZeroOrOne<Double>()))).toReturn("double");
        
        assertEquals("byte", mock.oneArg((byte) 0));
        assertEquals("short", mock.oneArg((short) 1));
        assertEquals("int", mock.oneArg(0));
        assertEquals("long", mock.oneArg(1L));
        assertEquals("float", mock.oneArg(0F));
        assertEquals("double", mock.oneArg(1.0));
        
        assertEquals(null, mock.oneArg(2));
        assertEquals(null, mock.oneArg("foo"));
    }
         
    @Test
    public void shouldUseCustomObjectMatcher() {
        stub(mock.oneArg(argThat(new ContainsFoo()))).toReturn("foo");
        
        assertEquals("foo", mock.oneArg("foo"));
        assertEquals(null, mock.oneArg("bar"));
    }
}