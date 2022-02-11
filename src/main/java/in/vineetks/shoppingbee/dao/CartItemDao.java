package in.vineetks.shoppingbee.dao;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import in.vineetks.shoppingbee.model.CartItem;

@Repository("cartItemDao")
public class CartItemDao extends AbstractDao<Integer, CartItem>{

	public CartItem getByKey(int cartItemId) {
		Criteria criteria = createEntityCriteria();
		criteria.add(Restrictions.eq("cartItemId", cartItemId));
		return (CartItem) criteria.uniqueResult();
	}
	
	public void deleteCartItem(int cartItemId) {
		Query query = getSession().createSQLQuery("DELETE from CartItem where cartItemId = :cartItemId");
		query.setInteger("cartItemId", cartItemId);
		query.executeUpdate();
	}
	
	public void saveCartItem(CartItem cartItem) {
		persist(cartItem);
	}
}
