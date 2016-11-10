$(document).ready( function() {

// Container qui change en fonction du bouton sur lequel on appuie !
//EN MAJUSCULE les champs à changer !

	document.getElementById("overview").addEventListener("click", function(){
	    document.getElementById("container").innerHTML = "Attente response MAIN PAGE"; //Ici, on met un loading spinner au cas où la requète prend du temps
	    $.getJSON("MAINSERVLET", function(data) {
	    	//Ici on n'oubliera pas de cacher le spin loader
            $("#container").html('<p><b>Nom</b> : ' + data.Name + '</p>');
        });
	});

	document.getElementById("events").addEventListener("click", function(){
	    document.getElementById("container").innerHTML = "Attente response EVENTS PAGE";
	    $.get("EVENTSSERVLET", function(response) {
            $("#container").text(response);
        });
	});

	document.getElementById("calendar").addEventListener("click", function(){
	    document.getElementById("container").innerHTML = "Attente response  CALENDAR PAGE";
	    $.get("CALENDARSERVLET", function(response) {
            $("#container").text(response);
        });
	});

	document.getElementById("chat").addEventListener("click", function(){
	    document.getElementById("container").innerHTML = "Attente response  CHAT PAGE";
	    $.get("CHATSERVLET", function(response) {
            $("#container").text(response);
        });
	});

	document.getElementById("invite").addEventListener("click", function(){
	    document.getElementById("container").innerHTML = "Attente response INVITE PAGE";
	    $.get("INVITESERVLET", function(response) {
            $("#container").text(response);
        });
	});

	document.getElementById("settings").addEventListener("click", function(){ //Ici, attention aux failles de sécurité ? 
	    document.getElementById("container").innerHTML = "Attente response SETTINGS PAGE";
	    $.get("SETTINGSSERVLET", function(response) {
            $("#container").text(response);
        });
	});

});