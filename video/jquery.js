$(function(){


var img = "#img0";
var imageNumber = 0;
var switchLine = $(".switchLine");
var switchTriangle= $(".switchTriangle");



$("#img0 .current").hide();
$("#img0 polyline").hide();

$("#img1 .current").hide();
$("#img1 polyline").hide();

$("#img2 .current").hide();
$("#img2 polyline").hide();

$("#img3 .current").hide();
$("#img3 polyline").hide();

$("#img4 .current").hide();
$("#img4 polyline").hide();

$("#img5 .current").hide();
$("#img5 polyline").hide();

$("#img7 .current").hide();
$("#img7 polyline").hide();


$(".button").on("click",function(){
	$(img).hide();
	var index = $(".button").index(this) ;
	img = "#img" + index;
	$(img).show();
	$(".button").removeClass("active");
	$(this).addClass("active");
	if(index == 7){
		switchLine.attr("class", "addSwitch");	
		switchTriangle.attr("class", "addSwitch");
	}
});

});