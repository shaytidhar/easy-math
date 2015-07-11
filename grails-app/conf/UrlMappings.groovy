class UrlMappings {

	static mappings = {
        "/$controller/$action?/$id?(.$format)?"{
            constraints {
                // apply constraints here
            }
        }

        "/"(view:"/index")
        "500"(view:'/error')

        "/Books/$BookId?" (controller: 'Books', parseRequest: true){
            action = [GET:"show", PUT:"update", DELETE:"delete", POST:"create"]
        }
	}

}
