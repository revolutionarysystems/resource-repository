package uk.co.revsys.resource.repository.provider.handler;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import uk.co.revsys.resource.repository.model.Resource;

public class TarGZResourceHandler implements ResourceHandler {

    private final ResourceHandler resourceHandler;

    public TarGZResourceHandler(ResourceHandler resourceHandler) {
        this.resourceHandler = resourceHandler;
    }

    @Override
    public void handle(String path, Resource resource, InputStream contents) throws IOException {
        System.out.println("Unzipping " + resource.getFullPath());
        GzipCompressorInputStream gzIn = new GzipCompressorInputStream(contents);
        TarArchiveInputStream tarIn = new TarArchiveInputStream(gzIn);
        TarArchiveEntry entry = null;
        while ((entry = (TarArchiveEntry) tarIn.getNextEntry()) != null) {
            System.out.println("Extracting: " + entry.getName());
            if (!entry.isDirectory()) {
                String zipPath = entry.getName();
                String zipName;
                if (zipPath.indexOf("/") > -1) {
                    zipName = zipPath.substring(zipPath.indexOf("/") + 1);
                    zipPath = zipPath.substring(0, zipPath.indexOf("/"));
                } else {
                    zipName = zipPath;
                    zipPath = ".";
                }
                Resource zipResource = new Resource(zipPath, zipName);
                System.out.println("Zipped file: " + zipResource.getFullPath());
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                byte[] bytesIn = new byte[4096];
                int read = 0;
                while ((read = tarIn.read(bytesIn)) != -1) {
                    bos.write(bytesIn, 0, read);
                }
                resourceHandler.handle("", zipResource, new ByteArrayInputStream(bos.toByteArray()));
                bos.close();
            }
        }
        tarIn.close();
    }

}
