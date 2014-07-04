package uk.co.revsys.resource.repository.cloud;

import org.jclouds.ContextBuilder;
import org.jclouds.blobstore.BlobStore;
import org.jclouds.blobstore.BlobStoreContext;
import uk.co.revsys.resource.repository.ResourceRepository;
import uk.co.revsys.resource.repository.ResourceRepositoryBuilder;

public class JCloudResourceRepositoryBuilder implements ResourceRepositoryBuilder{

    private final String type;
    private final String identity;
    private final String credential;
    private final String container;
    private final String baseDir;

    public JCloudResourceRepositoryBuilder(String type, String identity, String credential, String container) {
        this.type = type;
        this.identity = identity;
        this.credential = credential;
        this.container = container;
        this.baseDir = "";
    }

    public JCloudResourceRepositoryBuilder(String type, String identity, String credential, String container, String baseDir) {
        this.type = type;
        this.identity = identity;
        this.credential = credential;
        this.container = container;
        this.baseDir = baseDir;
    }
    
    @Override
    public ResourceRepository build() {
        BlobStore blobStore = ContextBuilder.newBuilder(type).credentials(identity, credential).buildView(BlobStoreContext.class).getBlobStore();
        return new JCloudResourceRepository(blobStore, container, baseDir);
    }

}
