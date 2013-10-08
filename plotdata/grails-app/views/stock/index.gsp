<html><head>


		<meta http-equiv="Content-Type" content="text/html;charset=utf-8" >
		<meta name="author" content="Flux User" >
		<meta name="description" content="My Website" >
		<meta name="keywords" content="Flux, Mac" >
		<link href="main.css" rel="stylesheet" media="screen" type="text/css" >
		<title>My Webpage</title>
	
<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.8.2/jquery.min.js" >
</script>

<!--<script src="js/highcharts.js" ></script>-->
<script src="http://code.highcharts.com/stock/highstock.js" ></script>
<!--<script src="js/modules/exporting.js" > </script>
-->
<script type="text/javascript" >


if (window.jQuery) {
	console.log("jQuery is now loaded"); // What if loaded multiple times?
} else {
	console.error("Cannot load jQuery");
}
</script>

<script type="text/javascript">

function stockChart(ticker, divname) {
	console.log("function stockChart(ticker:"+ticker+", divname:"+divname+")")
	var url="json/"+ticker;
	document.write("<a href=\"" + url +"\">JSON for "+ticker+"</a><br>")
	console.log("Retrieving JSON from url:"+url);
	$.getJSON(url, function(data) {
		console.log("getJSON received data for "+ticker+", "+container);	
		$('#'+divname).highcharts('StockChart', {
			
			rangeSelector : {
				selected : 1
			},

			title : {
				text : ticker+' Stock Price'
			},
			
			series : [{
				name : ticker,
				data : data,
				tooltip: {
					valueDecimals: 2
				}
			}]
		
		}) // setting highcharts for a container
	}).done( function(d) {
		console.log("getJSON for "+ticker+", "+container+" done")
	}).fail( function(d, textStatus, error) {
		console.error("getJSON for "+ticker+", "+container+" failed status: " + textStatus + ", error: "+error)
	}).always( function(d) {
		console.log("getJSON for "+ticker+", "+container+" complete")
	}); // getJSON
} // function stockChart

console.log("calling stockChart for AAPL")
stockChart('AAPL', 'container')
stockChart('ADBE', 'container2')
</script>


</head>
	<body style="left:61px; top:31px; " >
	
This is index.gsp.
	
<div id="container" style="position:relative; width:100%; height:400px; left:273px; top:37px; " ></div>
<div id="container2" style="position:relative; height:283px; width:1012px; " ></div>
	
<p></p>

<p></p></body></html>