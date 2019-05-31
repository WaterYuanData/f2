package com.yuan.testmemory;

public class MyMemory {
    public void forMemory() {
        String string = "adc";
        for (int i = 0; i < 100; i++) {
            string = string + ("_" + i + " ");
        }
    }
}
