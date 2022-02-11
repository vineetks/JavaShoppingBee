$(document).ready(function(){
	$(".admin-links").click(function(){
		$(".admin-links").removeClass("active-link");
		$(this).addClass("active-link");
	});
	getAllProducts();
	$("#getProducts").click(function(){
		$(".admin-div").fadeIn("slow");
		$(".add-product").fadeOut("slow");
		getAllProducts();
	});
	$("#getUsers").click(function(){
		$(".admin-div").fadeIn("slow");
		$(".add-product").fadeOut("slow");
		getAllUsers();
	});
	$("#getAddresses").click(function(){
		$(".admin-div").fadeIn("slow");
		$(".add-product").fadeOut("slow");
		getAllAddresses();
	});
	$("#getOrders").click(function(){
		$(".admin-div").fadeIn("slow");
		$(".add-product").fadeOut("slow");
		getAllOrders();
	});
	
	$("#addProduct").click(function(){
		$(".admin-div").fadeOut("slow");
		$(".add-product").fadeIn("slow");
	});
	$("#specButton").click(function(){
		$("#specInput").fadeIn("slow");
	});
	$('select[name="category"]').on('change', function() {
		var selectedCategory = $(this).val();
		getProductFilters(selectedCategory);
	});
	$(document).on("click", "#productSubmit", function(){
		event.preventDefault();
		addProduct();
	});
});
function getAllProducts(){
	$.getJSON("http://localhost:8080/shoppingbee/admin/products", function(data){
		var string='<div class="col-sm-12">';
		$.each(data, function(i){
			string+='<div class="row well well-sm">'+
						'<div class="col-sm-1">'+'</div>'+
						'<div class="col-sm-3">'+
							'<img src="../assets/images/'+ data[i].imageSRC +'" class="img-thumbnail product-image" alt="" style="max-height:100px">'+
						'</div>'+
						'<div class="col-sm-3">Product Name:<br>'+data[i].productName+'</div>'+
						'<div class="col-sm-3">Price:<br>Rs. '+data[i].price+'</div>'+
					'</div>';
		});
		string+='</div>';
		$(".admin-div").html(string);
	});
}

function getAllUsers(){
	$.getJSON("http://localhost:8080/shoppingbee/admin/users", function(data){
		console.log(data);
		var string='<div class="col-sm-12">';
		$.each(data, function(i){
			string+='<div class="row well well-sm">'+
						'<div class="col-sm-3">'+
							'<img src="../assets/images/'+ data[i].imageSRC +'" class="img-thumbnail product-image" alt="" style="max-height:100px">'+
						'</div>'+
						'<div class="col-sm-6">'+
							'<div>Name:'+data[i].name+'</div>'+
							'<div>User Id: #'+data[i].userId+'</div>'+
							'<div>Username:'+data[i].username+'</div>'+
							'<div>Email:'+data[i].email+'</div>'+
							'<div>Joining Date:'+data[i].joinedDate+'</div>'+
						'</div>'+
						'<div class="col-sm-3 form-group">'+
						  	'<label for="role">Change Role:</label>'+
						  	'<select class="form-control" name="role">'+
						  		'<option value="USER">User</option>'+
						  		'<option value="ADMIN">Admin</option>'+
							'</select>'+
						'</div>'+
					'</div>';
		});
		string+='</div>';
		$(".admin-div").html(string);
	});
}

function getAllAddresses(){
	$.getJSON("http://localhost:8080/shoppingbee/admin/addresses", function(data){
		console.log(data);
		var string ='<table class="table table-hover table-bordered">'+
						'<thead>'+
							'<tr>'+
								'<th>ID</th><th>Name</th><th>Address Type</th><th>Address</th>'+
								'<th>LandMark</th><th>Pincode</th><th>Contact</th>'+
							'</tr>'+
						'</thead>'+
						'<tbody>';
		$.each(data, function(i){
			string+='<tr>'+
			    		'<td>'+ data[i].addressId +'</td>'+
			    		'<td>'+ data[i].name +'</td>'+
			    		'<td>'+ data[i].addressType +'</td>'+
			    		'<td>'+ data[i].street + "," + data[i].city + "," + data[i].state +'</td>'+
			    		'<td>'+ data[i].landmark +'</td>'+
			    		'<td>'+ data[i].pincode +'</td>'+
			    		'<td>'+ data[i].contactNo +'</td>'+
			    	'</tr>';		
		});
		string+='</tbody>'+
    		'</table>';
		$(".admin-div").html(string);
	});
}
function getAllOrders(){
	$.getJSON("http://localhost:8080/shoppingbee/admin/orders", function(data){
		var string = fillOrderDiv(data);
		$(".admin-div").html(string);
	});
}

function getProductFilters(selectedCategory){
	var string="";
	var data = [];
	$.getJSON("http://localhost:8080/shoppingbee/filters/"+selectedCategory+"/filterName", function(data1){
		filterType = data1;
		$.each(data1, function(i){
			$.getJSON("http://localhost:8080/shoppingbee/filters/"+selectedCategory+"/"+data1[i], function(data2){
				data[data1[i]]= data2;
			});
		});
	});
	$(document).ajaxStop(function(){
		$(this).unbind("ajaxStop");
		$.each(filterType, function(i){
	        string += '<div class="form-group">'+
			  				'<select class="form-control" name="filterId" required>'+
			  					'<option value="" >--Select '+ filterType[i] +'--</option>';
	        var temp = data[filterType[i]];
	        $.each(temp, function(j){
	            string +=   '<option value="'+ temp[j].filterId +'" selected="selected">'+ temp[j].filterValue +'</option>';
	        });
	        string +=   '</select>'+
	        		'</div>';
	    });
		$("#filterSelect").html(string);
	});
}

function addProduct(){
	var formData = {};
	$("#newProductForm").find("input[name]").each(function (index, node) {
        formData[node.name] = node.value;
    });
	formData["category"]=$('select[name="category"]').val();
	formData["filterId"]=[];
	$("#newProductForm").find('select[name="filterId"]').each(function (index, node) {
        formData["filterId"].push(node.value);
    });
	formData["specName"]=[];
	formData["specValue"]=[];
	$("#newProductForm").find('input[name="specName"]').each(function (index, node) {
		var specValue = $('input[name="specValue"]').get(index).value;
		if(node.value!="" && specValue!=""){
			formData["specName"].push(node.value);
			formData["specValue"].push(specValue);
		}
    });
	
	var imageSRC=$("input[type='file']").val();
	formData["imageSRC"] = imageSRC.replace("C:\\fakepath\\", $('select[name="category"]').val()+'/');
	
	var myJSON = JSON.stringify(formData);
	var saveData = $.ajax({
	      type: 'POST',
	      url: "http://localhost:8080/shoppingbee/admin/addProduct",
	      data: myJSON,
	      contentType: "application/json",
	      success: function(data) { 
	    	  console.log(data);
	      }
	});
}