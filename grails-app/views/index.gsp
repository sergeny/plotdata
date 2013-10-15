<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main"/>
		<title>Plotdata real-time charts application</title>
		<script type="text/javascript">
			// GLOBAL VARIABLES
			window.refreshIntervalId = undefined; // Id of the function currently doing live updates of the chart. Used by setInterval and clearInterval.			
			window.chartRefreshPeriodMs = 1000;   // Refresh the charts every that many milliseconds
		</script>
		
		<style type="text/css" media="screen">
			/* GLOBAL STYLE SETTINGS
			
			 Position the main elements on the screen
			 The chart and the menu to pick, which chart to display
			*/
			#all_series_menu {
				float: none;
			} 
			
			
			/* Alternating lines in the main menu */
			#all_series_menu tr.odd {
				background: #87d787;
			}
			#all_series_menu tr.even {
				background: #afefaf;
			}
	
			#chart_container {
				float: none;
			}
		


			#page-body {
				margin: 2em 1em 1.25em 4em;
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

	

			@media screen and (max-width: 480px) {
		

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
		<div id="page-body" role="main">
			<h1>Graphing data</h1>
			<p>Pick the data you would like to graph</p>

			<div id="all_series_menu" role="navigation">
				<g:render template="/series/all_series_menu" model="[on_click_callback:'showSeries']" />
			</div>
				
			<div id="chart_container" style="position:relative; width:90%; height:400px; left:0px; top:37px; " ></div>
		
		</div>
		
	
		
		<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.8.2/jquery.min.js" ></script>
		<script src="http://code.highcharts.com/stock/highstock.js"  onload="console.log(123);"></script>
		<script src="http://code.highcharts.com/stock/modules/exporting.js"  onload="console.log(456)"></script>
		
	
	
	<!-- TRYING TO LOAD jQUERY and other libraries if offline... Not very successfully so far... 
	Not sure if this is because of Highcharts or because of how Grails treats different types of files in web-app (assigning them different urls)-->			
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
						useUTC: false   /* So the timestamps will be properly interpreted when you hover over a graph */
					}
				})
				seriesChart('chart_container', 'local', 'freemem');		
		}

		function showSeries(series_type, series_name /* i */) {
			seriesChart('chart_container', series_type, series_name /* i */)
		}
		
		
		// data is an array of the form [[timestamp_1, value_1], [timestamp_2, value_2], ... ]
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
									// set up the updating of the chart so it is live
									
									var series = this.series[0];
									// Store the last timestamp to request incremental updates
									var last_ts = max_timestamp(data);
									console.log("last timestamp: " + last_ts);
										// Very important. If we already have a scheduled function regularly updating a live chart, we have to 
										// cancel it before creating another one. Otherwise we can have 10 functions running on the timer
										// even if the charts are not actually displayed.
										if (window.refreshIntervalId != undefined) {
											clearInterval(window.refreshIntervalId);
											window.refreshIntervalId = undefined;
										}
										window.refreshIntervalId = setInterval( function() {
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
