package easy.math.exercise

import easy.math.models.Exercise
import grails.validation.Validateable

@Validateable
class CreateExerciseCommand {

    static constraints = {
        importFrom Exercise, exclude: [ "created", "modified" ]
    }

    String  exercise
    String  solution
}
