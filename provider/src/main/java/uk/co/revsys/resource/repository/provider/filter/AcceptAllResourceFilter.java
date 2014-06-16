package uk.co.revsys.resource.repository.provider.filter;

import uk.co.revsys.resource.repository.model.Resource;

public class AcceptAllResourceFilter implements ResourceFilter{

    @Override
    public boolean accept(Resource resource) {
        return true;
    }

}
