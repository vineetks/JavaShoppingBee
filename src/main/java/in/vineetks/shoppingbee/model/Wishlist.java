package in.vineetks.shoppingbee.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Wishlist {

	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	private int wishlistId;
	
	@ManyToOne
	@JoinColumn(name="productId")
	private Product product;
	
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name="userId")
	private UserProfile user;

	//Getter and Setters
	public int getWishlistId() {
		return wishlistId;
	}

	public void setWishlistId(int wishlistId) {
		this.wishlistId = wishlistId;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public UserProfile getUser() {
		return user;
	}

	public void setUser(UserProfile user) {
		this.user = user;
	}
}
