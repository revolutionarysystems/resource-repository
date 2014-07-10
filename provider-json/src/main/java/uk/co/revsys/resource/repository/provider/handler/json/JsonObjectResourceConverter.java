package uk.co.revsys.resource.repository.provider.handler.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import uk.co.revsys.resource.repository.provider.handler.AbstractResourcesConverter;

public class JsonObjectResourceConverter<I extends Object> extends AbstractResourcesConverter<I>{

    private final ObjectMapper objectMapper;
    private final Class<? extends I> type;

    public JsonObjectResourceConverter(ObjectMapper objectMapper, Class<? extends I> type) {
        this.objectMapper = objectMapper;
        this.type = type;
    }
    
    @Override
    public I convert(InputStream contents) throws IOException {
        I result = objectMapper.readValue(contents, type);
        return result;
    }

}
