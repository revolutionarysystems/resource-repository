
package uk.co.revsys.resource.repository.classpath;

import java.io.FileNotFoundException;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import uk.co.revsys.resource.repository.ResourceRepository;
import uk.co.revsys.resource.repository.model.Resource;

public class ClasspathResourceRepositoryTest {

    public ClasspathResourceRepositoryTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void test() throws Exception{
        ResourceRepository resourceRepository = new ClassPathXmlApplicationContext("applicationContext.xml").getBean(ClasspathResourceRepository.class);
        assertEquals("This is a test", IOUtils.toString(resourceRepository.read(new Resource("test.txt"))));
        try{
            resourceRepository.listResources("test.txt");
            fail("Expected FileNotFoundException to be thrown");
        }catch(FileNotFoundException ex){
            // pass
        }
    }

}