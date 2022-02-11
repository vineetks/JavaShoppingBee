$(document).ready(function(){
    $(".modified-container").css("height", $(window).height());
    
    var URLVariable = decodeURIComponent(window.location.search.substring(1));
    var query = URLVariable.split('=');
    if (query[1] == "badCredentials") {
    	$("#password").css("border-color","red").next().css("display","block");;
    	$("#username").css("border-color","red").next().css("display","block");
    }
    
    $("#loginSubmit").click( function(){
    	$("#loginForm").submit();
    });
    $("#signupSubmit").click( function(){
    	var submitForm = document.getElementsByTagName('form')[0];
    	if(submitForm.checkValidity()) {
    		if($("#password").val()==$("#confirmPassword").val()){
    			submitForm.submit();
    		}else{
    			validation($("#confirmPassword"), false);
    			event.preventDefault();
    		}
    	}else {
    		validation($("#email"), false);
    	}
    });
    $("#signupForm #email").blur(function(){
    	var username = $(this).val();
    	if(username!=""){
    		$.getJSON("http://localhost:8080/shoppingbee/signup/"+username, function(data){
        		if(data.availability=="available"){
        			validation($("#email"), true);
        		}else{
        			validation($("#email"), false);
        		}
        	});
    	}
    });
});

function validation(element, status){
	if(status==true){
		$(element).css("border-color","green").next()
		.removeClass("glyphicon-exclamation-sign")
		.addClass("glyphicon-ok-circle")
		.css({"display":"block", "color":"green"});
	}else{
		$(element).css("border-color","red").next()
		.removeClass("glyphicon-ok-circle")
		.addClass("glyphicon-exclamation-sign")
		.css({"display":"block", "color":"red"});
	}
}