package in.vineetks.shoppingbee.model;

import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Cart {
	
	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	private int cartId;
	
	@JsonIgnore
	@OneToOne
	@JoinColumn(name="userId")
	private UserProfile user;
	
	@JsonIgnore
	@LazyCollection(LazyCollectionOption.FALSE)
	@OneToMany(mappedBy="userCart", cascade = CascadeType.ALL)
	private Collection<CartItem> cartItem;
	
	//Getters and Setters
	public int getCartId() {
		return cartId;
	}
	public void setCartId(int cartId) {
		this.cartId = cartId;
	}
	public UserProfile getUser() {
		return user;
	}
	public void setUser(UserProfile user) {
		this.user = user;
	}
	public Collection<CartItem> getCartItem() {
		return cartItem;
	}
	public void setCartItem(Collection<CartItem> cartItem) {
		this.cartItem = cartItem;
	}
}
