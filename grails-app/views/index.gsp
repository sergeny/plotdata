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
			
			#series-list {
				float: none
			}
			
			#container {
				float: right;
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
		
	
	</head>
	
	     
	
	
	<body>


	<!--	
			<div id="container" style="position:relative; width:100%; height:400px; left:273px; top:37px; " ></div>
			<div id="container2" style="position:relative; height:283px; width:1012px; " ></div>
		-->
		<a href="#page-body" class="skip"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<!--<<div id="status" role="complementary">
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
-->
		<div id="page-body" role="main">
			<h1>Graphing data</h1>
			<p>Pick the data you would like to graph</p>

			<!--<div id="controller-list" role="navigation">
			
				<h2>Available Controllers:</h2>
				<ul>
					<g:each var="c" in="${grailsApplication.controllerClasses.sort { it.fullName } }">
						<li class="controller"><g:link controller="${c.logicalPropertyName}">${c.fullName}</g:link></li>
					</g:each>
				</ul>
			
			</div>
			-->
				<div id="series-list" role="navigation">
						Error: Cannot display series-list
				</div>
			
			
	<div id="container" style="position:relative; width:90%; height:400px; left:0px; top:37px; " ></div>
		
		</div>
		
		<!--<g:javascript src="Highstock-1/js/highstock.js" />-->
		
		<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.8.2/jquery.min.js" ></script>
		<script src="http://code.highcharts.com/stock/highstock.js"  onload="console.log(123);"></script>
		<script src="http://code.highcharts.com/stock/modules/exporting.js"  onload="console.log(456)"></script>
		
				
		<script type="text/javascript">
		    if (!window.navigator.onLine) {
				alert("It appears that your computer is offline. Things probably won't work...");
			}

			if (window.jQuery==undefined) {
				console.warn("Cannot load jQuery. Loading from a secondary source...")
				newscript = document.createElement("script");
				newscript.type = "text/javascript";
				// <g:javascript library="jquery" /> is nicer... but the templating in Grails moves it elsewhere :(
				newscript.src = "/plotdata/static/plugins/jquery-1.8.3/js/jquery/jquery-1.8.3.min.js"; 
				//newscript.src= "/Users/macowner/.grails/2.2.3/projects/plotdata/plugins/jquery-1.8.3/web-app/js/jquery/jquery-1.8.3.min.js"; 
				newscript.onload = function() { console.log("Loaded jQuery from a secondary source"); dojQuery(); }
				document.head.appendChild(newscript);
			} else {
				console.log("Loaded jQuery from the primary source");
				dojQuery();
			}

		// All jQuery code is here
		function dojQuery() {
		// First load jQuery
		// Then load highstock.js
		// Then load exporting.js
		// Exactly in this order
	
			if (window.jQuery==undefined) { console.error("Loaded but failed to initialize jQuery. Weird but it happens..."); }
			$( function() { 
				console.log("jQuery works. Checking Highcharts...");
			if ($().highcharts == undefined) { // still need to load Highcharts
				console.log("Loading Highcharts from a secondary source...");
				newscript = document.createElement("script");
				newscript.type = "text/javascript";
				newscript.src = "/plotdata/static/js/HighStock-1/js/highstock.js"; 
				newscript.onload = function() { 
					console.log("Loaded highstock.js"); 
					var newscript = document.createElement("script"); // Scoping with 'var' just to be safe
					newscript.type = "text/javascript";
					newscript.src = "/plotdata/static/js/HighStock-1/js/modules/exporting.js"; 
					newscript.onload = function() { console.log("Loaded exporting.js"); doCharts(); }
					document.head.appendChild(newscript);
				};	
				document.head.appendChild(newscript);
			} else {
				doCharts();
			}
		});
	};

		function doCharts() {
			console.log("doCharts");
			


			

				$.getJSON('series/json', function(data) {
				var s='<table>'
				for (var i=0; i<data.length; i++) {
					s += '<tr onclick="showSeries('+data[i].id+')"><td>'+data[i].type+'</td><td>'+data[i].name+'</td></tr>'
				}
				s+='</table>';
			    /*var tbl = document.createElement("table");
				tbl.setAttribute("border", "2");
				var tbody = document.createElement("tbody");
				tbl.appendChild(tbody);
				var row = document.createElement("tr");
				tbody.appendChild(row);
				var cell = document.createElement("td");
				row.appentChild(cell);
				cell.appendChild(document.createTextNode("hello, world!"));*/
			/*	document.getElementById("series-list").apendChild("test");
				alert(""+document.getElementById("container"));*/

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



				seriesChart('container2', 5);
			//	seriesChart('container', 5)


			
		}

		function showSeries(i) {
			seriesChart('container', i)
		}

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



		</script>
		
		
	</body>
</html>
