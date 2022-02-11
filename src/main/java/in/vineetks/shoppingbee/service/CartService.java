package in.vineetks.shoppingbee.service;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.vineetks.shoppingbee.dao.CartItemDao;
import in.vineetks.shoppingbee.model.Cart;
import in.vineetks.shoppingbee.model.CartItem;
import in.vineetks.shoppingbee.model.Product;

@Service("cartService")
@Transactional
public class CartService {
	
	@Autowired
	private CartItemDao dao;
	
	@Autowired
	private ProfileService userService;
	
	@Autowired
	private ProductService productService;
	
	public List<CartItem> getCartItems() {
		Cart cart;
		if(userService.getCurrentUser().getUserCart()==null){
			cart = createCart();
		}else{
			cart = userService.getCurrentUser().getUserCart();
		}
		return (List<CartItem>) cart.getCartItem();
	}

	public ResponseEntity<String> addToCart(int productId) {	
		Cart cart = userService.getCurrentUser().getUserCart();
		Collection<CartItem> cartItems = (Collection<CartItem>) cart.getCartItem();
		Product product = productService.getProductById(productId);
		int cartItemId=0;
		for(CartItem cartItem : cartItems){
			if(cartItem.getProduct().getProductId()==productId){
				cartItemId = cartItem.getCartItemId();
				cartItem.setQuantity(cartItem.getQuantity()+1);
				userService.saveUser(userService.getCurrentUser());
				ResponseEntity<String> response = new ResponseEntity<String>(product.getProductName(), HttpStatus.ACCEPTED);
				return response;
			}
		}
		
		if(cartItemId==0){
			CartItem cartItem = new CartItem();
			cartItem.setProduct(product);
			cartItem.setQuantity(1);
			cartItem.setUserCart(cart);
			cart.getCartItem().add(cartItem);
			userService.saveUser(userService.getCurrentUser());
			ResponseEntity<String> response = new ResponseEntity<String>(product.getProductName(), HttpStatus.CREATED);
			return response; 
		}
		ResponseEntity<String> response = new ResponseEntity<String>(product.getProductName(), HttpStatus.OK);
		return response; 
	}
	
	public Product removeFromCart(int cartItemId) {
		Product product = dao.getByKey(cartItemId).getProduct();
		dao.deleteCartItem(cartItemId);
		return product;
	}

	public void changeQuantity(int cartItemId, int quantity) {
		CartItem cartItem = dao.getByKey(cartItemId);
		cartItem.setQuantity(quantity);
		dao.saveCartItem(cartItem);
	}
	
	private Cart createCart(){
		Cart cart = new Cart();
		cart.setUser(userService.getCurrentUser());
		userService.getCurrentUser().setUserCart(cart);
		userService.saveUser(userService.getCurrentUser());
		return cart;
	}

}
