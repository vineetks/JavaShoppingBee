var loginStatus="Loggedout";
var user;
var role;
$(document).ready(function(){
	checkLoginStatus();
	
    var navigation = $('#navigation');
    var pos = navigation.offset();
    $(window).scroll(function(){
        if($(this).scrollTop() > pos.top){
            $("#navigation").addClass("affix");
            $("#product-nav").addClass("fixed-product-nav");
         } 
        else{
            $("#navigation").removeClass("affix");
            $("#product-nav").removeClass("fixed-product-nav");
        }
    });
    
    $(document).on( "ready", document, function() {
        $(".product-list>li:first-child>a span").removeClass("glyphicon-plus").addClass("glyphicon-minus");
    });
    
    $("#editProfile").click(function(event){
    	event.preventDefault();
 	   	$('.input-disabled').prop("disabled", false); // Element(s) are now enabled.
	});
    
    $(document).on( "click", ".product-list>li>a", function() {
        $(".product-list>li>a").removeClass("active-product-list");
        $(this).addClass("active-product-list");
        var activeTab = $(this).attr('data-active');
        $(".product-sub-list:not("+activeTab+")").slideUp("slow");
        $(".product-list>li>a span").removeClass("glyphicon-minus").addClass("glyphicon-plus");
        $("span", this).removeClass("glyphicon-plus").addClass("glyphicon-minus");
        $(activeTab).slideDown("slow");
        event.preventDefault();
    });
    
    $('body').on({
      mouseenter: function() {
          $(this).children().css("display", "block");
      },
      mouseleave: function() {
          $(this).children().css("display", "none");
      }
    }, '#searchResults a');
    
    $('body').on({
      mouseenter: function() {
          $(".initial-chevron-left").addClass("final-chevron-left");
          $(".initial-chevron-right").addClass("final-chevron-right");
      },
      mouseleave: function() {
          $(".initial-chevron-left").removeClass("final-chevron-left");
          $(".initial-chevron-right").removeClass("final-chevron-right");
      }
    }, '#myCarousel');
    var modal = '<div class="modal fade" id="loginAlert" role="dialog">'+
                    '<div class="modal-dialog">'+
                        '<div class="modal-content">'+
                            '<div class="modal-header modified-modal-header">'+
                                  '<h4 class="modal-title text-center">You are not logged in</h4>'+
                            '</div>'+
                            '<div class="modal-body text-center">'+
                                '<p>You need to login to access the cart</p>'+
                                '<a href="login.html"><button type="button" class="btn btn-primary">Login</button></a>'+
                            '</div>'+
                        '</div>'+
                    '</div>'+
                '</div>';
          
    $("body").append(modal);
    $(".footer").load("footer.html");
});

function checkLoginStatus(){
	var getStatus = $.getJSON("http://localhost:8080/shoppingbee/loginStatus", function(data){});
	$(document).ajaxStop(function(){
    	$(this).unbind("ajaxStop");
    	console.log(getStatus.responseText.split("@@")[1]);
    	if(getStatus.responseText.split("@@")[0]!="anonymousUser"){
    		loginStatus = "LoggedIn";
    		user=getStatus.responseText.split("@@")[1];
    		role=getStatus.responseText.split("@@")[2];
    		updatePage();
    	}else{
    		loginStatus = "LoggedOut";
    		updatePage();
    	}
    });
}

function updatePage(){
	if(loginStatus == "LoggedIn"){
		$(".glyphicon-cart-modified, .go-to-cart").css("color", "#646").click( function(){
			location.assign("cart.html");
		});
		updateCartContent();

		$("#loggedOut").css("display", "none");
		$("#greetingText").html(" " + user + " <span class='caret'></span>");
		if(role=="ADMIN"){
			$("#adminPage").html('<a href="admin.html"><span class="glyphicon glyphicon-hand-right"></span> Go To Admin</a>');
		}
		$('body').on({
            mouseenter: function() {
                $(".glyphicon-cart-modified").css("background","white");
                $(".hover-cart").addClass("hover-cart-displayed");
            },
            mouseleave: function() {
                $(".glyphicon-cart-modified").css("background","none");
                $(".hover-cart").removeClass("hover-cart-displayed");
        }   }, '.glyphicon-cart-modified, .hover-cart');
		
		$(document).on( "click", '.add-to-cart', function() {
			var keyItem = $(this).attr("data-productID");
			addToCart(keyItem);
			event.preventDefault();
		});
        $(document).on( "click", '.glyphicon-heart-empty, .add-to-wishlist', function() {
			$(this).removeClass("glyphicon-heart-empty").addClass("glyphicon-heart");
            addToWishlist($(this).attr("data-productId"));
		});
        $(document).on( "click", '.glyphicon-heart, .move-to-cart', function() {
			$(this).removeClass("glyphicon-heart").addClass("glyphicon-heart-empty");
            removeFromWishlist($(this).attr("data-productId"));
		});
	}
	else if(loginStatus == "LoggedOut"){
		$(document).on("click", ".glyphicon-cart-modified, .add-to-cart, .go-to-cart, .add-to-wishlist", function(){
			event.preventDefault();
            $("#loginAlert").modal();
        });
        $(".glyphicon-heart-empty").remove();
        $("#loggedIn").css("display", "none");
	}
}