package easy.math.book

import easy.math.models.Book
import grails.validation.Validateable

@Validateable
class CreateBookCommand {

    static constraints = {
        importFrom Book, exclude: [ "created", "modified" ]
    }

    String          title
    String          publishing
    String          subject
}
