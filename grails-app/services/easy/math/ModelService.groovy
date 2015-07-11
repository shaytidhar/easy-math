package easy.math

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.PropertyAccessor
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature

import javax.annotation.PostConstruct

class ModelService {

    private ObjectMapper objectMapper

    @PostConstruct
    def init() {

        objectMapper = new ObjectMapper()
        objectMapper.setVisibility(PropertyAccessor.GETTER, JsonAutoDetect.Visibility.PROTECTED_AND_PUBLIC)
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT)
    }

    ObjectMapper getObjectMapper() {
        return objectMapper
    }
}
