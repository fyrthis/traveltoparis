jQuery(document).ready(function($){
    // Defining a function to set size for #hero
    function fullscreen(){
        jQuery('#hero').css({
            width: jQuery(window).width(),
            height: jQuery(window).height()
        });
    }

    fullscreen();

    // Run the function in case of window resize
    jQuery(window).resize(function() {
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
                        $("#sign-in-res").empty().append("<p>Success</p>");
                    }
                    else{$("#sign-in-res").empty().append("<p>Already signed in</p>")}
                }
                else{$("#sign-in-res").empty().append("<p>Failed</p>")}
            },
            error: function(requestObj, status, error){
                $("#sign-in-res").empty().append("<p>Failed : " + status + "</p>");
            }
        });
    });
});
