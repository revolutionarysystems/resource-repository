package uk.co.revsys.resource.repository.provider.handler.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import uk.co.revsys.resource.repository.model.Resource;
import uk.co.revsys.resource.repository.provider.handler.ResourceConverter;
import uk.co.revsys.resource.repository.provider.handler.StreamAwareResourceHandler;

public class JsonArrayResourceConverter<I extends Object> implements ResourceConverter<List<I>>, StreamAwareResourceHandler{

    private final ObjectMapper objectMapper;
    private final CollectionType type;
    private List<I> result = new LinkedList<I>();

    public JsonArrayResourceConverter(ObjectMapper objectMapper, Class<? extends I> type) {
        this.objectMapper = objectMapper;
        this.type = objectMapper.getTypeFactory().constructCollectionType(List.class, type);
    }

    @Override
    public void handle(String path, Resource resource, InputStream contents) throws IOException {
        List<I> items = objectMapper.readValue(contents, type);
        result.addAll(items);
    }

    @Override
    public void newStream() throws IOException {
        result.clear();
    }

    @Override
    public void endOfStream() throws IOException {
        
    }

    @Override
    public List<I> getResult() {
        return result;
    }

}
