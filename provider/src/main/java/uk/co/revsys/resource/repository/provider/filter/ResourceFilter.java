package uk.co.revsys.resource.repository.provider.filter;

import uk.co.revsys.resource.repository.model.Resource;

public interface ResourceFilter {

    public boolean accept(Resource resource);
    
}
