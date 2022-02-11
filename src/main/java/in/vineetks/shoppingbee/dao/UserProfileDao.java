package in.vineetks.shoppingbee.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import in.vineetks.shoppingbee.model.Address;
import in.vineetks.shoppingbee.model.UserProfile;

@Repository("userProfileDao")
public class UserProfileDao extends AbstractDao<Integer, UserProfile>{

	@SuppressWarnings("unchecked")
	public List<UserProfile> getAllUsers() {
		Criteria criteria = createEntityCriteria();
		return (List<UserProfile>) criteria.list();
	}
	
	public UserProfile getUserByUsername(String username){
		Criteria criteria = createEntityCriteria();
		criteria.add(Restrictions.eq("username", username));
		return (UserProfile) criteria.uniqueResult();
	}
	
	public Address getAddressById(int addressId) {
		Criteria criteria = getSession().createCriteria(Address.class);
		criteria.add(Restrictions.eq("addressId", addressId));
		return (Address) criteria.uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	public List<Address> getAllAddresses() {
		Criteria criteria = getSession().createCriteria(Address.class);
		return (List<Address>) criteria.list();
	}
	
	public void saveUser(UserProfile user) {
		persist(user);
	}
	
	public void deleteUser(String username) {
		Query query = getSession().createSQLQuery("DELETE from UserProfile where username = :username");
		query.setString("username", username);
		query.executeUpdate();
	}
	
	public void deleteFromWishlist(int productId) {
		Query query = getSession().createSQLQuery("DELETE from Wishlist where productId = :productId");
		query.setInteger("productId", productId);
		query.executeUpdate();
	}
}
