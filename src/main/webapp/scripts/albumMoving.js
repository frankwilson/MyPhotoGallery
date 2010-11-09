$(function() {
   	$(".photo a img").draggable({
        stack: ".photo_frame",
        revert: true
    });
    $(".photo_frame").disableSelection();
    
    $("#albumId").change(function(){
        var albumId = $("#albumId option:selected").val();
        if( albumId != 0 && $("#album_"+ albumId).get() == '' ) {
        	$(this).after('<div id="album_'+ albumId +'" class="targetAlbum" style="display:none;">[<a href="" class="targetAlbumClose">&#160X&#160;</a>]&#160;'+ $("#albumId option:selected").text() +'</div>');
            $("#album_"+ albumId).show("blind", null, 'fast');
        }
        else if( $("#album_"+ albumId).get() != '' && $("#album_"+ albumId).css("display") == "none" ) {
            $("#album_"+ albumId).show("blind", null, 'fast');
        }
        
        $(".targetAlbumClose").click(function() {
            $(this).parent().hide("blind", null, "fast");
            return false;
        });

        $(".targetAlbum").droppable({
            accept: ".photo a img",
            hoverClass: "ui-state-active",
            drop: function( event, ui ) {
            	$(ui.draggable).draggable( "option", "revert", false );
                $(ui.draggable).draggable("disable");
                var re = /^image_(\d+)$/i;
                var str = $(ui.draggable).attr("id");
                if( str != undefined ) {
                    var photoId = str.match( re )[1];

	                $.ajax({
	                    type: "POST",
	                    data: "albumId="+ albumId,
	                    url: './photo_'+ photoId +'/move.html',
	                    dataType: "json",
	                    success: function(data) {
	                    	if( data.moved == true ) {
	                    		var options = {};
	                    		$(ui.draggable).effect('fade', options, 'slow');
                                
                                options = { to: '#album_'+ albumId, className: "ui-effects-transfer" };
                                $("#photo_"+ photoId).effect('transfer', options, 'slow');
                                $("#photo_"+ photoId).effect('fade', options, 'slow');
	                    	}
	                        else {
	                            $(ui.draggable).draggable("enable");
	                            $(ui.draggable).animate({top: '0px', left: '0px'}, 'slow');
	                        	$(ui.draggable).draggable( "option", "revert", true );
	                        }
	                    },
	                    error: function(xhr, status, trown) {
	                        if( status != 'success' && xhr.status == 200 ) {
                                $(ui.draggable).draggable("enable");
                                $(ui.draggable).animate({top: '0px', left: '0px'}, 'slow');
                                $(ui.draggable).draggable( "option", "revert", true );
	                            alert('Status is not success!');
	                        }
	                        else if( xhr.status != 200 ) {
                                $(ui.draggable).draggable("enable");
                                $(ui.draggable).animate({top: '0px', left: '0px'}, 'slow');
                                $(ui.draggable).draggable( "option", "revert", true );
	                            alert('Server return status '+ xhr.status +'!');
	                        }
	                    }
	                });
                }
            }
        });
    });
});
