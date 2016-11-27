

$(document).ready(function(){
    var pagenumcpt = 0;
    var trip_id = getUrlParameter("id");
    //INDEX.HTML
    //---> Placer correctement le bandeau du index.html au chargement, et pendant les resize.
    function fullscreen(){
        $('#hero').css({
            width: $(window).width(),
            height: $(window).height()
        });
    }
    if($('body').is('.index')) { fullscreen(); }
    $(window).resize(function() {
        if($('body').is('.index')) { fullscreen(); }
    });
    //---> Gérer correctement les boutons signin et signup, avec un affichage sans changer de page.
    if($('body').is('.index')) {
        var modal = $('#id01');
        var modal2 = $('#id02');
        $('#bt01').click(function(event){
            modal.css('display', 'block');
            event.stopPropagation();
        });
        $('#bt02').click(function(event){
            modal2.css('display', 'block');
            event.stopPropagation();
        });
        $('.signbox').click(function(event){ event.stopPropagation(); });
        $(window).click(function(event){
            console.log("modals closed because you click outside of the box");
            modal.css('display', 'none');
            modal2.css('display', 'none');
        });
    }

    //---> Soumission du formulaire pour se connecter
    $("#form-sign-in").submit(function(event){
        event.preventDefault();
        var $form = $(this),
            url = $form.attr('action');
        $.ajax({
            type: "POST",
            url: url,
            dataType: "json",
            data: $form.serialize(),
            success: function(data){
                console.log(data);
                if(data.requestValid === "yes"){
                    if(data.sessionType === "new"){
                        $("#id01").css("display", "none");
                        load_bar();
                    }
                    else{$("#sign-in-res").empty().append("<p>Already signed in</p>")}
                }
                else{$("#sign-in-res").empty().append("<p>Failed</p>")}
            },
            error: function(requestObj, status, error){
                $("#sign-in-res").empty().append("<p>Failed : " + status + " : "+ error+"</p>");
            }
        });
    });

    //---> Changement de la barre de navigation en fonction de l'état (connecté/déconnecté)
    function load_bar(){
        $.ajax({
            type: "GET",
            url: "./sign-in",
            dataType: "json",
            success: function (data) {
                if (data.has_session == true){
                    $("#first_div").toggleClass("hidden", true);
                    $("#second_div").toggleClass("hidden", false);
                }
                else{
                    $("#first_div").toggleClass("hidden", false);
                    $("#second_div").toggleClass("hidden", true);
                }
            },
            error: function (requestObj, status, error) {
                console.log(requestObj + " : " + status + " : " + error);
                $("#first_div").toggleClass("hidden", true);
                $("#second_div").toggleClass("hidden", false);

            }
        });
    }
    if($('body').is('.index') || $('body').is('.about') || $('body').is('.contact') || $('body').is('.faqs')|| $('body').is('.dev')|| $('body').is('.terms')) {	load_bar();	}

    //---> Placer le footer en pied de page
    $('body:last-child').append("<footer class='footer'><div class='container text-center'><div class='footer_top'><div class='col-md-6'><h5>Support</h5><ul class='nav nav-pills nav-stacked'><li><a href='/TravelToParis/html/help-and-faqs.html'>Help &amp; FAQs</a></li><li><a href='/TravelToParis/html/privacy-and-terms.html'>Privacy &amp; Terms</a></li><li><a href='/TravelToParis/html/developers.html'>Developers</a></li></ul></div><div class='col-md-6'><h5>Travel to Paris</h5><ul class='nav nav-pills nav-stacked'><li><a href='/TravelToParis/html/about.html'>About us</a></li><li><a href='/TravelToParis/html/contact.html'>Contact us</a></li></ul></div></div><div class='col-md-12'><p>Copyright &copy; 2016. All rights reserved.</p></div></div></footer>");

    if($('body').is('.index')) {
//		-->	SET THE MINIMUM AGE TO SIGN UP
        var min_birth = new Date();
        var dd = min_birth.getDate();
        var mm = min_birth.getMonth()+1;
        var yyyy = min_birth.getFullYear() - 15;
        if(dd<10){ dd='0'+dd }
        if(mm<10){ mm='0'+mm }
        min_birth = yyyy+'-'+mm+'-'+dd;
        document.getElementById("birth").setAttribute("max", min_birth);

//		--->	SET THE MAXIMUM AGE TO SIGN UP
        var max_birth = new Date();
        dd = max_birth.getDate();
        mm = max_birth.getMonth()+1;
        yyyy = max_birth.getFullYear() - 120;
        if(dd<10) dd='0'+dd;
        if(mm<10) mm='0'+mm;
        max_birth = yyyy+'-'+mm+'-'+dd;
        document.getElementById("birth").setAttribute("min", max_birth);

//		--->	from http://www.freeformatter.com/iso-country-list-html-select.html 
        var countries = [
            "Afghanistan","Aland Islands","Albania","Algeria","American Samoa","Andorra","Angola","Anguilla","Antarctica","Antigua and Barbuda","Argentina","Armenia","Aruba","Australia","Austria","Azerbaijan","Bahamas","Bahrain","Bangladesh","Barbados","Belarus","Belgium","Belize","Benin","Bermuda","Bhutan","Bolivia, Plurinational State of","Bonaire, Sint Eustatius and Saba","Bosnia and Herzegovina","Botswana","Bouvet Island","Brazil","British Indian Ocean Territory","Brunei Darussalam","Bulgaria","Burkina Faso","Burundi","Cambodia","Cameroon","Canada","Cape Verde","Cayman Islands","Central African Republic","Chad","Chile","China","Christmas Island","Cocos (Keeling) Islands","Colombia","Comoros","Congo","Congo, the Democratic Republic of the","Cook Islands","Costa Rica","Côte d'Ivoire","Croatia","Cuba","Curaçao","Cyprus","Czech Republic","Denmark","Djibouti","Dominica","Dominican Republic","Ecuador","Egypt","El Salvador","Equatorial Guinea","Eritrea","Estonia","Ethiopia","Falkland Islands (Malvinas)","Faroe Islands","Fiji","Finland","France","French Guiana","French Polynesia","French Southern Territories","Gabon","Gambia","Georgia","Germany","Ghana","Gibraltar","Greece","Greenland","Grenada","Guadeloupe","Guam","Guatemala","Guernsey","Guinea","Guinea-Bissau","Guyana","Haiti","Heard Island and McDonald Islands","Holy See (Vatican City State)","Honduras","Hong Kong","Hungary","Iceland","India","Indonesia","Iran, Islamic Republic of","Iraq","Ireland","Isle of Man","Israel","Italy","Jamaica","Japan","Jersey","Jordan","Kazakhstan","Kenya","Kiribati","Korea, Democratic People's Republic of","Korea, Republic of","Kuwait","Kyrgyzstan","Lao People's Democratic Republic","Latvia","Lebanon","Lesotho","Liberia","Libya","Liechtenstein","Lithuania","Luxembourg","Macao","Macedonia, the former Yugoslav Republic of","Madagascar","Malawi","Malaysia","Maldives","Mali","Malta","Marshall Islands","Martinique","Mauritania","Mauritius","Mayotte","Mexico","Micronesia, Federated States of","Moldova, Republic of","Monaco","Mongolia","Montenegro","Montserrat","Morocco","Mozambique","Myanmar","Namibia","Nauru","Nepal","Netherlands","New Caledonia","New Zealand","Nicaragua","Niger","Nigeria","Niue","Norfolk Island","Northern Mariana Islands","Norway","Oman","Pakistan","Palau","Palestinian Territory, Occupied","Panama","Papua New Guinea","Paraguay","Peru","Philippines","Pitcairn","Poland","Portugal","Puerto Rico","Qatar","Réunion","Romania","Russian Federation","Rwanda","Saint Barthélemy","Saint Helena, Ascension and Tristan da Cunha","Saint Kitts and Nevis","Saint Lucia","Saint Martin (French part)","Saint Pierre and Miquelon","Saint Vincent and the Grenadines","Samoa","San Marino","Sao Tome and Principe","Saudi Arabia","Senegal","Serbia","Seychelles","Sierra Leone","Singapore","Sint Maarten (Dutch part)","Slovakia","Slovenia","Solomon Islands","Somalia","South Africa","South Georgia and the South Sandwich Islands","South Sudan","Spain","Sri Lanka","Sudan","Suriname","Svalbard and Jan Mayen","Swaziland","Sweden","Switzerland","Syrian Arab Republic","Taiwan, Province of China","Tajikistan","Tanzania, United Republic of","Thailand","Timor-Leste","Togo","Tokelau","Tonga","Trinidad and Tobago","Tunisia","Turkey","Turkmenistan","Turks and Caicos Islands","Tuvalu","Uganda","Ukraine","United Arab Emirates","United Kingdom","United States","United States Minor Outlying Islands","Uruguay","Uzbekistan","Vanuatu","Venezuela, Bolivarian Republic of","Viet Nam","Virgin Islands, British","Virgin Islands, U.S.","Wallis and Futuna","Western Sahara","Yemen","Zambia","Zimbabwe"
        ];
        $( "#countries" ).autocomplete({
            source: countries
        });
    }

//	--> Vérifier le match des deux passwords indiqués.

    $('#password_confirm').on('input', function checkpw() {
        if ($(this).value != $('#password').value) {
            document.getElementById("password_confirm").setCustomValidity('Password Must be Matching.'); //Check pattern TODO : En fonction de la langue ?
        } else {
            document.getElementById("password_confirm").setCustomValidity('');
        }
    });

    //---> Ajouter les champs pour saisir des adresses mails.
    var i = 0;
    function addFriend() {
        var add = $('#add');
        add.attr('id','changing');

        add.toggleClass('glyphicon-plus');
        add.toggleClass('glyphicon-minus');
        add.unbind(); //On enlève tous les listeners, et en particulier celui de addFriend qui persiste.
        add.on('click', function() {
            $(this).parent().parent().fadeOut(500, function() { $(this).remove(); })
        });

        $('<tr><td><input type="text" name="friend" class="form-control unames"></td><td><i id="add" class="glyphicon glyphicon-plus"></i></td></tr>').fadeIn(500).insertAfter(add.parent().parent());
        add.removeAttr('id');
        var add = $('#add');
        add.on('click', function() { addFriend(); });
    }
    $('#add').on('click', function() {addFriend();});



    //--->Obtenir un paramètre, si il existe
    function getUrlParameter(sParam){
        var sPageURL = decodeURIComponent(window.location.search.substring(1)),
            sURLVariables = sPageURL.split('&'),
            sParameterName, i;

        for (i = 0; i < sURLVariables.length; i++) {
            sParameterName = sURLVariables[i].split('=');
            if (sParameterName[0] === sParam) {
                return sParameterName[1];
            }
        }
    }

    //---> Si la page est trips, obtenir les trips
    if($('body').is('.trips')) {
        $.get("../get-my-trips", function(data) {
            console.log(data);
            //Pour chaque trip, on append
            console.log(data.trips.list[0]);
            for(var i = 0; i < data.trips.size; i++) {
                var trip = data.trips.list[i].trip;
                $(".row.trips").append('<div class="col-lg-12 col-sm-12 col-xs-12"><div class="expandable brdr bgc-fff pad-10 box-shad btm-mrg-20 property-listing"><div class="media"><a class="pull-left" href="trip.html?id='+trip.id+'"><img class="img-responsive list" src="http://lorempixel.com/100/100/city/"></a><div class="media-body fnt-smaller"><a href="trip.html?id='+trip.id+'"></a><h4 class="media-heading"><a href="trip.html?id='+trip.id+'">'+trip.name+'</a></h4><ul class="list-inline mrg-0 btm-mrg-10 clr-535353"><li>From '+trip.begins+'</li><li style="list-style: none">|</li><li>To '+trip.ends+'</li></ul><p class="hidden-xs"> ?? participants</p><span class="fnt-smaller fnt-lighter fnt-arial">'+trip.description+'</span></div></div></div></div>');
            }
        });
    }

    function removeVote(id_event, id_trip, vote) {
        $.ajax({
            type: "POST",
            url: "../calendar",
            dataType: "json",
            data: {trip_id: id_trip, event_id: id_event, vote: vote, remove: true},
            success: function (data) {
                if (data.status === "failure") console.log("failure t " + id_trip + " e " + id_event);
                else {
                    var elem = $("#" + id_event);
                    if (elem.length && $("#calendar").hasClass("btn-primary")) {
                        var votes = vote ? $("span.upvotes") : $("span.downvotes");
                        var spanVote = elem.find(votes);
                        var up = $("span.badge-notify-up");
                        var down = $("span.badge-notify-down");
                        var spanBadgeUp = elem.find(up);
                        var spanBadgeDown = elem.find(down);
                        spanVote.text(parseInt(spanVote.text()) - 1);
                        spanBadgeDown.off('click');
                        spanBadgeDown.css("background", "rgba(217,83,79,0.3)");
                        spanBadgeDown.click(function () {
                            voteForEvent(id_event, trip_id, false, false);
                        });
                        spanBadgeUp.off('click');
                        spanBadgeUp.css("background", "rgba(91,192,222,0.3)");
                        spanBadgeUp.click(function () {
                            voteForEvent(id_event, trip_id, true, false);
                        });
                    }
                }
            }
        });
    }

    function voteForEvent(id_event, id_trip, vote, other){
        $.ajax({
            type: "POST",
            url: "../calendar",
            dataType: "json",
            data: {trip_id: id_trip, event_id: id_event, vote: vote, remove: false},
            success: function (data) {
                console.log(data.status);
                if (data.status === "failure") console.log("failure t " + id_trip + " e " + id_event);
                else{
                    //location.reload();
                    var elem = $("#"+ id_event);
                    if(elem.length && $("#calendar").hasClass("btn-primary")){
                        var votesUp = $("span.upvotes");
                        var votesDown = $("span.downvotes");
                        var spanVoteUp = elem.find(votesUp);
                        var spanVoteDown = elem.find(votesDown);
                        var up = $("span.badge-notify-up");
                        var down = $("span.badge-notify-down");
                        var spanBadgeUp = elem.find(up);
                        var spanBadgeDown = elem.find(down);
                        if(vote) {
                            spanVoteUp.text(parseInt(spanVoteUp.text()) + 1);
                            if(other) spanVoteDown.text(parseInt(spanVoteDown.text()) - 1);
                        }
                        if(!vote){
                            spanVoteDown.text(parseInt(spanVoteDown.text()) + 1);
                            if(other) spanVoteUp.text(parseInt(spanVoteUp.text()) - 1);
                        }

                        if(vote){
                            spanBadgeUp.off('click');
                            spanBadgeUp.css("background", "rgba(91,192,222,0.7)");
                            spanBadgeUp.click(function () {
                                removeVote(id_event, trip_id, true);
                            });
                            spanBadgeDown.off('click');
                            spanBadgeDown.css("background", "rgba(217,83,79,0.3)");
                            spanBadgeDown.click(function () {
                                voteForEvent(id_event, trip_id, false, true);
                            });

                        } else {
                            spanBadgeDown.off('click');
                            spanBadgeDown.css("background", "rgba(217,83,79,0.7)");
                            spanBadgeDown.click(function () {
                                removeVote(id_event, trip_id, false);
                            });
                            spanBadgeUp.off('click');
                            spanBadgeUp.css("background", "rgba(91,192,222,0.3)");
                            spanBadgeUp.click(function () {
                                voteForEvent(id_event, trip_id, true, true);
                            });
                        }
                    }
                }
            },
            error: function (requestObj, status, error) {
                console.log("req : " + requestObj + " | status : " + status + " | error : " + error);
                window.location.href = "../index.html";
            }
        });
    }

    //---> Dans la page Events, afficher les events
    function callEventsTrip() {
        //Si on affiche pour la première fois : On vide le contenu
        if(pagenumcpt==0) {
            $(".events-trip-elements").empty();
            $(".events-trip-elements").append('<span id="spin1" class="glyphicon glyphicon-hourglass spin-loader center-block" aria-hidden="true"></span>');
            $(".events-trip-elements").append('<button class="btn btn-primary btn-loadmore center-block">Load more...</button>');
        }
        $('.btn-loadmore').hide();
        $("#spin1").show();
        //Les valeurs dont on a besoin
        var begins = "2016-11-27"; console.log("begins : "+begins);
        var ends = "2016-11-30"; console.log("ends : "+ends);
        var select = $('#evt-select-by').find(":selected").text(); console.log("sort by : "+select);
        var cats = [];
        cats = $(".catbox input:checkbox:checked").map(function(){ console.log("Added "+$(this).val()); return $(this).val(); }).get();
        //if(cats.length==0) cats = $(".catbox").map(function(){ console.log("Added "+$(this).val()); return $(this).val(); }).get();
        console.log("categories : "+cats);
        console.log("page : "+pagenumcpt);
        //Executer le Ajax
        $.ajax({
            type: "GET",
            url: "../EventsTrip",
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            data: {id: trip_id, begins: begins, ends: ends, sortby: select, categories: cats, page: pagenumcpt},
            success: function(data){
                //console.log(data);
                if(data.status=="success") {
                    //console.log(data.events.list[0]);
                    for(var i = 0; i < data.events.size; i++) {
                        var event = data.events.list[i];
                        $('<div class="expandable brdr bgc-fff pad-10 box-shad btm-mrg-20 property-listing" id="'+ event.id +'">' +
                            ' <div class="media"> <a class="pull-left" href="'+event.url+'" target="_parent"><img alt="image" class="img-responsive list" src=""></a>' +
                            '<div class="media-body fnt-smaller"><a href="'+event.url+'" target="_parent"></a>' +
                            '<h4 class="media-heading"><a href="'+event.url+'" target="_parent">'+event.name+'</a></h4>' +
                            '<ul class="list-inline mrg-0 btm-mrg-10 clr-535353"><li>FROM '+event.begins+'</li><li style="list-style: none">|</li>' +
                            '<li>TO '+event.ends+'</li></ul><div><span class="pull-right"><i><button class="btn btn-primary add-trip">Add to trip</button></i></span></div>' +
                            '<p class="hidden-xs">'+event.description+'</p></div></div></div>').insertBefore( "#spin1" );
                    }
                    $('.add-trip').click(function (event) {
                        var event_obj = $(event.target).parents().eq(5);
                        var event_id = event_obj.attr("id");
                        voteForEvent(event_id, trip_id, true);
                        event_obj.remove();
                    });
                    //Si on a reçu != 0 résultats, on met le bouton loadmore
                    if(data.events.size != 0){
                        $('.btn-loadmore').show();
                        //$(".events-trip-elements").append('<button class="btn btn-primary btn-loadmore center-block">Load more...</button>');
                    }
                } else {
                    console.log("received a data with field status = failed");
                }
            },
            error: function(requestObj, status, error){
                console.log("req : " + requestObj + " | status : " + status + " | error : " + error);
                window.location.href = "../index.html";
            }
        });
        //Enlever le spinLoader
        $('#spin1').hide();
    }

    function getOverview() {
        $.ajax({
            type: "GET",
            context: this,
            url: "../overview",
            dataType: "json",
            data: {id: trip_id},
            success: function(data){
                $("#trip_name_banner").text(data.name);
                $("#panel_trip_name").text(data.name);
                $("#description_trip").text(data.description);
                $("#nb_participants").text(data.participants);
                $("#from_trip").text(data.begins);
                $("#to_trip").text(data.ends);
                $("#music_trip").text(data.music);
                $("#family_trip").text(data.family);
                $("#food_trip").text(data.food);
                $("#movie_trip").text(data.movie);
                $("#art_trip").text(data.art);
                $("#health_trip").text(data.support);
                $("#museum_trip").text(data.attraction);
                $("#sport_trip").text(data.sports);
                $("#technology_trip").text(data.technology);
                $("#festival_trip").text(data.festival);
                $("#fundraiser_trip").text(data.fundraiser);
                $("#animal_trip").text(data.animals);
            },
            error: function(requestObj, status, error){
                console.log("req : " + requestObj + " | status : " + status + " | error : " + error);
                window.location.href = "../index.html";
            }
        });
    }

    // --> Si la page est trip
    if($('body').is('.trip')) {
        var overview_tab = $("#overview");
        if(trip_id == undefined){window.location.href = "../index.html";}
        if(overview_tab.hasClass("btn-primary")) {getOverview();}
//		-->	Lorsqu'on change de tab
        $(".btn-pref .btn.tab").on('click', function () {
            $(".btn-pref .btn.tab").removeClass("btn-primary").addClass("btn-default");
            $(this).removeClass("btn-default").addClass("btn-primary");
            //Tab Overview

            // si la tab est selectionnée
            if(overview_tab.hasClass("btn-primary")){
                getOverview();
            }
            //Tab Events
            if($("#events").hasClass("btn-primary")){
                pagenumcpt=0;
                callEventsTrip();
                $(".btn-events").on('click', function () {
                    pagenumcpt=0;
                    console.log("[click:btn-events] affichage page "+pagenumcpt);
                    callEventsTrip();
                });


                $(".btn-loadmore").on('click', function () {
                    pagenumcpt++;
                    console.log("[click:btn-loadmore] affichage page "+pagenumcpt);
                    callEventsTrip();
                });
            }

            //Tab Calendar
            if($("#calendar").hasClass("btn-primary")){
                var eventsbody = $('#spin2').parent();
                eventsbody.empty();
                eventsbody.append('<span id="spin2" class="glyphicon glyphicon-hourglass spin-loader" aria-hidden="true"></span>');
                $.ajax({
                    type: "GET",
                    url: "../calendar",
                    dataType: "json",
                    data: {id: trip_id},
                    beforeSend: function () {
                        $('#spin2').show();
                    },
                    success: function (data){
                        $('#spin2').hide();
                        var status = data.status;
                        if(status === "success"){
                            var events = data.events;
                            for(var i = 0; i < events.size; i++) {
                                var elem = events.list[i];
                                $('<div class="brdr bgc-fff pad-10 box-shad btm-mrg-20 property-listing expandable" id="'+ elem.event.id +'">' +
                                    '<div class="media"><a class="pull-left" href="'+elem.event.url+'" target="_parent">'+
                                    '<img alt="image" class="img-responsive list" src=""></a>' +
                                    '<div class="media-body fnt-smaller"><a href="#" target="_parent"></a>' +
                                    '<h4 class="media-heading"><a href="#" target="_parent">'+elem.event.name+'</a></h4>' +
                                    '<ul class="list-inline mrg-0 btm-mrg-10 clr-535353"><li>FROM '+elem.event.begins+'</li>' +
                                    '<li style="list-style: none">|</li><li>TO '+elem.event.ends+'</li></ul>' +
                                    '<div class="like-or-not"><span class="pull-right">' +
                                    '<span class="badge badge-notify-up"><i class="glyphicon glyphicon-thumbs-up"></i><span class="number_vote upvotes">'+ elem.upvotes +'</span></span>' +
                                    '<span class="badge badge-notify-down"><i class="glyphicon glyphicon-thumbs-down"></i><span class="number_vote downvotes">'+elem.downvotes+'</span></span>' +
                                    '<span class="badge badge-notify-remove"><i class="glyphicon glyphicon-remove"></i><span class="number_vote">Remove</span></span>' +
                                    '</span></div><p class="hidden-xs">'+elem.event.description+'</p></div></div></div>').insertBefore("#spin2");
                            }
                            if(events.size > 0){
                                $('.badge-notify-remove').click(function (event) {
                                    var event_obj = $(event.target).parents().eq(5);
                                    var event_id = event_obj.attr("id");
                                    $.ajax({
                                        type: "DELETE",
                                        url: "../calendar",
                                        dataType: "json",
                                        headers: {trip_id: trip_id, event_id: event_id},
                                        success: function (data) {
                                            console.log(data.status);
                                            if(data.status === "failure") console.log("failure t " + trip_id + " e " + event_id);
                                            else event_obj.remove();
                                        },
                                        error: function (requestObj, status, error){
                                            console.log("req : " + requestObj + " | status : " + status + " | error : " + error);
                                            window.location.href = "../index.html";
                                        }
                                    });
                                });
                                var hasUp = parseInt(elem.hasupvote);
                                var hasDown = parseInt(elem.hasdownvote);
                                if(hasUp == 0 && hasDown == 0){
                                    $('.badge-notify-up').click(function (event) {
                                        var event_obj = $(event.target).parents().eq(5);
                                        var event_id = event_obj.attr("id");
                                        voteForEvent(event_id, trip_id, true, false);
                                    });
                                    $('.badge-notify-down').click(function (event) {
                                        var event_obj = $(event.target).parents().eq(5);
                                        var event_id = event_obj.attr("id");
                                        voteForEvent(event_id, trip_id, false, false);
                                    });
                                }
                                else{
                                    if(hasUp > 0){
                                        var badge = $('.badge-notify-up');
                                        badge.click(function (event) {
                                            var event_obj = $(event.target).parents().eq(5);
                                            var event_id = event_obj.attr("id");
                                            removeVote(event_id, trip_id, true);
                                        });
                                        badge.css("background", "rgba(91,192,222,0.7)");
                                        $('.badge-notify-down').click(function (event) {
                                            var event_obj = $(event.target).parents().eq(5);
                                            var event_id = event_obj.attr("id");
                                            voteForEvent(event_id, trip_id, false, true);
                                        });
                                    }
                                    if(hasDown > 0){
                                        badge = $('.badge-notify-down');
                                        badge.click(function (event) {
                                            var event_obj = $(event.target).parents().eq(5);
                                            var event_id = event_obj.attr("id");
                                            removeVote(event_id, trip_id, false);
                                        });
                                        badge.css("background", "rgba(217,83,79,0.7)");
                                        $('.badge-notify-up').click(function (event) {
                                            var event_obj = $(event.target).parents().eq(5);
                                            var event_id = event_obj.attr("id");
                                            voteForEvent(event_id, trip_id, true, true);
                                        });
                                    }
                                }
                            }
                        }
                        else{console.log("Error" + data);}
                    },
                    error: function (requestObj, status, error){
                        console.log("req : " + requestObj + " | status : " + status + " | error : " + error);
                        window.location.href = "../index.html";
                    }
                });
            }

            //Tab Chat
            if($("#chat").hasClass("btn-primary")){}
            //Tab Invitations
            if($("#invitations").hasClass("btn-primary")){
                $('#invite-submit').click(function () {
                    var unames = $('.unames').map(function(){return $(this).val()}).get();
                    $.ajax({
                        type: "POST",
                        url: "../invite-friends",
                        dataType: "json",
                        data: {trip_id:trip_id, users:unames},
                        success: function () {
                            console.log("sucess");
                        },
                        error: function(requestObj, status, error){
                            console.log("req : " + requestObj + " | status : " + status + " | error : " + error);
                            window.location.href = "../index.html";
                        }
                    });
                });

            }
            //Tab Settings
            if($("#settings").hasClass("btn-primary")){
            	$.ajax({
					type: "GET",
					url: "../settings",
					dataType: "json",
					data: {id: trip_id},
					success: function(data){
						$("#settings-tripid").attr("value", trip_id);
						$("#settings-tripname").attr("value", data.name);
						$("#settings-begins").attr("value", data.begins);
						$("#settings-ends").attr("value", data.ends);
						if(data.description!=null)
							$("#settings-description").attr("value", data.description);

					},
					error: function(requestObj, status, error){
						console.log("req : " + requestObj + " | status : " + status + " | error : " + error);
						window.location.href = "../index.html";
					}
				});
            }
        });
    }
	
	if($('body').is('.account')) {
		
		//Mettre les infos connues
		$.ajax({
			type: "GET",
			url: "../account",
			dataType: "json",
			success: function(data){
				$("#loginspan").text(data.login);
				$("#firstname").attr("value", data.firstname);
				$("#lastname").attr("value", data.lastname);
				$("#email").attr("value", data.email);
				if(data.birthday!=null) $("#birth").attr("value", data.birthday);
				if(data.country!=null) $("#countries").attr("value", data.country);

			},
			error: function(requestObj, status, error){
				console.log("req : " + requestObj + " | status : " + status + " | error : " + error);
				window.location.href = "../index.html";
			}
		});
	}
});
