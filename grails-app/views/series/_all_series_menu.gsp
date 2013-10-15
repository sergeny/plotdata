<% 
def ds = org.codehaus.groovy.grails.commons.ConfigurationHolder.config.dataSource
def sql = groovy.sql.Sql.newInstance(ds.url, ds.username, ds.password, ds.driverClassName)
def all_series = sql.rows("SELECT * from `series`")
%>
${param}

<TABLE>
	<TBODY>
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