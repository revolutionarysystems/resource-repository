package uk.co.revsys.resource.repository.provider.filter;

import uk.co.revsys.resource.repository.model.Resource;

public class ExtensionFilter implements ResourceFilter{

    private final String extension;  

    public ExtensionFilter(String extension) {
        this.extension = extension;
    }
    
    @Override
    public boolean accept(Resource resource) {
        return resource.getName().endsWith(extension);
    }

}
