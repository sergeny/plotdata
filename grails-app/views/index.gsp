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
					 Loading series-list...
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
				Highcharts.setOptions({
					global: {
						useUTC: false
					}
				})


			

				$.getJSON('series/json', function(data) {
				var s='<table>'
				for (var i=0; i<data.length; i++) {
					s += '<tr onclick="showSeries(\''+data[i].type+'\', \'' + data[i].name + '\')"><td>'+data[i].type+'</td><td>'+data[i].name+'</td></tr>'
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



				seriesChart('container2', 'local', 'freemem');
			//	seriesChart('container', 5)


			
		}

		function showSeries(series_type, series_name /* i */) {
			seriesChart('container', series_type, series_name /* i */)
		}
		
		
		
		function max_timestamp(data) {
			var m = 0;
			for (var i = 0; i < data.length; i++) {
				if (m < data[i][0]) {
					m = data[i][0];
				}
			}
			return m;
		}
	
	

		function seriesChart(divname, series_type, series_name /* series_id */) {

			console.log("stockChart divname="+divname)
			var get_json_url = 'series/json?type=' + series_type + '&name=' + series_name; 
			$.getJSON(get_json_url, function(data) {
				console.log("Binding a chart to the container '" + divname + "'");
				$('#'+divname).highcharts('StockChart', {
						chart : {
							events: {
								load: function () {
									// set up the updating of the chart every once in a while
									
									var series = this.series[0];
									// Store the last timestamp to request incremental updates
									var last_ts = max_timestamp(data);
									console.log("last timestamp: " + last_ts);
									
									setInterval( function() {
										console.log("Iteration last_ts="+last_ts);
										var x = 1, y = 2;
											var x = (new Date()).getTime(), // current time
											y = 2;
									
										$.getJSON(get_json_url + '&strictlyafter=' + last_ts, function(new_data) { // request any new data 
											if (new_data.length == 0) {
												return;
											}
											console.log("Received new data: " + new_data);
										
											
											for (var i = 0; i < new_data.length; i++) {
												series.addPoint(new_data[i], true, true); // note the last "true"
												//console.log("ts: " + max_timestamp(data));
												//console.log("adding point "+data[i]);
											//	series.addPoint([data[i][0], data[i][1]], true, true);
											}
											last_ts = max_timestamp(new_data); // it is supposed to increase
											
										}); // $.getJSON
									}, 5000); // each 5 seconds
									
								/*	setInterval( (function(x) { return function() { // Bind x into the function
											x=x+1;
										console.debug("get json " + get_json_url + '&strictlyafter=' + x);
										$.getJSON(get_json_url + '&strictlyafter=' + x, (function(x) { return function(data) { // Again bind x into the function
												console.log("got json" + x);
												series.addPoint([1,2], true, true);
												//series = series.concat(data);
												//var x = (new Date()).getTime(), // current time
												//y = Math.round(Math.random() * 100);
												//series.addPoint([x, y], true, true);
										}; })(x)); // $.getJSON
									}; })(x), 1000); // exery second	*/							
								}
								}
						},
						rangeSelector : {
							selected : 1
						},

						title : {
							text : ' Graph of ' + series_type + ':' + series_name
						},

						series : [{
							name : series_type + ':' + series_name,
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
