package in.vineetks.shoppingbee.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class CartItem {
	
	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	private int cartItemId;
	
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name="cartId")
	private Cart userCart;
	
	@OneToOne
	@JoinColumn(name="productId")
	private Product product;
	
	private int quantity;
	
	//Getters and Setters
	public int getCartItemId() {
		return cartItemId;
	}
	public void setCartItemId(int cartItemId) {
		this.cartItemId = cartItemId;
	}
	public Cart getUserCart() {
		return userCart;
	}
	public void setUserCart(Cart userCart) {
		this.userCart = userCart;
	}
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

}
