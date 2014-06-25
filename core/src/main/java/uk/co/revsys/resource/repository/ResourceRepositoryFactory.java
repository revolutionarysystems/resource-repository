package uk.co.revsys.resource.repository;

import java.util.Map;

public class ResourceRepositoryFactory {

    private final Map<String, ResourceRepositoryBuilder> builders;

    public ResourceRepositoryFactory(Map<String, ResourceRepositoryBuilder> builders) {
        this.builders = builders;
    }
    
    public ResourceRepository build(String type){
        return builders.get(type).build();
    }
    
}
