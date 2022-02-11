$(document).ready(function(){  
    $(document).on( "click", '.remove-from-cart', function() {
        var keyItem = $(this).attr("data-cartItemId");
        removeFromCart(keyItem);
        event.preventDefault();
    });  
    
    $(document).on( "click", '.glyphicon-minus-sign', function() {
        setQuantity("minus", this);
    }); 
    $(document).on( "click", '.glyphicon-plus-sign', function() {
        setQuantity("plus", this);
    }); 
    $(document).on( "keyup", '.quantity', function() {
        setQuantity("editable", this);
    });
    $("#checkoutButton").click(function(){
    	$(".review-order-content").slideUp("slow");
    	$("#selectAddress .panel-heading").addClass("active-panel-heading");
    	getDeliveryAddresses();
    });
    
    $(document).on( "click", '#paymentButton', function() {
    	$(".select-address-content").slideUp("slow");
    	$("#payment .panel-heading").addClass("active-panel-heading");
    	$(".payment-content").slideDown("slow");
    });
    $("#orderButton").click(function(){
    	$("#orderStatus .panel-heading").addClass("active-panel-heading");
    	$(".payment-content").slideUp("slow");
    	getOrderStatus();
    });
    $(".payment-type").click(function(){
    	$(".payment-type").removeClass("active-list-item");
    	$(this).addClass("active-list-item");
    	$("#paymentType").html("Pay Using "+$(this).html());
    	event.preventDefault();
    });
    $(document).on("click", ".new-entity", function(){
        $(".newAddressDiv").load("address.html", function(){
        	$("#newAddressModal").modal();
        }); 
    });
    $(document).on("click", "#addressSubmit", function(){
    	var category= getUrlPath();
    	var submitForm = document.getElementById('newAddressForm');
    	if(submitForm.checkValidity()){
    		event.preventDefault();
    		addNewAddress(category);
    	}
    });
});  

function updateCartContent(){
	$.getJSON("http://localhost:8080/shoppingbee/current/cart", function(cartItem){
		var string = "";
        if(cartItem.length!=0){
            var subTotal = 0, total = 0, taxes = 0;
            $.each(cartItem, function(i){ 
            	var data = cartItem[i].product;
                string += '<tr data-cartItemId="'+ cartItem[i].cartItemId +'">'+
                            '<td><a href="productdetail.html?productID=' + data.productId + '">'+
                                '<img src="../assets/images/' + data.imageSRC + '" class="img-thumbnail" alt="' + data.productName + '" style="width:100px">'+
                            '</a></td>'+
                            '<td>'+
                                '<h4><a href="productdetail.html?productID=' + data.productId + '">' + data.productName + '</a></h4>'+
                                '<div> Price: Rs.' + data.price + '</div>'+
                            '</td>'+
                            '<td>'+
                                '<span class="glyphicon glyphicon-minus-sign cart-glyphicon"></span>'+
                                '<span class="quantity" data-price="'+ data.price +'" contenteditable="true">'+cartItem[i].quantity+'</span>'+
                                '<span class="glyphicon glyphicon-plus-sign cart-glyphicon"></span>'+
                            '</td>'+
                            '<td>Rs. <span class="total-price">'+ data.price +'</span></td>'+
                            '<td><button type="button" class="btn btn-danger remove-from-cart" data-cartItemId="'+ cartItem[i].cartItemId +'">Remove<span class="glyphicon glyphicon glyphicon-remove cart-glyphicon"></span></button></td>'+
                        '</tr>';
                subTotal += parseInt((data.price)*(cartItem[i].quantity));
            });
            taxes = Math.round(subTotal * 14) / 100;
            total = taxes + subTotal;
            string += '<tr>'+
                        '<td colspan="2"><input class="form-control" type="text" id="applyCoupon" placeholder="I have a discount coupon">'+
                        '<button type="button" class="btn btn-success" id="applyCouponBtn">Apply</td><td></td>'+
                        '<td colspan="2">'+
                            '<div class="cartTotal"><h5><b>Sub-Total :</b></h5><h5><b>Taxes :</b></h5><h5><b>Total : </b></h5></div>'+
                            '<div class="cartTotalValues"><h5 data-total="'+subTotal+'">Rs. '+subTotal +'</h5><h5>Rs. '+ taxes +'</h5><h5>Rs. '+ total +'</h5></div>'+
                        '</td>'+
                      '</tr>';
        } else if(cartItem.length==0){
            string += '<tr><td colspan="5">'+
                                '<div class="alert alert-danger">'+
                                        '<a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>'+
                                        '<strong> No items in cart!</strong>'+
                                '</div>'+
                      '</td></tr>';
            $('#checkoutButton').prop("disabled", true);
        }
        updateCartIndex(cartItem.length);
        $(".cart-content").html(string);
	});
}

function addToCart(productID){
	$.post("http://localhost:8080/shoppingbee/current/addToCart/"+productID, function(data, statusText, xhr) {
		if(xhr.status==201){
			var string = 	'<div class="alert alert-success">'+
								'<a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>'+
								'<strong>"'+ xhr.responseText +'" successfully added to cart </strong>'+
							'</div>';
			
		}else if(xhr.status==202){
			var string = 	'<div class="alert alert-warning">'+
								'<a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>'+
								'<strong>"'+ xhr.responseText +'" already in the cart </strong>'+
							'</div>';
		}
		$(".addCartMessage").html(string);
		updateCartContent();
	});
}

function removeFromCart(productID){  
	$.getJSON("http://localhost:8080/shoppingbee/current/removeFromCart/"+productID, function(data){
		console.log(data);
		updateCartContent();
	});
	//SHOW REMOVED ITEM
}

function updateCartIndex(count){
    $("#cart-counter").css("display","block").html(count);
}

function setQuantity(sign, element){
	var lowerLimitError = '<div class="alert alert-danger row"><a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a><strong> Quantity can not be less than ONE ! </strong></div>'
	var upperLimitError = '<div class="alert alert-danger row"><a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a><strong> Quantity can not be greater than 30 ! </strong></div>'
	var quantity;
	if(sign=="plus"){
        quantity = $(element).prev().html();
        var price = $(element).prev().attr("data-price");
        if (quantity==30){ 
        	$("#cartError").html(upperLimitError);
        }
	    else{ 
	    	$("#cartError").html("");
        	quantity++;
            $(element).prev().html(quantity);
            var totalPrice = quantity*price;
            $(element).parent().next().children("span").html(totalPrice);
            setTotalAmount();
	    }
    }else if(sign=="minus"){
        quantity = $(element).next().html();
        var price = $(element).next().attr("data-price");
        if (quantity<=1){ 
        	$(element).next().html(1);
            $("#cartError").html(lowerLimitError);
        }else{
            quantity--;
            $(element).next().html(quantity);
            var totalPrice = quantity*price;
            $(element).parent().next().children("span").html(totalPrice);
            setTotalAmount();
        }
    }else if(sign=="editable"){
    	quantity = $(element).html();
    	var price = $(element).attr("data-price");
    	if(Math.floor(quantity) != quantity){
    		quantity = Math.floor(quantity);
    		$(element).html(quantity);
    	}
    	if(quantity<1 || !($.isNumeric(quantity))){
    		quantity = 1;
    		$(element).html(quantity);
        	$("#cartError").html(lowerLimitError);
    	}else if(quantity>30){
    		quantity = 30;
    		$(element).html(quantity);
        	$("#cartError").html(upperLimitError);
    	}else{
    		$("#cartError").html("");
    	}
    	var totalPrice = quantity*price;
        $(element).parent().next().children("span").html(totalPrice);
        setTotalAmount();
    }
	var cartItemId = $(element).parents("tr").attr("data-cartItemId");
	$.getJSON("http://localhost:8080/shoppingbee/current/changeQuantity/"+cartItemId+"/"+quantity, function(data){
	});
}

function setTotalAmount(){
	var subTotal = 0;
	$('.total-price').each(function(i) {
		subTotal+= parseInt($(this).html());
	});
    var taxes = Math.round(subTotal * 14) / 100;
    var total = Math.round((taxes + subTotal) * 100) / 100;
    $(".cartTotalValues").html('<h5 data-total="'+subTotal+'">Rs. '+subTotal +'</h5><h5>Rs. '+ taxes +'</h5><h5>Rs. '+ total +'</h5>');
}

function getDeliveryAddresses(){
	var string="";
	$.getJSON("http://localhost:8080/shoppingbee/current/getAddressBook", function(data){
		string+='<div class="panel-body">';
		$.each(data, function(i){
			string+='<div class="col-sm-4"><div class="panel panel-default">'+
  						'<div class="panel-heading" id="addressType">'+
  							'<b>'+ data[i].addressType +'</b>'+
  							'<input type="radio" name="addressRadio" value="'+ data[i].addressId +'" style="float:right;" checked="checked" required>'+
  						'</div>'+
  						'<div class="panel-body panel-body-address">'+
  							'<div>Name: '+ data[i].name +'</div>'+
  							'<div>Street Name: '+ data[i].street +'</div>'+
  							'<div>City: '+ data[i].city +'</div>'+
  							'<div>State: '+ data[i].state +'</div>'+
  							'<div>PIN code: '+ data[i].pincode +'</div>'+
  							'<div>LandMark: '+ data[i].landmark +'</div>'+
  							'<div>Contact No:'+ data[i].contactNo +'</div>'+
  						'</div>'+
  					'</div></div>';
		});
		string+='<div class="col-sm-4"><div class="panel panel-default">'+
					'<div class="panel-heading" id="addressType"><b>Add New Address</b></div>'+
						'<div class="panel-body panel-body-address">'+
							'<div class="new-entity">'+
								'<span class="glyphicon glyphicon-plus" style="font-size: 20px;left: 30px;top: 30px;"></span>'+
							'</div>'+
							'<div class="newAddressDiv"></div>'+
						'</div>'+
					'</div>'+
				'</div>'+
				'<div class="col-md-12">'+
	                 '<button type="button" class="btn btn-primary btn-right" id="paymentButton">'+
	                     'Proceed to Payment  <span class="glyphicon glyphicon-circle-arrow-right"></span>'+
	                  '</button>'+
	            '</div>'+
			'</div>';
		$(".select-address-content").html(string);
	});
}

function getOrderStatus(){
	var addressId = $('input[name="addressRadio"]:checked').val();
	var paymentMode = $("a.payment-type.active-list-item").html();
	var string="";
	$.getJSON("http://localhost:8080/shoppingbee/current/checkout/"+addressId, function(data){
		console.log(data)
		var orderItem = data.orderItem;
		var address = data.orderAddress;
		var date = new Date(parseInt(data.orderDate) + (4*86400000));
		var deliveryDate = date.getDate()+"-"+(date.getMonth()+1)+"-"+date.getFullYear();
		
		string+='<div class="col-sm-12">'+
					'<div class="alert alert-success"><strong>Order placed successfully!</strong></div>'+
					'<div class="well well-sm">'+
						'<div>Payment made through: '+ paymentMode +'</div>'+
						'<div>To be deliverd on or before: '+ deliveryDate +'</div>'+
					'</div>'+
				'<div>'+
				'<div class="col-sm-8">';
		$.each(orderItem, function(j){
			string+='<div class="row well well-sm">'+
						'<div class="col-sm-5">'+
							'<img id="profilePic" src="../assets/images/'+ orderItem[j].product.imageSRC +'" class="img-thumbnail product-image" alt="" style="max-height:100px">'+
							'</div>'+
						'<div class="col-sm-6">'+
							'<div><h3>'+orderItem[j].product.productName+'</h3></div>'+
							'<div>Price: Rs.'+orderItem[j].product.price+'</div>'+
							'<div>Quantity: '+orderItem[j].quantity+'</div>'+
						'</div>'+
					'</div>';
		});
		string+='</div>'+
						'<div class="col-sm-4">'+
							'<div><b>Delivery Address: '+ address.name +'</b></div>'+
							'<div>'+ address.street +'</div>'+
							'<div>City: '+ address.city + ", "+ address.state +'</div>'+
							'<div>PIN code: '+ address.pincode +'</div>'+
							'<div>Contact No:'+ address.contactNo +'</div>'+
						'</div>'+
				'</div>'+
			'</div>';
		string+='<div class="col-md-12">'+
			        '<button type="button" class="btn btn-primary btn-right">'+
				        '<a href="http://localhost:8080/shoppingbee/static/shoppingbee.com/account.html?entry=Orders" class="goToOrders">'+
				        	'Go to Orders <span class="glyphicon glyphicon-circle-arrow-right"></span>'+
				        '</a>'+
				     '</button>'+
			     '</div>';
		$(".order-status-body").html(string);
	});		
}
function addNewAddress(category){
	var formData = {};
	$("#newAddressForm").find("input[name]").each(function (index, node) {
	    formData[node.name] = node.value;
	});
	formData["addressType"] = $('select[name="addressType"]').val();
	var myJSON = JSON.stringify(formData);
	console.log(myJSON);
	var saveData = $.ajax({
	      type: 'POST',
	      url: "http://localhost:8080/shoppingbee/current/addNewAddress",
	      data: myJSON,
	      contentType: "application/json",
	      success: function(data) { 
	    	  if(category=="account"){
	    		  location.assign("http://localhost:8080/shoppingbee/static/shoppingbee.com/account.html?entry=AddressBook");
	    	  }  
	    	  else{
	    		  getDeliveryAddresses();
	    		  $('.modal-backdrop').remove();
	    	  }
	      }
	});
}