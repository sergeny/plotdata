<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main"/>
		<title>Welcome to Grails</title>
		<style type="text/css" media="screen">
			#status {
				background-color: #eee;
				border: .2em solid #fff;
				margin: 2em 2em 1em;
				padding: 1em;
				width: 12em;
				float: left;
				-moz-box-shadow: 0px 0px 1.25em #ccc;
				-webkit-box-shadow: 0px 0px 1.25em #ccc;
				box-shadow: 0px 0px 1.25em #ccc;
				-moz-border-radius: 0.6em;
				-webkit-border-radius: 0.6em;
				border-radius: 0.6em;
			}

			.ie6 #status {
				display: inline; /* float double margin fix http://www.positioniseverything.net/explorer/doubled-margin.html */
			}

			#status ul {
				font-size: 0.9em;
				list-style-type: none;
				margin-bottom: 0.6em;
				padding: 0;
			}

			#status li {
				line-height: 1.3;
			}

			#status h1 {
				text-transform: uppercase;
				font-size: 1.1em;
				margin: 0 0 0.3em;
			}

			#page-body {
				margin: 2em 1em 1.25em 18em;
			}

			h2 {
				margin-top: 1em;
				margin-bottom: 0.3em;
				font-size: 1em;
			}

			p {
				line-height: 1.5;
				margin: 0.25em 0;
			}

			#controller-list ul {
				list-style-position: inside;
			}

			#controller-list li {
				line-height: 1.3;
				list-style-position: inside;
				margin: 0.25em 0;
			}

			@media screen and (max-width: 480px) {
				#status {
					display: none;
				}

				#page-body {
					margin: 0 1em 1em;
				}

				#page-body h1 {
					margin-top: 0;
				}
			}
		</style>
		





	<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.8.2/jquery.min.js" ></script>

				 <!--"Highstock-1/js/modules/exporting.js"-->
<!--			<script type="text/javascript" src="/Users/macowner/.grails//2.2.3/projects/plotdata/plugins/jquery-1.8.3/web-app/js/jquery/jquery-1.8.3.min.js" />-->
			<script type="text/javascript" >


		
		</script>

		
		<script type="text/javascript">
	
		
	
	//	stockChart('AAPL', 'container');
	//	stockChart('ADBE', 'container2');

		</script>
	</head>
	
	     
	
	
	<body>

<script type="text/javascript">


function loadScript(url, onsuccess, onerror) {
$.get(url)
    .done(function() {
        // File/url exists
        console.log("JS Loader: file exists, executing $.getScript "+url)
        $.getScript(url, function() {
            if (onsuccess) {
                console.log("JS Loader: Ok, loaded. Calling onsuccess() for " + url);
                onsuccess();
                console.log("JS Loader: done with onsuccess() for " + url);
            } else {
                console.log("JS Loader: Ok, loaded, no onsuccess() callback " + url)
            }
        });
    }).fail(function() {
            // File/url does not exist
            if (onerror) {
                console.error("JS Loader: probably 404 not found. Not calling $.getScript. Calling onerror() for " + url);
                onerror();
                console.error("JS Loader: done with onerror() for " + url);
            } else {
                console.error("JS Loader: probably 404 not found. Not calling $.getScript. No onerror() callback " + url);
            }
    });
}

//loadScript("http://localhost:8080/plotdata/static/js/Highstock-1/js/highstock.js");
	if (window.jQuery) {
	        console.log("jQuery is now loaded"); // What if loaded multiple times?
	} else {
	        console.error("Cannot load jQuery");
	
	}
	
	function showSeries(i) {
		seriesChart('container', i)
	}
	
	$.getJSON('series/json', function(data) {
	var s='<table>'
	for (var i=0; i<data.length; i++) {
		s += '<tr onclick="showSeries('+data[i].id+')"><td>'+data[i].type+'</td><td>'+data[i].name+'</td></tr>'
	}
	s+='</table>'
	$('#series-list').html(s)
		//document.write("HELLO<b1>HI</b1>")
		//document.writeln("got data "+data)
	}).done(function(d) {
			console.log("success, done");
		}).fail(function(d) {
			console.error("fail");
		}).always(function(d) {
			console.log("complete");
		});
	
	
	
		function seriesChart(divname, series_id) {
	
		console.log("stockChart divname="+divname)
		$.getJSON('series/json?id=' + series_id, function(data) {
			console.log("Binding a chart to the container '" + divname + "'");
		$('#'+divname).highcharts('StockChart', {


					rangeSelector : {
						selected : 1
					},

					title : {
						text : ' Graph'
					},

					series : [{
						name : 'my series',
						data : data,
						tooltip: {
							valueDecimals: 2
						}
					}]


		})

		}).done(function(d) {
			console.log("success, done");
		}).fail(function(d) {
			console.error("fail");
		}).always(function(d) {
			console.log("complete");
		});
		
	}
	seriesChart('container', 7)
//	seriesChart('container', 5)
	
	
		</script>
	<!--	
			<div id="container" style="position:relative; width:100%; height:400px; left:273px; top:37px; " ></div>
			<div id="container2" style="position:relative; height:283px; width:1012px; " ></div>
		-->
		<a href="#page-body" class="skip"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div id="status" role="complementary">
			<h1>Application Status</h1>
			<ul>
				<li>App version: <g:meta name="app.version"/></li>
				<li>Grails version: <g:meta name="app.grails.version"/></li>
				<li>Groovy version: ${GroovySystem.getVersion()}</li>
				<li>JVM version: ${System.getProperty('java.version')}</li>
				<li>Reloading active: ${grails.util.Environment.reloadingAgentEnabled}</li>
				<li>Controllers: ${grailsApplication.controllerClasses.size()}</li>
				<li>Domains: ${grailsApplication.domainClasses.size()}</li>
				<li>Services: ${grailsApplication.serviceClasses.size()}</li>
				<li>Tag Libraries: ${grailsApplication.tagLibClasses.size()}</li>
			</ul>
			<h1>Installed Plugins</h1>
			<ul>
				<g:each var="plugin" in="${applicationContext.getBean('pluginManager').allPlugins}">
					<li>${plugin.name} - ${plugin.version}</li>
				</g:each>
			</ul>
		</div>
		<div id="page-body" role="main">
			<h1>Welcome to Grails2</h1>
			<p>Congratulations, you have successfully started your first Grails application! At the moment
			   this is the default page, feel free to modify it to either redirect to a controller or display whatever
			   content you may choose. Below is a list of controllers that are currently deployed in this application,
			   click on each to execute its default action:</p>

			<div id="controller-list" role="navigation">
				<h2>Available Controllers:</h2>
				<ul>
					<g:each var="c" in="${grailsApplication.controllerClasses.sort { it.fullName } }">
						<li class="controller"><g:link controller="${c.logicalPropertyName}">${c.fullName}</g:link></li>
					</g:each>
				</ul>
			</div>
			
			<div id="series-list" role="navigation">
			</div>
	<div id="container" style="position:relative; width:90%; height:400px; left:0px; top:37px; " ></div>
		
		</div>
		
		<!--<g:javascript src="Highstock-1/js/highstock.js" />-->
		
		<script src="http://code.highcharts.com/stock/highstock.js"  onload="console.log(123);"></script>
		<script src="http://code.highcharts.com/stock/modules/exporting.js"  onload="console.log(456)"></script>
		
	</body>
</html>
