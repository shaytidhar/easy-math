package easy.math.author

import easy.math.models.Author
import grails.validation.Validateable

@Validateable
class UpdateAuthorCommand {

    static constraints = {
        importFrom Author, exclude: [ "created", "modified" ]
    }

    String  firstName
    String  lastName
}
