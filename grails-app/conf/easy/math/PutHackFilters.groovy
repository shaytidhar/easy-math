package easy.math

import com.fasterxml.jackson.databind.ObjectMapper

class PutHackFilters {

    def modelService

    def ObjectMapper objectMapper = new ObjectMapper()

    def filters = {

        all(controller: '*', action: '*') {
            before = {

                if (actionName && !actionName.equalsIgnoreCase("updateConfig") && request.getMethod().equalsIgnoreCase("PUT")){
                    params.theBody = request.reader.text
                }
            }
            after = { Map model ->

            }
            afterView = { Exception e ->

            }
        }
    }
}
