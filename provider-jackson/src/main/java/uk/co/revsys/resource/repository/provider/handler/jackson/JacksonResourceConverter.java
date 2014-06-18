package uk.co.revsys.resource.repository.provider.handler.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import uk.co.revsys.resource.repository.provider.handler.AbstractResourceConverter;

public class JacksonResourceConverter<I extends Object> extends AbstractResourceConverter<I>{

    private final ObjectMapper objectMapper;
    private final Class<? extends I> type;

    public JacksonResourceConverter(ObjectMapper objectMapper, Class<? extends I> type) {
        this.objectMapper = objectMapper;
        this.type = type;
    }
    
    @Override
    public I convert(InputStream contents) throws IOException {
        I result = objectMapper.readValue(contents, type);
        return result;
    }

}
