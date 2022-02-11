package in.vineetks.shoppingbee.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import in.vineetks.shoppingbee.model.Address;
import in.vineetks.shoppingbee.model.Order;
import in.vineetks.shoppingbee.model.Product;
import in.vineetks.shoppingbee.model.UserProfile;
import in.vineetks.shoppingbee.service.OrderService;
import in.vineetks.shoppingbee.service.ProductService;
import in.vineetks.shoppingbee.service.ProfileService;

@RestController
@RequestMapping(value = "/admin")
public class AdminController {
	
	@Autowired
	ProfileService profileService;
	
	@Autowired
	ProductService productService;
	
	@Autowired
	OrderService orderService;
	
	@RequestMapping(value = "/users", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody List<UserProfile> listAllUsers() {
		return profileService.getAllUsers();
	}
	
	@RequestMapping(value = "/products", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody List<Product> listAllProducts() {
		return productService.getAllProducts();
	}
	
	@RequestMapping(value = "/addresses", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody List<Address> listAddressBook() {
		return profileService.getAllAddresses();
	}
	
	@RequestMapping(value = "/orders", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody List<Order> listAllOrders() {
		return orderService.getAdminOrders();
	}
	
	@RequestMapping(value = "/addProduct", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public @ResponseBody Product addProduct(@RequestBody String productString) {
		return productService.addProduct(productString);
	}
}
