package easy.math

import org.apache.http.HttpStatus

abstract class BaseController {

    protected renderJson(String json){
        response.setContentType("application/json;charset=UTF-8")
        //response.setCharacterEncoding(Charsets.UTF_8.name())
        render json.toString()
    }

    protected renderInternalError(String error){
        response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR)
        renderJson(GeneralUtils.errorJson(error))
    }

    protected renderNotFound(String reason){
        response.setStatus(HttpStatus.SC_NOT_FOUND)
        renderJson(GeneralUtils.errorJson(reason))
    }

    protected renderForbidden(){
        response.setStatus(HttpStatus.SC_FORBIDDEN)
        renderJson(GeneralUtils.errorJson("Forbidden"))
    }

    protected renderUnauthorized(){
        response.setStatus(HttpStatus.SC_UNAUTHORIZED)
        renderJson(GeneralUtils.errorJson("Unauthorized"))
    }

    protected renderNotImplemented(){
        response.setStatus(HttpStatus.SC_NOT_IMPLEMENTED)
        renderJson("{}")
    }

    protected auth = {

        return true
    }

    def handlesException(Exception e){

        log.error("Exception on $controllerName-$actionName with params: $request.parameterMap. \n" +
                "StackTrace: $e.stackTrace.\n" +
                "Message: $e.message.")

        renderInternalError(e.getMessage())
    }
}
