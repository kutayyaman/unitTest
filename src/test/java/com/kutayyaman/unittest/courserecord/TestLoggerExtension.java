package com.kutayyaman.unittest.courserecord;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.util.logging.Logger;

public class TestLoggerExtension implements BeforeAllCallback, AfterAllCallback { // kullanim sekli olarak istedigimiz test sinifinin basina @ExtendWith(TestLoggerExtension.class) yazarak kullanabiliriz veya sinif icerisinde static TestLoggerExtension testLoggerExtension = new TestLoggerExtension(); yazip ustune @RegisterExtension anatasyonu eklersek yine olur

    private static Logger log = Logger.getLogger(TestLoggerExtension.class.getName());

    @Override
    public void beforeAll(ExtensionContext context) throws Exception { //context parametresi calisan test context'tin bilgilerini verir bize
        log.info(String.format("Test is started...%s", context.getDisplayName()));
    }

    @Override
    public void afterAll(ExtensionContext context) throws Exception { //context parametresi calisan test context'tin bilgilerini verir bize
        log.info(String.format("Test is ended...%s", context.getDisplayName()));
    }
}
