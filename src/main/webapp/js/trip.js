$(document).ready( function() {
	var i = 0;
	function addFriend() {
		$('#add').attr('id','changing');
		$('#changing').attr('src', 'minus.png');
		$('#changing').unbind(); //On enlève tous les listeners, et en particulier celui de addFriend qui persiste.
		$('#changing').on('click', function() {
			$(this).parent().parent().fadeOut(700, function() { $(this).remove(); })
		});
		
		$('<tr><td><input type="email" name="friend"></td> <td><img id="add" src="plus.png"></td></tr>').fadeIn(1000).insertAfter($('#changing').parent().parent());
		$('#changing').removeAttr('id');
		$('#add').on('click', function() { addFriend(); });
	}

	$('#add').on('click', function() {addFriend();});
});
