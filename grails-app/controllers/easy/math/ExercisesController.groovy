package easy.math

import easy.math.exercise.CreateExerciseCommand
import easy.math.exercise.UpdateExerciseCommand
import easy.math.models.Book
import easy.math.models.Exercise
import org.apache.http.HttpStatus

class ExercisesController extends BaseController {

    def modelService

    def beforeInterceptor = [action: super.&auth]

    def index() {

        renderNotImplemented()
    }

    def list(){

        List<Exercise> listOfExercises =
                Exercise.findAll()

        // render
        renderJson(modelService.getObjectMapper().writeValueAsString(listOfExercises.collect { it.properties }))
    }

    def show(){

        // Book is given and exists
        withBook { Book book ->

            // Index
            if (params.exerciseId == null) {

                forward(action: "list", params: params)
            }
            // Show
            else {

                Exercise exercise = Exercise.findById(params.exerciseId)

                // Exercise not exists
                if (exercise == null) {

                    renderNotFound("Exercise with Id [${params.exerciseId}] does not exist")
                } else {

                    // render
                    renderJson(modelService.getObjectMapper().writeValueAsString(exercise.properties))
                }
            }
        }
    }

    def update(UpdateExerciseCommand updateExerciseCommand){

        updateExerciseCommand =
                new UpdateExerciseCommand(modelService.getObjectMapper().readValue(params.theBody, Map.class))

        // Book is given and exists
        withBook { Book book ->

            // Exercise was given
            withExercise { Exercise exercise ->

            // Not valid
            if (!updateExerciseCommand.validate()) {

                response.setStatus(HttpStatus.SC_BAD_REQUEST)
                renderJson(GeneralUtils.errorJson(updateExerciseCommand.errors.allErrors.collect {
                    it.toString()
                }.join(" , ")))
            }
            else {


                    // Update fields
                    exercise.setModified(new Date())
                    exercise.setBook(book)
                    exercise.setExercise(updateExerciseCommand.getExercise())
                    exercise.setSolution(updateExerciseCommand.getSolution())

                    // Save
                    exercise.save()

                    // Render
                    renderJson(modelService.getObjectMapper().writeValueAsString(exercise.properties))
                }
            }
        }
    }

    def delete(){

        // Book is given and exists
        withBook { Book book ->

            // Exercise was given
            withExercise { Exercise exercise ->

                // Delete
                exercise.delete()

                // Render
                renderJson(modelService.getObjectMapper().writeValueAsString(exercise.properties))
            }
        }
    }

    def create(CreateExerciseCommand createExerciseCommand){

        // Book is given and exists
        withBook { Book book ->

            // Not valid
            if (!createExerciseCommand.validate()) {

                response.setStatus(HttpStatus.SC_BAD_REQUEST)
                renderJson(GeneralUtils.errorJson(createExerciseCommand.errors.allErrors.collect {
                    it.toString()
                }.join(" , ")))
            }
            else {

                Exercise exercise = new Exercise()

                // Set fields
                exercise.setCreated(new Date())
                exercise.setModified(exercise.getCreated())
                exercise.setBook(book)
                exercise.setExercise(createExerciseCommand.getExercise())
                exercise.setSolution(createExerciseCommand.getSolution())

                // Save
                exercise.save()

                // render
                response.setStatus(HttpStatus.SC_CREATED)
                renderJson(modelService.getObjectMapper().writeValueAsString(exercise.properties))
            }
        }
    }

    private def withBook(Closure c) {

        // No book was given
        if (params.bookId == null) {

            response.setStatus(HttpStatus.SC_BAD_REQUEST)
            renderJson(GeneralUtils.errorJson("Book Id is missing"))
        }
        else {

            Book book = Book.findById(params.bookId)

            // Book not exists
            if (book == null) {

                renderNotFound("Book with Id [${params.bookId}] does not exist")
            }
            else {

                // Call closure
                c.call book
            }
        }
    }

    private def withExercise(Closure c) {

        // No exercise was given
        if (params.exerciseId == null) {

            response.setStatus(HttpStatus.SC_BAD_REQUEST)
            renderJson(GeneralUtils.errorJson("Exercise Id is missing"))
        }
        else {

            Exercise exercise = Exercise.findById(params.exerciseId)

            // Exercise not exists
            if (exercise == null) {

                renderNotFound("Exercise with Id [${params.exerciseId}] does not exist")
            }
            else {

                // Call closure
                c.call exercise
            }
        }
    }
}
