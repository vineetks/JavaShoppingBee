package in.vineetks.shoppingbee.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import in.vineetks.shoppingbee.model.Address;
import in.vineetks.shoppingbee.model.CartItem;
import in.vineetks.shoppingbee.model.UserProfile;
import in.vineetks.shoppingbee.model.Wishlist;
import in.vineetks.shoppingbee.service.CartService;
import in.vineetks.shoppingbee.service.OrderService;
import in.vineetks.shoppingbee.service.ProfileService;
import in.vineetks.shoppingbee.model.Order;
import in.vineetks.shoppingbee.model.Product;

@RestController
@RequestMapping(value = "/current")
public class UserController {
	
	@Autowired
	ProfileService profileService;
	
	@Autowired
	CartService cartService;
	
	@Autowired
	OrderService orderService;
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String updateProfile(UserProfile updatedUser) {
		profileService.updateProfile(updatedUser);
		return "redirect:/static/shoppingbee.com/account.html?entry=Details";
	}
	
	@RequestMapping(value = "/getDetails", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody UserProfile getCurrentUser() {
		return profileService.getCurrentUser();
	}
	
	@RequestMapping(value = "/getOrders", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody List<Order> listUserOrders() {
		return orderService.getAllOrders();
	}
	
	@RequestMapping(value = "/getAddressBook", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody List<Address> listUserAddressBook() {
		return profileService.getUserAddressBook();
	}
	
	@RequestMapping(value = "/getWishlist", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody List<Wishlist> listWishlist() {
		return profileService.getWishlist();
	}
	
	@RequestMapping(value = "/updateWishlist/{action}/{productId}", method = RequestMethod.POST)
	public ResponseEntity<String> updateWishlist(@PathVariable("action") String action, @PathVariable("productId") int productId) {
		return profileService.updateWishlist(action, productId);
	}
	
	@RequestMapping(value = "/addNewAddress", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public @ResponseBody List<Address> addNewAddress(@RequestBody String jsonData) throws JsonParseException, JsonMappingException, IOException {
	    ObjectMapper objectMapper = new ObjectMapper();
	    Address address = objectMapper.readValue(jsonData, Address.class); 
		return (List<Address>) profileService.addNewAddress(address);
	}
	
	@RequestMapping(value = "/cart", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody List<CartItem> getCartItems() {
		return cartService.getCartItems();
	}
	
	@RequestMapping(value = "/addToCart/{productId}", method = RequestMethod.POST)
	public ResponseEntity<String> addToCart(@PathVariable("productId") int productId) {
		return cartService.addToCart(productId);
	}
	
	@RequestMapping(value = "/removeFromCart/{cartItemId}", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody Product removeFromCart(@PathVariable("cartItemId") int cartItemId) {
		return cartService.removeFromCart(cartItemId);
	}
	
	@RequestMapping(value = "/changeQuantity/{cartItemId}/{quantity}", method = RequestMethod.GET)
	public @ResponseBody void changeQuantity(@PathVariable("cartItemId") int cartItemId, @PathVariable("quantity") int quantity) {
		cartService.changeQuantity(cartItemId, quantity);
	}
	
	@RequestMapping(value = "/checkout/{addressId}", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody Order moveCartToOrder(@PathVariable("addressId") int addressId) {
		return orderService.transferCart(addressId);
	}
}
