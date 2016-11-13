

$(document).ready(function(){
    //---> Placer correctement le bandeau du index.html au chargement, et pendant les resize.

    var body = $('body');

    function fullscreen(){
        $('#hero').css({
            width: $(window).width(),
            height: $(window).height()
        });
    }
    if(body.is('.index')) { fullscreen(); }
    $(window).resize(function() {
        if($('body').is('.index')) { fullscreen(); }
    });

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
        });}
    if(body.is('.index') || body.is('.about') || body.is('.contact') || body.is('.faqs')|| body.is('.dev')|| body.is('.terms')) {	load_bar();	}

    //---> Placer le footer en pied de page
    $('body:last-child').append("<footer class=\"footer\"><div class=\"container text-center\"><div class=\"footer_top\"><div class=\"col-md-6\"><h5>Support</h5><ul class=\"nav nav-pills nav-stacked\"><li><a href=\"/help\">Help &amp; FAQs</a></li><li><a href=\"/legal\">Privacy &amp; Terms</a></li><li><a href=\"/devs\">Developers</a></li></ul></div><div class=\"col-md-6\"><h5>Travel to Paris</h5><ul class=\"nav nav-pills nav-stacked\"><li><a href=\"/about\">About us</a></li><li><a href=\"/contact\">Contact us</a></li></ul></div></div><div class=\"col-md-12\"><p>Copyright &copy; 2016. All rights reserved.</p></div></div></footer>");

    if(body.is('.signup')) {
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

//		--> Vérifier le match des deux passwords indiqués.
        function checkpw(input) {
            if (input.value != document.getElementById('password').value) {
                input.setCustomValidity('Password Must be Matching.'); //Check pattern TODO : En fonction de la langue ?
            } else {
                input.setCustomValidity('');
            }
        }
    }

    //---> Ajouter les champs pour saisir des adresses mails.
    var i = 0;
    function addFriend() {
        var add = $('#add');
        var changing = $('#changing');
        add.attr('id','changing');
        changing.attr('src', 'minus.png');
        changing.unbind(); //On enlève tous les listeners, et en particulier celui de addFriend qui persiste.
        changing.on('click', function() {
            $(this).parent().parent().fadeOut(700, function() { $(this).remove(); })
        });

        $('<tr><td><input type="email" name="friend"></td> <td><img id="add" src="plus.png"></td></tr>').fadeIn(1000).insertAfter(changing.parent().parent());
        changing.removeAttr('id');
        add.on('click', function() { addFriend(); });
    }
    $('#add').on('click', function() {addFriend();});

//	-->	Script pour la gestion des tabs TODO : ajouter la classe .tab
    $(".btn-pref .btn.tab").on('click', function () {
        $(".btn-pref .btn.tab").removeClass("btn-primary").addClass("btn-default");
        $(this).removeClass("btn-default").addClass("btn-primary");
    });

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
    if(body.is('.trips')) {
        $.get("../get-my-trips", function(data) {
            console.log(data);
            //Pour chaque trip, on append
            console.log(data.trips.list[0]);
            for(var i = 0; i < data.trips.size; i++) {
                var trip = data.trips.list[i].trip;
                $(".row.trips").append('<div class="col-lg-12 col-sm-12 col-xs-12"><divclass="brdr bgc-fff pad-10 box-shad btm-mrg-20 "><div class="media"><a class="pull-left" href="trip.html?id='+trip.id+'"><img class="img-responsive list" src="http://lorempixel.com/100/100/city/"></a><div class="media-body fnt-smaller"><a href="trip.html?id='+trip.id+'"></a><h4 class="media-heading"><a href="trip.html?id='+trip.id+'">'+trip.name+'</a></h4><ul class="list-inline mrg-0 btm-mrg-10 clr-535353"><li>From '+trip.begins+'</li><li style="list-style: none">|</li><li>To '+trip.ends+'</li></ul><p class="hidden-xs"> ?? participants</p><span class="fnt-smaller fnt-lighter fnt-arial">'+trip.description+'</span></div></div></div></div>');
            }
        });
    }

    // --> Si la page est trip
    if(body.is('.trip')) {
        var trip_id = getUrlParameter("id");
        if(trip_id == undefined){window.location.href = "../index.html";}
        var overview_tab = $("#overview");
        // si la tab est selectionnée
        if(overview_tab.hasClass("btn-primary")){
            $.ajax({
                type: "GET",
                url: "../overview",
                dataType: "json",
                data: {id: trip_id},
                success: function(data){
                    $("#trip_name_banner").text(data.name);
                    $("#panel_trip_name").text(data.name);
                    $("#description_trip").text("Description : " + data.description);
                    $("#nb_participants").text(data.participants);
                    $("#from_trip").text(data.begins);
                    $("#to_trip").text(data.ends);
                },
                error: function(requestObj, status, error){
                    console.log("req : " + requestObj + " | status : " + status + " | error : " + error);
                    window.location.href = "../index.html";
                }
            });
        }
    }


});
