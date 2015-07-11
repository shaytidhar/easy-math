package easy.math

import easy.math.author.CreateAuthorCommand
import easy.math.author.UpdateAuthorCommand
import easy.math.models.Author
import org.apache.http.HttpStatus

class AuthorsController extends BaseController {

    def modelService

    def beforeInterceptor = [action: super.&auth]

    def index() {

        renderNotImplemented()
    }

    def list(){

        List<Author> listOfAuthors =
                Author.findAll()

        // render
        renderJson(modelService.getObjectMapper().writeValueAsString(listOfAuthors.collect { it.properties }))
    }

    def show(){

        // Index
        if (params.authorId == null) {

            forward(action: "list", params: params)
        }
        // Show
        else {

            Author author = Author.findById(params.authorId)

            // Author not exists
            if (author == null) {

                renderNotFound("Author with Id [${params.authorId}] does not exist")
            }
            else {

                // render
                renderJson(modelService.getObjectMapper().writeValueAsString(author.properties))
            }
        }
    }

    def update(UpdateAuthorCommand updateAuthorCommand){

        updateAuthorCommand =
                new UpdateAuthorCommand(modelService.getObjectMapper().readValue(params.theBody, Map.class))

        // Author was given
        withAuthor { Author author ->

            // Not valid
            if (!updateAuthorCommand.validate()) {

                response.setStatus(HttpStatus.SC_BAD_REQUEST)
                renderJson(GeneralUtils.errorJson(updateAuthorCommand.errors.allErrors.collect {
                    it.toString()
                }.join(" , ")))
            }
            else {

                // Update fields
                author.setModified(new Date())
                author.setFirstName(updateAuthorCommand.getFirstName())
                author.setLastName(updateAuthorCommand.getLastName())

                // Save
                author.save()

                // Render
                renderJson(modelService.getObjectMapper().writeValueAsString(author.properties))
            }
        }
    }

    def delete(){

        // Author was given
        withAuthor { Author author ->

            // Delete
            author.delete()

            // Render
            renderJson(modelService.getObjectMapper().writeValueAsString(author.properties))
        }
    }

    def create(CreateAuthorCommand createAuthorCommand){

        // Not valid
        if (!createAuthorCommand.validate()) {

            response.setStatus(HttpStatus.SC_BAD_REQUEST)
            renderJson(GeneralUtils.errorJson(createAuthorCommand.errors.allErrors.collect {
                it.toString()
            }.join(" , ")))
        } else {

            Author author = new Author()

            // Set fields
            author.setCreated(new Date())
            author.setModified(author.getCreated())
            author.setFirstName(createAuthorCommand.getFirstName())
            author.setLastName(createAuthorCommand.getLastName())

            // Save
            author.save()

            // render
            response.setStatus(HttpStatus.SC_CREATED)
            renderJson(modelService.getObjectMapper().writeValueAsString(author.properties))
        }
    }

    private def withAuthor(Closure c) {

        // No author was given
        if (params.authorId == null) {

            response.setStatus(HttpStatus.SC_BAD_REQUEST)
            renderJson(GeneralUtils.errorJson("Author Id is missing"))
        }
        else {

            Author author = Author.findById(params.authorId)

            // Author not exists
            if (author == null) {

                renderNotFound("Author with Id [${params.authorId}] does not exist")
            }
            else {

                // Call closure
                c.call author
            }
        }
    }
}
