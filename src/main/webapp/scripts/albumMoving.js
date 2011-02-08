$(function() {
   	$(".photo a img").draggable({
        stack: ".photo",
        revert: true,
        cursor: 'move'
    });
    $(".photo_frame").disableSelection();
    
    $("#albumId").change(function(){
        var albumId = $("#albumId option:selected").val();
        if( albumId != 0 && $("#album_"+ albumId).get() == '' ) {
        	$(this).after('<div id="album_'+ albumId +'" class="targetAlbum" style="display:none;">[<a href="" class="targetAlbumClose">&#160X&#160;</a>]&#160;'+ $("#albumId option:selected").text() +'</div>');
            $("#album_"+ albumId).show("blind", null, 'fast');
            if( allowMultiselect )
            	$("#album_"+ albumId).append('<input type="button" id="moveToAlbum_'+ albumId +'" value="Move here" '+ (selectedCount > 0 ? '' : 'disabled="disabled"') +'>');
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
            	var re = /^album_(\d+)$/i;
            	var str = $(this).attr("id");
            	var dstAlbumId = str.match(re)[1];

            	$(ui.draggable).draggable( "option", "revert", false );
                $(ui.draggable).draggable("disable");

                var re = /^image_(\d+)$/i;
                var str = $(ui.draggable).attr("id");
                if( str != undefined ) {
                    var photoId = str.match(re)[1];

	                $.ajax({
	                    type: "POST",
	                    data: "albumId="+ dstAlbumId,
	                    url: './photo_'+ photoId +'/move.html',
	                    dataType: "json",
	                    success: function(data) {
	                    	if( data.moved == true ) {
	                    		var options = {};
	                    		$(ui.draggable).effect('fade', options, 'slow');
                                
                                options = { to: '#album_'+ dstAlbumId, className: "ui-effects-transfer" };
                                $("#photo_"+ photoId).effect('transfer', options, 'slow');
                                $("#photo_"+ photoId).effect('fade', options, 'slow');
                                
                                if( currentAlbumId == 0 ){
                                	unallocatedPhotosCount--;
                                	$("#unallocatedPhotosCount").html(unallocatedPhotosCount);
                                }
	                    	}
	                        else {
	                            $(ui.draggable)
	                            	.draggable("enable")
	                            	.animate({top: '0px', left: '0px'}, 'slow')
	                        		.draggable( "option", "revert", true );
	                        }
	                    },
	                    error: function(xhr, status, trown) {
	                        if( status != 'success' && xhr.status == 200 ) {
                                $(ui.draggable)
                                	.draggable("enable")
                                	.animate({top: '0px', left: '0px'}, 'slow')
                                    .draggable( "option", "revert", true );
	                            alert('Status is not success!');
	                        }
	                        else if( xhr.status != 200 ) {
                                $(ui.draggable)
                                	.draggable("enable")
                                	.animate({top: '0px', left: '0px'}, 'slow')
                                	.draggable( "option", "revert", true );
	                            alert('Server return status '+ xhr.status +'!');
	                        }
	                    }
	                });
                }
            }
        });
    });

    $("#allowMultipleSelection").change(function(){
    	if( $('#allowMultipleSelection:checked').val() == "on" ) {
    	    $(".photo_icons").each(function(){
    	    	var photoId = $(this).parent().find(".photo_id").val();
    	    	$(this).append('<input type="checkbox" id="select_'+ photoId +'" name="select_'+ photoId +'">');
    	    });
    	    
    	    $("input[id^=select]").change(function(){
    	    	selectedCount = $("input[id^=select]:checked").length;
    	    	if( selectedCount > 0 ) {
    	    		$("input[id^=moveToAlbum]").attr("disabled", "");
    	    	}
    	    	else {
    	    		$("input[id^=moveToAlbum]").attr("disabled", "disabled");
    	    	}
    	    });
    	    allowMultiselect = true;
    	    
    	    $("div[id^=album_]").each(function(){
            	var re = /^album_(\d+)$/i;
            	var str = $(this).attr("id");
            	var albumId = str.match(re)[1];
    	    	$(this).append('<input type="button" id="moveToAlbum_'+ albumId +'" value="Move here" '+ (selectedCount > 0 ? '' : 'disabled="disabled"') +'>');
    	    });
    	}
    	else {
    		$("input[id^=select]", ".photo_icons").remove();
    		allowMultiselect = false;
    		selectedCount    = 0;
    		$("input[id^=moveToAlbum]").remove();
    	}
    });

    $(".photoDelLink").click(function(){
        var photoTitle = $( "td.photo > a", $(this).parent().parent()).attr('title');
        if( confirm(deleteConfirm +" '"+ photoTitle +"'?") ) {
            var re = /^photo_(\d+)$/i;
            var str = $(this).parent().parent().attr("id");
            var photoId = str.match(re)[1];

            $.ajax({
                type: "GET",
                url: './photo_'+ photoId +'/delete.html',
                dataType: "json",
                success: function(data) {
                    if( data.deleted == true ) {
                        options = { to: '#album_'+ albumId, className: "ui-effects-transfer" };
                        $("#photo_"+ photoId).effect('fade', options, 'slow');

                        if( currentAlbumId == 0 ){
                        	unallocatedPhotosCount--;
                        	$("#unallocatedPhotosCount").html(unallocatedPhotosCount);
                        }
                    }
                    else {
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
