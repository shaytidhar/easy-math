package easy.math

import easy.math.book.CreateBookCommand
import easy.math.book.UpdateBookCommand
import easy.math.models.Book
import org.apache.http.HttpStatus

class BooksController extends BaseController {

    def modelService

    def beforeInterceptor = [action: super.&auth]

    def index() {

        renderNotImplemented()
    }

    def list(){

        List<Book> listOfBooks =
                Book.findAll()

        // render
        renderJson(modelService.getObjectMapper().writeValueAsString(listOfBooks.collect { it.properties }))
    }

    def show(){

        // Index
        if (params.bookId == null) {

            forward(action: "list", params: params)
        }
        // Show
        else {

            Book book = Book.findById(params.bookId)

            // Book not exists
            if (book == null) {

                renderNotFound("Book with Id [${params.bookId}] does not exist")
            }
            else {

                // render
                renderJson(modelService.getObjectMapper().writeValueAsString(book.properties))
            }
        }
    }

    def update(UpdateBookCommand updateBookCommand){

        updateBookCommand =
                new UpdateBookCommand(modelService.getObjectMapper().readValue(params.theBody, Map.class))

        // Book was given
        withBook { Book book ->

            // Not valid
            if (!updateBookCommand.validate()) {

                response.setStatus(HttpStatus.SC_BAD_REQUEST)
                renderJson(GeneralUtils.errorJson(updateBookCommand.errors.allErrors.collect {
                    it.toString()
                }.join(" , ")))
            }
            else {

                // Update fields
                book.setModified(new Date())
                book.setTitle(updateBookCommand.getTitle())
                book.setPublishing(updateBookCommand.getPublishing())
                book.setSubject(updateBookCommand.getSubject())

                // Save
                book.save()

                // Render
                renderJson(modelService.getObjectMapper().writeValueAsString(book.properties))
            }
        }
    }

    def delete(){

        // Book was given
        withBook { Book book ->

            // Delete
            book.delete()

            // Render
            renderJson(modelService.getObjectMapper().writeValueAsString(book.properties))
        }
    }

    def create(CreateBookCommand createBookCommand){

        // Not valid
        if (!createBookCommand.validate()) {

            response.setStatus(HttpStatus.SC_BAD_REQUEST)
            renderJson(GeneralUtils.errorJson(createBookCommand.errors.allErrors.collect {
                it.toString()
            }.join(" , ")))
        } else {

            Book book = new Book()

            // Set fields
            book.setCreated(new Date())
            book.setModified(book.getCreated())
            book.setTitle(createBookCommand.getTitle())
            book.setPublishing(createBookCommand.getPublishing())
            book.setSubject(createBookCommand.getSubject())

            // Save
            book.save()

            // render
            response.setStatus(HttpStatus.SC_CREATED)
            renderJson(modelService.getObjectMapper().writeValueAsString(book.properties))
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
}
