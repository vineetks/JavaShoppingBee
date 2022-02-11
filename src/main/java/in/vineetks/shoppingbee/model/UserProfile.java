package in.vineetks.shoppingbee.model;

import java.util.Collection;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class UserProfile {
	
	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	private int userId;
	private String name;
	private String username;
	private String email;
	@JsonIgnore
	private String password;
	private String mobileNo;
	private String imageSRC;
	private String role;
	
	@JsonIgnore
	@LazyCollection(LazyCollectionOption.FALSE)
	@OneToMany(mappedBy="user", cascade = CascadeType.ALL)
	private Collection<Address> addressList;
	
	@OneToOne(mappedBy="user", cascade = CascadeType.ALL)
	private Cart userCart;
	
	@JsonIgnore
	@LazyCollection(LazyCollectionOption.FALSE)
	@OneToMany(mappedBy="user", cascade = CascadeType.ALL)
	private Collection<Order> orders;
	
	@JsonIgnore
	@LazyCollection(LazyCollectionOption.FALSE)
	@OneToMany(mappedBy="user", cascade = CascadeType.ALL)
	private Collection<Wishlist> wishlist;

	@Temporal(TemporalType.DATE)
	private Date joinedDate;
	
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getMobileNo() {
		return mobileNo;
	}
	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}
	public String getImageSRC() {
		return imageSRC;
	}
	public void setImageSRC(String imageSRC) {
		this.imageSRC = imageSRC;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public Collection<Address> getAddressList() {
		return addressList;
	}
	public void setAddressList(Collection<Address> addressList) {
		this.addressList = addressList;
	}
	public Cart getUserCart() {
		return userCart;
	}
	public Collection<Wishlist> getWishlist() {
		return wishlist;
	}
	public void setWishlist(Collection<Wishlist> wishlist) {
		this.wishlist = wishlist;
	}
	public void setUserCart(Cart userCart) {
		this.userCart = userCart;
	}
	public Collection<Order> getOrders() {
		return orders;
	}
	public void setOrders(Collection<Order> orders) {
		this.orders = orders;
	}
	public Date getJoinedDate() {
		return joinedDate;
	}
	public void setJoinedDate(Date joinedDate) {
		this.joinedDate = joinedDate;
	}
	
	@Override
	public String toString() {
		return "UserProfile [userId=" + userId + ", username=" + username + ", joinedDate="
				+ joinedDate + ", email=" + email + ", mobileNo=" + mobileNo + ", imageSRC=" + imageSRC  + "]";
	}
}
