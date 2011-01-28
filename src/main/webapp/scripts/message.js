function addMessage(var str) {
	$(".messageBoard").addClass("on");
	$(".messageBoard").html( $(".messageBoard").html() +"<br />\n"+ str );
}