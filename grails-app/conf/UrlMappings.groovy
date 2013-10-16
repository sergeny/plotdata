class UrlMappings {

	static mappings = {
		"/series/json/$type?/$name?" {
			controller = "series"
			action = "json"
		}
		"/$controller/$action?/$id?"{
			constraints {
				// apply constraints here
			}
		}

		"/"(view:"/index")
		"500"(view:'/error')
	}
}
