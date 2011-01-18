$(function() {
	$(".userDelLink").click(function(){
        var userLogin = $(".login", $(this).parent().parent()).text();

        if( confirm(deleteConfirm +" '"+ userLogin +"'?") ) {
            var userId = $(".id", $(this).parent().parent()).text();

            $.ajax({
                type: "GET",
                url: './user_'+ userId +'/delete.html',
                dataType: "json",
                success: function(data) {
                    if( data.deleted == true ) {
                    	var cell = $("td:eq(7)", "#user_"+ userId);
                        cell.addClass('upd_ok').removeClass('upd_ok', 5000);
                        $.pnotify({
    						pnotify_title: 'User delete successful',
    						pnotify_text: 'User deleted. You can see that field "D" has changed.',
							pnotify_stack: {"dir1": "down", "dir2": "right", "push": "top"}							
    					});

                        // Do something to clean user's data and photos
                    }
                    else {
                    	var cell = $("td:eq(7)", "#user_"+ userId);
                        cell.addClass('upd_err').removeClass('upd_err', 5000);
                        $.pnotify({
    						pnotify_title: 'User delete error!',
    						pnotify_text: 'We can\'t delete user so... try to do in later!.',
    						pnotify_type: 'error'
    					});
                    	// Out message to show that photo was not moved
                    }
                },
                error: function(xhr, status, trown) {
                	if( status != 'success' && xhr.status == 200 ) {
                        alert('Status is not success!');
                    }
                    else if( xhr.status != 200 ) {
                        alert('Server return status '+ xhr.status +'!');
                    }
	            }
            });
        }
    });
});