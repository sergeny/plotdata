class UrlMappings {

	static mappings = {
		"/stock/json/$ticker?" {
			controller = "stock"
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
