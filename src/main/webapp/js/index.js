

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

    load_bar();
});
