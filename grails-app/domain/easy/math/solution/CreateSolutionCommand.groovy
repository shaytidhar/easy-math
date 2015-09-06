package easy.math.solution

import easy.math.models.Solution
import grails.validation.Validateable

@Validateable
class CreateSolutionCommand {

    static constraints = {
        importFrom Solution, exclude: [ "created", "modified" ]
    }

    String          solution
    String          bookTitle
    String          authorName
    Integer         pageNumber
    Integer         exerciseNumber
}
