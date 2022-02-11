package in.vineetks.shoppingbee.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.vineetks.shoppingbee.model.Order;
import in.vineetks.shoppingbee.model.Product;
import in.vineetks.shoppingbee.dao.UserProfileDao;
import in.vineetks.shoppingbee.model.Address;
import in.vineetks.shoppingbee.model.UserProfile;
import in.vineetks.shoppingbee.model.Wishlist;

@Service("profileService")
@Transactional
public class ProfileService {
	
	@Autowired
	private UserProfileDao dao;
	
	@Autowired
	private ProductService productService;

	public List<UserProfile> getAllUsers(){
		return dao.getAllUsers();
	}
	
	public UserProfile getCurrentUser(){
		return dao.getUserByUsername(getPrincipal());
	}
	
	public UserProfile getUserByUsername(String username){
		return dao.getUserByUsername(username);
	}
	
	public List<Order> getAllOrders() {
		return (List<Order>) getCurrentUser().getOrders();
	}
	
	public List<Address> getUserAddressBook() {
		return (List<Address>) getCurrentUser().getAddressList();
	}
	
	public List<Address> getAllAddresses() {
		return (List<Address>) dao.getAllAddresses();
	}
	
	public Address getAddressById(int addressId) {
		return dao.getAddressById(addressId);
	}
	
	public List<Wishlist> getWishlist() {
		return (List<Wishlist>) getCurrentUser().getWishlist();
	}
	
	public List<Address> addNewAddress(Address address) {
		List<Address> addressList = (List<Address>) getCurrentUser().getAddressList();
		address.setUser(getCurrentUser());
		addressList.add(address);
		return getUserAddressBook();
	}
	
	public UserProfile createUser(UserProfile user){
		user.setJoinedDate(new Date());
		user.setUsername(user.getEmail());
		user.setRole("USER");
		saveUser(user);
		return user;
    }
	
	public void saveUser(UserProfile user) {
		dao.saveUser(user);
	}
	
	public UserProfile updateProfile(UserProfile updatedUser) {
		UserProfile user = getCurrentUser();
		user.setMobileNo(updatedUser.getMobileNo());
		user.setName(updatedUser.getName());
		return user;
	}
	
	private String getPrincipal(){
        String username = null;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
 
        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }
        return username;
    }

	public ResponseEntity<String> updateWishlist(String action, int productId) {
		Product product = productService.getProductById(productId);
		Collection<Wishlist> wishlists = getWishlist();
		if(wishlists==null)
			wishlists = new ArrayList<Wishlist>();
		if(action.equals("add")){
			for(Wishlist item: wishlists){
				if(item.getProduct().getProductId()==productId){
					return new ResponseEntity<String>("\""+product.getProductName() + "\" already in the wishlist", HttpStatus.OK);
				}
			}
			Wishlist wishlist = new Wishlist();
			wishlist.setProduct(product);
			wishlist.setUser(getCurrentUser());
			wishlists.add(wishlist);
			return new ResponseEntity<String>("\""+product.getProductName() + "\" successfully added to wishlist", HttpStatus.OK);
		}
		else{
			dao.deleteFromWishlist(productId);
			return new ResponseEntity<String>("\""+product.getProductName() + "\" successfully removed from wishlist", HttpStatus.OK);
		}
	}
}
