package easy.math

import easy.math.solution.CreateSolutionCommand
import easy.math.solution.UpdateSolutionCommand
import easy.math.models.Solution
import org.apache.http.HttpStatus

class SolutionsController extends BaseController {

    def modelService

    def beforeInterceptor = [action: super.&auth]

    def index() {

        renderNotImplemented()
    }

    def list(){
        List<Solution> listOfSolutions =
                modelService.getEntitiesService().listEntities(
                       Solution.class, params.filter, "100", "0", "")

        // render
        renderJson(modelService.getObjectMapper().writeValueAsString(listOfSolutions.collect { it.properties }))
    }

    def show(){

        // Index
        if (params.solutionId == null) {

            forward(action: "list", params: params)
        }
        // Show
        else {

            Solution Solution = Solution.findById(params.SolutionId)

            // Solution not exists
            if (Solution == null) {

                renderNotFound("Solution with Id [${params.SolutionId}] does not exist")
            }
            else {

                // render
                renderJson(modelService.getObjectMapper().writeValueAsString(Solution.properties))
            }
        }
    }

    def update(UpdateSolutionCommand updateSolutionCommand){

        updateSolutionCommand =
                new UpdateSolutionCommand(modelService.getObjectMapper().readValue(params.theBody, Map.class))

        // Solution was given
        withSolution { Solution solution ->

            // Not valid
            if (!updateSolutionCommand.validate()) {

                response.setStatus(HttpStatus.SC_BAD_REQUEST)
                renderJson(GeneralUtils.errorJson(updateSolutionCommand.errors.allErrors.collect {
                    it.toString()
                }.join(" , ")))
            }
            else {

                // Update fields
                solution.setModified(new Date())
                solution.setSolution(updateSolutionCommand.getSolution())
                solution.setAuthorName(updateSolutionCommand.getAuthorName())
                solution.setBookTitle(updateSolutionCommand.getBookTitle())
                solution.setPageNumber(updateSolutionCommand.getPageNumber())
                solution.setExerciseNumber(updateSolutionCommand.getExerciseNumber())

                // Save
                solution.save()

                // Render
                renderJson(modelService.getObjectMapper().writeValueAsString(solution.properties))
            }
        }
    }

    def delete(){

        // Solution was given
        withSolution { Solution solution ->

            // Delete
            solution.delete()

            // Render
            renderJson(modelService.getObjectMapper().writeValueAsString(solution.properties))
        }
    }

    def create(CreateSolutionCommand createSolutionCommand){

        // Not valid
        if (!createSolutionCommand.validate()) {

            response.setStatus(HttpStatus.SC_BAD_REQUEST)
            renderJson(GeneralUtils.errorJson(createSolutionCommand.errors.allErrors.collect {
                it.toString()
            }.join(" , ")))
        } else {

            Solution solution = new Solution()

            // Set fields
            solution.setCreated(new Date())
            solution.setModified(new Date())
            solution.setSolution(createSolutionCommand.getSolution())
            solution.setAuthorName(createSolutionCommand.getAuthorName())
            solution.setBookTitle(createSolutionCommand.getBookTitle())
            solution.setPageNumber(createSolutionCommand.getPageNumber())
            solution.setExerciseNumber(createSolutionCommand.getExerciseNumber())

            // Save
            solution.save()

            // render
            response.setStatus(HttpStatus.SC_CREATED)
            renderJson(modelService.getObjectMapper().writeValueAsString(solution.properties))
        }
    }

    private def withSolution(Closure c) {

        // No Solution was given
        if (params.solutionId == null) {

            response.setStatus(HttpStatus.SC_BAD_REQUEST)
            renderJson(GeneralUtils.errorJson("Solution Id is missing"))
        }
        else {

            Solution solution = Solution.findById(params.solutionId)

            // Solution not exists
            if (solution == null) {

                renderNotFound("Solution with Id [${params.solutionId}] does not exist")
            }
            else {

                // Call closure
                c.call solution
            }
        }
    }
}
