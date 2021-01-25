package com.kutayyaman.unittest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals; //static import bu dikkat

public class HelloWorldTest {

    @Test
    void sayHelloWorld(){
        HelloWorld helloWorld = new HelloWorld();
        assertEquals("Hello World!", helloWorld.sayHello(), "If it will fail you can show this message"); //ilk parametresi benim bu test sonucunda bekledigim deger ikinci parametre gercekte olan ney ucuncu parametre ise hata olursa mesaj olarak ne gosterilsin.
    }
}
