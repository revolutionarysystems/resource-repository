package uk.co.revsys.resource.repository.provider.handler;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import uk.co.revsys.resource.repository.model.Resource;

public class UnzippingResourceHandler implements ResourceHandler{

    private final ResourceHandler resourceHandler;

    public UnzippingResourceHandler(ResourceHandler resourceHandler) {
        this.resourceHandler = resourceHandler;
    }

    @Override
    public void handle(String path, Resource resource, InputStream contents) throws IOException {
        System.out.println("Unzipping " + resource.getFullPath());
        ZipInputStream zippedStream = new ZipInputStream(contents);
        System.out.println("zippedStream = " + zippedStream);
        ZipEntry zipEntry = zippedStream.getNextEntry();
        System.out.println("zipEntry = " + zipEntry);
        while (zipEntry != null) {
            System.out.println(zipEntry.getName());
            System.out.println(zipEntry.isDirectory());
            if (!zipEntry.isDirectory()) {
                String zipPath = zipEntry.getName();
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
                while ((read = zippedStream.read(bytesIn)) != -1) {
                    bos.write(bytesIn, 0, read);
                }
                resourceHandler.handle("", zipResource, new ByteArrayInputStream(bos.toByteArray()));
                bos.close();
            }
            zippedStream.closeEntry();
            zipEntry = zippedStream.getNextEntry();
            System.out.println("zipEntry = " + zipEntry);
        }
        zippedStream.close();

    }

}
