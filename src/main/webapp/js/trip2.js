$(document).ready(function() {

//	Script pour la gestion des tabs
	$(".btn-pref .btn").click(function () {
		$(".btn-pref .btn").removeClass("btn-primary").addClass("btn-default");
		$(this).removeClass("btn-default").addClass("btn-primary");   
	});

//	Script pour l'ajout d'emails pour les invitations
	var i = 0;
	function addFriend() {
		$('#add').attr('id','changing');
		$('#changing').attr('class', 'glyphicon glyphicon-minus');
		$('#changing').unbind(); //On enlève tous les listeners, et en particulier celui de addFriend qui persiste.
		$('#changing').on('click', function() {
			$(this).parent().parent().fadeOut(700, function() { $(this).remove(); })
		});

		$('<tr><td><input type="email" name="friend" class="form-control"></td> <td><i id="add" class="glyphicon glyphicon-plus"></i></td></tr>').fadeIn(1000).insertAfter($('#changing').parent().parent());
		$('#changing').removeAttr('id');
		$('#add').on('click', function() { addFriend(); });
	}

	$('#add').on('click', function() {addFriend();});

	
});

/*
<div class="container text-center">
	<span class="glyphicon glyphicon-refresh glyphicon-refresh-animate"></span>
  <p>updating information...</p>
</div>
 */