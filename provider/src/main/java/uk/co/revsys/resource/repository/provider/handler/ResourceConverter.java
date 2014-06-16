package uk.co.revsys.resource.repository.provider.handler;

public interface ResourceConverter<R extends Object> extends ResourceHandler{

    public R getResult();
    
}
