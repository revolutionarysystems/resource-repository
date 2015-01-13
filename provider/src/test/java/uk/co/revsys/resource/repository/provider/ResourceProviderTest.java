
package uk.co.revsys.resource.repository.provider;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import uk.co.revsys.resource.repository.LocalDiskResourceRepository;
import uk.co.revsys.resource.repository.ResourceRepository;
import uk.co.revsys.resource.repository.model.Resource;
import uk.co.revsys.resource.repository.provider.filter.AcceptAllResourceFilter;
import uk.co.revsys.resource.repository.provider.handler.ResourceHandler;
import uk.co.revsys.resource.repository.provider.handler.UnzippingResourceHandler;

public class ResourceProviderTest {

    public ResourceProviderTest() {
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
    public void testRefreshDirectory() throws Exception {
        ResourceRepository resourceRepository = new LocalDiskResourceRepository(new File("src/test/resources"));
        ResourceHandler resourceHandler = new ResourceHandler() {

            @Override
            public void handle(String path, Resource resource, InputStream contents) throws IOException {
                assertEquals("test1", path);
                assertEquals("test1", resource.getPath());
                assertEquals("test1.txt", resource.getName());
                assertEquals("This is a test", IOUtils.toString(contents));
            }
        };
        ResourceProvider resourceProvider = new ResourceProvider(resourceRepository, "test1", new AcceptAllResourceFilter(), resourceHandler);
        resourceProvider.refresh();
    }
    
    @Test
    public void testRefreshFile() throws Exception {
        ResourceRepository resourceRepository = new LocalDiskResourceRepository(new File("src/test/resources"));
        ResourceHandler resourceHandler = new ResourceHandler() {

            @Override
            public void handle(String path, Resource resource, InputStream contents) throws IOException {
                assertEquals("test1/test1.txt", path);
                assertEquals("test1", resource.getPath());
                assertEquals("test1.txt", resource.getName());
                assertEquals("This is a test", IOUtils.toString(contents));
            }
        };
        ResourceProvider resourceProvider = new ResourceProvider(resourceRepository, "test1/test1.txt", new AcceptAllResourceFilter(), resourceHandler);
        resourceProvider.refresh();
    }
    
    @Test
    public void testRefreshZipFile() throws Exception {
        ResourceRepository resourceRepository = new LocalDiskResourceRepository(new File("src/test/resources"));
        ResourceHandler resourceHandler = new ResourceHandler() {

            @Override
            public void handle(String path, Resource resource, InputStream contents) throws IOException {
                assertEquals("", path);
                assertEquals("test1", resource.getPath());
                assertEquals("test1.txt", resource.getName());
                assertEquals("This is a test", IOUtils.toString(contents));
            }
        };
        ResourceHandler unzippingResourceHandler = new UnzippingResourceHandler(resourceHandler);
        ResourceProvider resourceProvider = new ResourceProvider(resourceRepository, "test1.zip", new AcceptAllResourceFilter(), unzippingResourceHandler);
        resourceProvider.refresh();
    }

}