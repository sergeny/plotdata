<% 
def all_series
try {
	def ds = org.codehaus.groovy.grails.commons.ConfigurationHolder.config.dataSource
	def sql = groovy.sql.Sql.newInstance(ds.url, ds.username, ds.password, ds.driverClassName)
	all_series = sql.rows("SELECT * from `series`")
} catch (Throwable t) {
	all_series = null
	// TODO: Should log whatever happened. But for security reasons, do not let the user see what went wrong. 
} finally {
	sql?.close()
}
%>

<TABLE>
	<TBODY>
		<g:if test="${all_series}">
		</g:if>
		<g:else>
			No data is available
		</g:else>
		<g:each in="${all_series}" status="i" var="series">
			<tr class="${(i % 2) == 0 ? 'even' : 'odd'}" 
				onclick="<g:if test="${on_click_callback}">${on_click_callback}('${series.type}', '${series.name}');</g:if>
				<g:else>alert('For some reason this menu is not working. Sorry.');</g:else>">
				<!--${series} X ${series.id} ${series.name} ${series.type} -->
				<td> ${series.type} </td> <td> ${series.name} </td>
			</tr>
			
		</g:each>
	</TBODY>
</TABLE>