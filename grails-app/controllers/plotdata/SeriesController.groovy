package plotdata

import org.codehaus.groovy.grails.commons.ConfigurationHolder
import groovy.sql.Sql

class SqlBackend {
	public static Sql connect() {
		def ds = ConfigurationHolder.config.dataSource
		return Sql.newInstance(ds.url, ds.username, ds.password, ds.driverClassName)
	}
}

class SeriesController {

	def json(Long id, String type, String name, Long strictlyafter) {
		Sql sql = SqlBackend.connect()
		if (!strictlyafter) { strictlyafter=0 }
		
		render (contentType: "application/json") {
			if (id) {
				sql.rows("SELECT * from `points` where series_id=? and time>?", [id, strictlyafter])
			} else if ((name) && (type)) {
				sql.rows("SELECT * from `points` where series_id=(SELECT id from series where type=? and name=?) and time>?", 
					[type, name, strictlyafter])
			} else {
				sql.rows("SELECT * from `series`")
			}
		}
		sql.close()
	}
}
