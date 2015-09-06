class UrlMappings {

	static mappings = {
        "/$controller/$action?/$id?(.$format)?"{
            constraints {
                // apply constraints here
            }
        }

        "/"(view:"/index")
        "500"(view:'/error')

        "/books/$BookId?" (controller: 'Books', parseRequest: true){
            action = [GET:"show", PUT:"update", DELETE:"delete", POST:"create"]
        }

        "/solutions/$solutionId?" (controller: 'Solutions', parseRequest: true){
            action = [GET:"show", PUT:"update", DELETE:"delete", POST:"create"]
        }
	}

}
