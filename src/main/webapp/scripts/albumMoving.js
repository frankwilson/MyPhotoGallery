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
                addMoveButton(albumId, $("#album_"+ albumId));
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
                var dstAlbumId = $(this).attr("id").match(/^album_(\d+)$/i)[1];

                $(ui.draggable).draggable( "option", "revert", false );
                $(ui.draggable).draggable("disable");

                re = /^image_(\d+)$/i;
                str = $(ui.draggable).attr("id");
                if( str != undefined ) {
                    var photoId = str.match(re)[1];
                    movePhoto(photoId, dstAlbumId);
                }
            }
        });
    });

    $("#allowMultipleSelection").change(function(){
        if( $('#allowMultipleSelection').is(':checked') ) {
            $(".photo_icons").each(function(){
                var photoId = $(this).parent().find(".photo_id").val();
                $(this).append('<input type="checkbox" id="select_'+ photoId +'" name="select_'+ photoId +'">');
                $(this).parent().find("#image_"+ photoId).parent().attr("onClick", "return false;");
                $(this).parent().click(function(){
                    var checkbox = $(this).find("#select_"+ $(this).find(".photo_id").val());
                    checkbox.attr('checked', (checkbox.is(':checked') ? false : true));
                    checkSelectedPhotos();
                });
            });

            $("input[id^=select_]").change(checkSelectedPhotos());

            allowMultiselect = true;
            $("#selectBlock").show();
            
            $("div[id^=album_]").each(function(){
                var albumId = $(this).attr("id").match(/^album_(\d+)$/i)[1];
                addMoveButton(albumId, $(this));
            });
        }
        else {
            $("input[id^=select_]", ".photo_icons").remove();
            allowMultiselect = false;
            selectedCount    = 0;
            $("input[id^=moveToAlbum]").remove();
            $("img[id^=image_]").parent().attr("onClick", "");
            $("#selectBlock").hide();
        }
    });

    $(".photoDelLink").click(function(){
        var photoTitle = $( "td.photo > a", $(this).parent().parent()).attr('title');
        if( confirm(deleteConfirm +" '"+ photoTitle +"'?") ) {
            var str = $(this).parent().parent().attr("id");
            var photoId = str.match(/^photo_(\d+)$/i)[1];

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

    function movePhoto(photoId, dstAlbumId) {
        var movingPhoto = $("#photo_"+ photoId);
        var movingImage = movingPhoto.find("#image_"+ photoId);

        $.ajax({
            type: "POST",
            data: "albumId="+ dstAlbumId,
            url: './photo_'+ photoId +'/move.html',
            dataType: "json",
            success: function(data) {
                if( data.moved == true ) {
                    var options = {};
                    movingImage.effect('fade', options, 'slow');
                    
                    options = { to: '#album_'+ dstAlbumId, className: "ui-effects-transfer" };
                    movingPhoto.effect('transfer', options, 'slow');
                    movingPhoto.effect('fade', options, 'slow');
                    
                    if( currentAlbumId == 0 ){
                        unallocatedPhotosCount--;
                        $("#unallocatedPhotosCount").html(unallocatedPhotosCount);
                    }
                }
                else {
                    movingImage
                        .draggable("enable")
                        .animate({top: '0px', left: '0px'}, 'slow')
                        .draggable( "option", "revert", true );
                }
            },
            error: function(xhr, status, trown) {
                if( status != 'success' && xhr.status == 200 ) {
                    movingImage
                        .draggable("enable")
                        .animate({top: '0px', left: '0px'}, 'slow')
                        .draggable( "option", "revert", true );
                    alert('Status is not success!');
                }
                else if( xhr.status != 200 ) {
                    movingImage
                        .draggable("enable")
                        .animate({top: '0px', left: '0px'}, 'slow')
                        .draggable( "option", "revert", true );
                    alert('Server return status '+ xhr.status +'!');
                }
            }
        });
    }
    
    function addMoveButton(albumId, block) {
        block.append('<input type="button" class="moveButton" id="moveToAlbum_'+ albumId +'" value="'+ moveHereTitle +'" '+ (selectedCount > 0 ? '' : 'disabled="disabled"') +'>');

        $(".moveButton").click(function(){
            var dstAlbumId = $(this).attr("id").match(/^moveToAlbum_(\d+)$/i)[1];

            $("input[id^=select_]:checked").each(function(){
                var photoId = $(this).parent().parent().find(".photo_id").val();
                movePhoto(photoId, dstAlbumId);
            })
        });
    }

    function checkSelectedPhotos() {
        selectedCount = $("input[id^=select_]:checked").length;

        if( selectedCount > 0 ) {
            $("input[id^=moveToAlbum_]").attr("disabled", "");
        }
        else {
            $("input[id^=moveToAlbum_]").attr("disabled", "disabled");
        }
    }

    $(".album_microtables").hover(
        function() { $(this).addClass('hover'); }, 
        function() { $(this).removeClass('hover'); }
    );

    $("input#selectAllPhotos").click(function() {
        $("input[id^=select_]").attr('checked', true);
        checkSelectedPhotos();
    });

    $("input#invertSelection").click(function() {
        $("input[id^=select_]").each(function(){
            $(this).attr('checked', ($(this).is(':checked') ? false : true));
        });
        checkSelectedPhotos();
    });
});
