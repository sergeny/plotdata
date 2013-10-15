<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main"/>
		<title>Welcome to Grails</title>
		<script type="text/javascript">
			window.chartRefreshPeriodMs = 1000;   // Refresh the charts every that many milliseconds
		</script>
		
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



		<a href="#page-body" class="skip"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>

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
				<div id="all_series_menu" role="navigation">
					<g:render template="/series/all_series_menu" model="[on_click_callback:'showSeries']" />
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
				seriesChart('container', 'local', 'freemem');		
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
											console.log("Request " + get_json_url + '&strictlyafter=' + last_ts+ "Received new data: " + new_data);
										
											
											for (var i = 0; i < new_data.length; i++) {
												series.addPoint(new_data[i], true, true); // note the last "true"
												//console.log("ts: " + max_timestamp(data));
												//console.log("adding point "+data[i]);
											//	series.addPoint([data[i][0], data[i][1]], true, true);
											}
											var new_last_ts = max_timestamp(new_data); // it is supposed to increase
											if (new_last_ts > last_ts) {
												console.log("last_ts was " + last_ts + " and is now " + new_last_ts);
											} else {
												console.error("last_ts must increase! last_ts was " + last_ts + " and is now " + new_last_ts);	
											}
											last_ts = new_last_ts;
											
										}); // $.getJSON
									}, window.chartRefreshPeriodMs); // each 5 seconds
															
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
