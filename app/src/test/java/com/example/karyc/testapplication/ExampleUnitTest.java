package com.example.karyc.testapplication;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(JUnit4.class)
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void passwordValidationTest() {
        String[] correctPassword = {"hdyr653", "jskjasa1", "password123", "password1", "pass1234"};
        String[] incorrectPassword = {"lqwerty", "1234567", "123", "j12", "kkkkkkkkkkk", "", null};

        for (String currentPassword : correctPassword) {
            boolean result = Utils.isValidPassword(currentPassword);
            Assert.assertTrue(currentPassword, result);
        }
        for (String currentIncorrectPassword : incorrectPassword) {
            boolean resultIncorrect = Utils.isValidPassword(currentIncorrectPassword);
            Assert.assertFalse(currentIncorrectPassword, resultIncorrect);
        }
    }
}