package com.github.supermarket;

import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
//@ExtendWith({SpringExtension.class})
@ContextConfiguration(classes = {ApplicationConfig.class})
//@DataJpaTest
public class BaseTest {

}
