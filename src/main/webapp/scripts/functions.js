function toddleLeftPanel() {
	if( $(".left_panel").css("display") == "none" ) {
		$(".content").removeClass('full_width')
	    $(".left_panel").animate({ height: 'show' });
	    $(".left_panel_show > a").html( "&#160;hide left panel&#160;" );
	}
	else {
	    $(".left_panel").animate({ height: 'hide' }).queue(function() {
	        $(".content").addClass('full_width');
	        $(this).dequeue();
	    });
	    $(".left_panel_show > a").html( "&#160;show left panel&#160;" );
	}
}
