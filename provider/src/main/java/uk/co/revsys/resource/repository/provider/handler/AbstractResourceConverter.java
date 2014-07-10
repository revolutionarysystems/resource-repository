package uk.co.revsys.resource.repository.provider.handler;

import java.io.IOException;
import java.io.InputStream;
import uk.co.revsys.resource.repository.model.Resource;

public abstract class AbstractResourceConverter<R extends Object> implements ResourceConverter<R>{

    private R result;

    @Override
    public void handle(String path, Resource resource, InputStream contents) throws IOException {
        result = convert(contents);
    }
    
    public abstract R convert(InputStream contents) throws IOException;
    
    @Override
    public R getResult() {
        return result;
    }

}
