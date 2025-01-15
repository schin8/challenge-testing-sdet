package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {

    @Test
    public void sanityCheck(){
        Main testTarget = new Main();
        assertEquals(1, testTarget.sanity_check());
    }

    @Test
    public void pullRequestTest(){
        Main testTarget = new Main();
        assertEquals(1, testTarget.sanity_check());
    }
}