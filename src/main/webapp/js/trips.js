$(document).ready(function() {

//Servlet get-my-trips
    $.get("../get-my-trips", function(data) {
        console.log(data);
        //Pour chaque trip, on append
        console.log(data.trips.list[0]);
        for(var i = 0; i < data.trips.size; i++) {
            var trip = data.trips.list[i].trip;
            $(".trips").append('<div class="col-lg-12 col-sm-12 col-xs-12"> \
					<div \
					class="brdr bgc-fff pad-10 box-shad btm-mrg-20 "> \
					<div class="media"> \
					<a class="pull-left" href="trip.html?id='+trip.id+'"><img class="img-responsive list" \
					src="http://lorempixel.com/100/100/city/"></a> \
					<div class="media-body fnt-smaller"> \
					<a href="trip.html?id='+trip.id+'"></a> \
					<h4 class="media-heading"> \
					<a href="trip.html?id='+trip.id+'">'+trip.name+'</a> \
					</h4> \
					<ul class="list-inline mrg-0 btm-mrg-10 clr-535353"> \
					<li>From '+trip.begins+'</li> \
					<li style="list-style: none">|</li> \
					<li>To '+trip.ends+'</li> \
					</ul> \
					<p class="hidden-xs"> ?? participants</p> \
					<span class="fnt-smaller fnt-lighter fnt-arial">'+trip.description+'</span> \
					</div> \
					</div> \
					</div> \
			</div>');
        }
    });
});