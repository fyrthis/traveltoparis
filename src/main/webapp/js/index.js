function load_bar(){
    console.log("bar function got called");
    $.ajax({
        type: "GET",
        url: "./sign-in",
        dataType: "json",
        success: function (data) {
            console.log(data);
            if (data.has_session == true){
                $("#first_div").css("display", "none");
                $("#second_div").css("display", "block");
            }
            else{
                $("#first_div").css("display", "none");
                $("#second_div").css("display", "block");
            }
        },
        error: function (requestObj, status, error) {
            console.log(requestObj + " : " + status + " : " + error);
            $("#first_div").css("display", "none");
            $("#second_div").css("display", "block");
        },
        async: false
    });}

load_bar();

$(document).ready(function(){
    // Defining a function to set size for #hero
    function fullscreen(){
        $('#hero').css({
            width: $(window).width(),
            height: $(window).height()
        });
    }

    fullscreen();

    // Run the function in case of window resize
    $(window).resize(function() {
        fullscreen();
    });

    $("#form-sign-in").submit(function(event){
        event.preventDefault();
        console.log("Got Called");
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
                        //location.reload();
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
});
