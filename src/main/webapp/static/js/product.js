var filterType;
$(document).ready(function(){
	
	$(document).on( "keyup", '#search', function() {
        $("#searchResults").css("display", "block");
        $(':not(".modified-search")').click( function(){
            $("#searchResults").css("display", "none");
        });
        searchProducts($(this).val());
    });
	
    $(document).on( "click", 'input[type="checkbox"]', function() {
    	var checkedValues ={};
        $('input:checkbox:checked').map(function() { 
        	var name = $(this).attr("data-filterName");
        	var value = $(this).attr("data-filterId"); 
        	checkedValues[name] = ( typeof checkedValues[name] != 'undefined' && checkedValues[name] instanceof Array ) ? checkedValues[name] : [];
        	checkedValues[name].push(value);
        	return checkedValues;
        }).get();
        
        setFilteredProducts(category, checkedValues);
    });

    $(document).on( "click", '.product-list>li>ul>li>a', function() {
        $(".product-list>li>ul>li>a").css("background","none");
        $(".product-list>li>ul>li>a").next().css("display","none");
        $(this).css("background","rgba(0,0,0,0.3)");
        $(this).next().css("display","block");
        var value = $(this).attr("data-filterId");
        var keys3 = $(this).attr("data-category");
        var checkedValues ={};
        filterType = ["Sub-Category"];
        checkedValues["Sub-Category"] = ( typeof checkedValues[name] != 'undefined' && checkedValues[name] instanceof Array ) ? checkedValues[name] : [];
        checkedValues["Sub-Category"].push(value);
        setFilteredProducts(keys3, checkedValues);
        event.preventDefault();
    });
    
    var category = getUrlPath();
    if(category == "productdetail"){
        var productID = getUrlParameter('productID');
        productItemDetail(productID);
    }
    else if(category == "account"){
    	var entry = getUrlParameter('entry');
    	updateAccountDetails(entry);
    }
    else if( (category == "index") || (category == "" )){
    	var categories = ["electronics", "books", "sports", "clothing"];
    	$.each(categories, function(i){
    		setProductItems(categories[i]);
    		$(document).ajaxStop(function(){
    			$("#"+ categories[i] + "Head").html(capitalizeFirst(categories[i]));
    		});
    	});
    	setHomeFilters(categories);
    }
    else if( (category == "electronics") || (category == "books") || (category == "sports") || (category == "clothing")){
        setProductItems(category);
        setFilters(category);
    }
});

function capitalizeFirst(string){
	string = string.toLowerCase().replace(/\b[a-z]/g, function(letter) {
	    return letter.toUpperCase();
	});
	return string;
}

function addToWishlist(productId){
    $.post("http://localhost:8080/shoppingbee/current/updateWishlist/add/"+productId, function(data, statusText, xhr){
        var string = 	'<div class="alert alert-success">'+
                            '<a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>'+
                            '<strong>'+ xhr.responseText +'</strong>'+
                        '</div>';
		$(".addCartMessage").html(string);
    });
}

function removeFromWishlist(productId){
    $.post("http://localhost:8080/shoppingbee/current/updateWishlist/remove/"+productId, function(data, statusText, xhr){
        var string =    '<div class="alert alert-warning">'+
					        '<a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>'+
				            '<strong>'+ xhr.responseText +'</strong>'+
				        '</div>';
        $(".addCartMessage").html(string);
    });
}

function getUrlPath() {
    var sPageURL = window.location.pathname,
        sURLVariables = sPageURL.split('shoppingbee.com/');
    var sPath = sURLVariables[1].split('.html');
    return sPath[0] === undefined ? true : sPath[0];        
};

var getUrlParameter = function getUrlParameter(sParam) {
    var sPageURL = decodeURIComponent(window.location.search.substring(1)),
        sURLVariables = sPageURL.split('&'),
        sParameterName,
        i;

    for (i = 0; i < sURLVariables.length; i++) {
        sParameterName = sURLVariables[i].split('=');
        if (sParameterName[0] === sParam) {
            return sParameterName[1] === undefined ? true : sParameterName[1];
        }
    }
};

function setFilters(categoryParam){
	var string="";
	var data = [];
	$.getJSON("http://localhost:8080/shoppingbee/filters/"+categoryParam+"/filterName", function(data1){
		filterType = data1;
		$.each(data1, function(i){
			$.getJSON("http://localhost:8080/shoppingbee/filters/"+categoryParam+"/"+data1[i], function(data2){
				data[data1[i]]= data2;
			});
		});
	});
	$(document).ajaxStop(function(){
		$(this).unbind("ajaxStop");
		$.each(filterType, function(i){
	        string += '<div class="panel panel-default">'+        
	                        '<div class="panel-heading modified-panel-heading">'+
	                            '<h4 class="panel-title">' + filterType[i] + '</h4>' +
	                        '</div>'+
	                        '<div class="panel-body" class="' + filterType[i] + '">';
	        var temp = data[filterType[i]];
	        $.each(temp, function(j){
	            string +=   '<div class="checkbox">'+
	                            '<label><input type="checkbox" data-filterName="' + temp[j].filterName + '" data-filterId="'+ temp[j].filterId +'">' + temp[j].filterValue + '</label>'+
	                        '</div>';
	        });
	        string +=   '</div>'+
	        		'</div>';
	    });
		$(".filter-category").append(string);
	});
}

function setProductItems (categoryParam){	
	$.getJSON("http://localhost:8080/shoppingbee/products/"+categoryParam, function(data){
	    var string = "";
	    string += '<div class="row text-center product-item-container">';
	    string += '<h2 class="text-center" id="'+ categoryParam +'Head"></h2>';
	
	    $.each(data, function(i){
	        string += '<div class="col-sm-6 col-md-4">'+
	                    '<div class="individual-item">'+
                            '<span class="glyphicon glyphicon-heart-empty" data-productID="'+ data[i].productId +'"></span>'+
	                        '<a href="productdetail.html?productID=' + data[i].productId + '">'+
	                            '<img src="../assets/images/' + data[i].imageSRC + '" class="img-thumbnail" alt="' + data[i].productName + '" style="max-height:190px">'+
	                             '<h4>' + data[i].productName + '</h4>'+
	                        '</a>'+
	                        '<div class="row">'+
	                            '<div class="col-sm-6">Price: Rs. ' + data[i].price + '</div>'+
	                            '<div class="col-sm-6"><a href="#" class="add-to-cart" data-productID="'+ data[i].productId +'">Add to Cart</a></div>'+
	                        '</div>'+
	                    '</div>'+
	                '</div>';
	    });
	    string += '</div>';

	    $(".product-items").append(string);
    });
}

function productItemDetail(productID){
	var data;
	var specification;
	$.getJSON("http://localhost:8080/shoppingbee/products/getById/"+productID, function(product){
		data = product;
	});
	$.getJSON("http://localhost:8080/shoppingbee/products/getById/"+productID+"/specs", function(specs){
		specification = specs;
	});
	$(document).ajaxStop(function(){
		$(this).unbind("ajaxStop");
		var string = ""; 
	    string +=   '<div class="col-sm-12 col-md-6">'+
	                    '<img src="../assets/images/' + data.imageSRC + '" class="img-thumbnail product-image" alt="' + data.productName + '">'+
	                '</div>'+
	                '<div class="col-sm-12 col-md-6 product-details">'+
	                    '<h3>' + data.productName + '</h3>'+
	                    '<div> Price: Rs. ' + data.price + '</div>';
	    string += '<div class="product-specs row">';
	    $.each(specification, function(i){
	        string +=  		'<div class="col-sm-3">'+ specification[i].specName +'</div>'+
	        				'<div class="col-sm-9">: '+ specification[i].specValue+'</div>';
	    });
	    string += '</div>';
	    string += '<div style="padding: 10px 0;">'+
	                    '<a href="#" class="add-to-cart" data-productID="'+ productID +'">'+
	                           '<button type="button" class="btn btn-success">Add to cart</button>'+
                        '</a>'+
                        '<a href="#" class="add-to-wishlist btn-right" data-productID="'+ productID +'">'+
	                           '<button type="button" class="btn btn-success">Add to wishlist</button>'+
                        '</a>'+
	                '</div>'+
	            '</div>';
	    $(".product-detail").append(string); 
	});
}

function setHomeFilters(category){
	var string = "";
	    string += '<ul class="nav nav-stacked product-list">';
	var data = [];
	$.each(category, function(i){
		$.getJSON("http://localhost:8080/shoppingbee/filters/"+category[i]+"/Sub-Category", function(data1){
			data[category[i]]=data1;
		});
	});
	
	$(document).ajaxStop(function(){
		$(this).unbind("ajaxStop");
		$.each(category, function(i){			
			if(i==0){ var newClass = "active-product-list";}
		    else	{ var newClass = ""; }
			
		    string += '<li href="#collapse' + (i) + '">'+
		                    '<a href="#" data-active="#collapse' + (i) + '" class="capitalize '+ newClass +'">' + category[i] + 
		                        '<span class="glyphicon glyphicon-plus glyphicon-modified"></span>'+
		                    '</a>'+
		                    '<ul id="collapse' + i + '" class="product-sub-list">';
		    	var filter = data[category[i]];
			    $.each(filter, function(j){
			         string +=    '<li><a href="#" data-filter="subCategory" data-filterId="'+ filter[j].filterId +'" data-category="'+ category[i] +'">' + filter[j].filterValue + '</a><span class="glyphicon glyphicon-chevron-right glyphicon-chevron-modified"></span></li>';
			    });
		    string += '</ul>'+
		    		'</li>';
		});
		string += '</ul>';
	    $(".filter-category").html(string);
	});
}

function setFilteredProducts(categoryParam, checkedValues){
	var sql = [];
	var reqString="";
	var count = 0;
	$.each(filterType, function(i){
		sql[i] = "";
		if(typeof checkedValues[filterType[i]] != 'undefined' && checkedValues[filterType[i]] instanceof Array){
			var values = checkedValues[filterType[i]];
			$.each(values, function(j){
				if(j!=0)
					sql[i]+=" OR ";
				else
					sql[i]+="(";
				sql[i]+="filterId="+values[j];
			});
			sql[i]+=")";
			if(count!=0)
				reqString+="@@";
			reqString+=sql[i];
			count++;
		}
	});
	
	$.getJSON("http://localhost:8080/shoppingbee/products/"+categoryParam+"/getFilteredProducts?sqlString="+reqString, function(data){
		$(".product-items").html("");
		var string = "";
	    string += '<div class="row text-center product-item-container">';
	    string += '<h2 class="text-center" id="'+ categoryParam +'Head"></h2>';
	
	    $.each(data, function(i){
	        string += '<div class="col-sm-6 col-md-4">'+
	                    '<div class="individual-item">'+
                            '<span class="glyphicon glyphicon-heart-empty" data-productID="'+ data[i].productId +'"></span>'+
	                        '<a href="productdetail.html?productID=' + data[i].productId + '">'+
	                            '<img src="../assets/images/' + data[i].imageSRC + '" class="img-thumbnail" alt="' + data[i].productName + '" style="max-height:190px">'+
	                             '<h4>' + data[i].productName + '</h4>'+
	                        '</a>'+
	                        '<div class="row">'+
	                            '<div class="col-sm-6">Price: Rs. ' + data[i].price + '</div>'+
	                            '<div class="col-sm-6"><a href="#" class="add-to-cart" data-productID="'+ data[i].productId +'">Add to Cart</a></div>'+
	                        '</div>'+
	                    '</div>'+
	                '</div>';
	    });
	    string += '</div>';
	    
	    if(data.length>0){
	        successString = '<div class="alert alert-success row">'+
	                            '<a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>'+
	                            '<strong>' + data.length + ' results found!</strong>'+
	                        '</div>';
	    }else{
	        successString = '<div class="alert alert-danger row">'+
	                            '<a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>'+
	                            '<strong> No results found!</strong>'+
	                        '</div>';
	    }
	    $(".product-items").append(successString);
	    $(".product-items").append(string);
        if(loginStatus == "LoggedOut"){
            $(".glyphicon-heart-empty").remove();
        }
	});
    
//	var saveData = $.ajax({
//	      type: 'POST',
//	      url: "http://localhost:8080/shoppingbee/products/"+categoryParam+"/getFilteredProducts",
//	      data: reqString,
//	      contentType: "text",
//	      success: function(data) { 
//	    	  console.log(data);
//	      }
//	});
//	$("#loading").ajaxStart(function () {
//	    $(this).show();
//	 });
//
//	 $("#loading").ajaxStop(function () {
//	   $(this).hide();
//	 });
}

function searchProducts(searchKey){
	var searchKey = searchKey.replace(/\W+/g, " ");
	$.getJSON("http://localhost:8080/shoppingbee/products/search/"+searchKey, function(data){
		var string = "";
	    string += '<ul class="nav nav-stacked">';
	    $.each(data, function(i){
	        var temp = data[i];
	        string += '<li>'+
	        				'<a href="productdetail.html?productID=' + temp[0] + '">' + temp[1] + 
	        					'<span class="glyphicon glyphicon-chevron-right glyphicon-chevron-modified" style="color: black;"></span>'+
	        				'</a>'+
	        		  '</li>';
	    });
	    if(data.length==10){ 
	    	string += '<li><a href="#" style="color:black">See more results...</a></li>';
	    }
	    string += '</ul>';
	    $("#searchResults").html(string);
	});
}

function updateAccountDetails(entry){
	if(entry==undefined){
		entry="Details";
	}
	var string = "";
	$(".account-details").css("display","none");
	$("#user"+entry).css("display","block");
	$("."+entry+"-link").addClass("active-link");
	$.getJSON("http://localhost:8080/shoppingbee/current/get"+entry, function(data){
		if(entry=="Details"){
			$(".Details-link>ul").slideDown("slow");
			$("#name").val(data.name);
			$("#email").val(data.email);
			$("#userId").val(data.userId);
			$("#username").val(data.username);
			$("#mobileNo").val(data.mobileNo);
			$("#joinedDate").val(data.joinedDate);
			$("#profilePic").attr("src","../assets/images/"+data.imageSRC);
			var percent=-40;
			$("#updateProfileForm").find('input[name]').each(function (index, node) {
				if(node.value!="")
					percent+=20;
		    });
			if(data.imageSRC!=""){percent+=20;}
			$(".progress-bar").animate({"width": percent+"%"}).html(percent+"% complete");
		}
		else if(entry=="AddressBook"){
			if(data.length!=0){
				$.each(data, function(i){
					string+='<div class="col-sm-4"><div class="panel panel-default">'+
		  						'<div class="panel-heading" id="addressType"><b>'+ data[i].addressType +'</b></div>'+
		  						'<div class="panel-body panel-body-address">'+
		  							'<div>Name: '+ data[i].name +'</div>'+
		  							'<div>Street Name: '+ data[i].street +'</div>'+
		  							'<div>City: '+ data[i].city +'</div>'+
		  							'<div>State: '+ data[i].state +'</div>'+
		  							'<div>PIN code: '+ data[i].pincode +'</div>'+
		  							'<div>LandMark: '+ data[i].landmark +'</div>'+
		  							'<div>Contact No: +91-'+ data[i].contactNo +'</div>'+
		  						'</div>'+
		  					'</div></div>';
				});
			}
			else{
				string+='<div class="alert alert-danger">'+
			                '<a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>'+
			                '<strong> You haven\'t added any address yet!</strong>'+
			             '</div>';
			}
			$("#userAddressBook").prepend(string);
		}
		else if(entry=="Orders"){
			string+=fillOrderDiv(data);
			$("#userOrders").html(string);
		}
		else if(entry=="Wishlist"){
			string += '<div class="text-center product-item-container">';
			if(data.length!=0){
				$.each(data, function(i){
					var product = data[i].product;
			        string += '<div class="col-sm-6 col-md-4">'+
			                    '<div class="individual-item">'+
			                    	'<span class="glyphicon glyphicon-heart" data-productID="'+ product.productId +'"></span>'+
			                        '<a href="productdetail.html?productID=' + product.productId + '">'+
			                            '<img src="../assets/images/' + product.imageSRC + '" class="img-thumbnail" alt="' + product.productName + '" style="max-height:190px">'+
			                             '<h4>' +product.productName + '</h4>'+
			                        '</a>'+
			                        '<div class="row">'+
			                            '<div class="col-sm-6">Price: Rs. ' + product.price + '</div>'+
			                            '<div class="col-sm-6"><a href="#" class="add-to-cart move-to-cart" data-productID="'+ product.productId +'">Move to Cart</a></div>'+
			                        '</div>'+
			                    '</div>'+
			                '</div>';
			    });
			}
			else{
				string+='<div class="alert alert-danger">'+
			                '<a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>'+
			                '<strong> No products in wishlist!</strong>'+
			             '</div>';
			}
			string+='</div>';
			$("#userWishlist").html(string);
		}
		console.log(data);
	});
}

function fillOrderDiv(data){
	var string="";
	if(data.length!=0){
		$.each(data, function(i){
			string+='<div class="col-sm-12"><div class="panel panel-default">'+
						'<div class="panel-heading">'+
							'<div class="col-sm-4"><b>Order Id: #'+ data[i].orderId +'</b></div>'+
							'<div class="col-sm-4">Order Date: '+ data[i].orderDate +'</div>'+
							'<div><b>Status: '+ data[i].orderStatus +'</b></div>'+
						'</div>'+
						'<div class="panel-body">'+
							'<div class="col-sm-8">';
			orderItem = data[i].orderItem;
			address = data[i].orderAddress;
			$.each(orderItem, function(j){
				string+='<div class="row  well well-sm">'+	
							'<div class="col-sm-1">'+ (j+1) +'</div>'+
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
			'</div></div>';
		});
	}
	else{
		string+='<div class="alert alert-danger">'+
	                '<a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>'+
	                '<strong> No orders placed yet!</strong>'+
	             '</div>';
	}
	return string;
}