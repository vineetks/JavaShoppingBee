package in.vineetks.shoppingbee.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import in.vineetks.shoppingbee.model.Order;

@Repository("orderDao")
public class OrderDao extends AbstractDao<Integer, Order>{
	
	@SuppressWarnings("unchecked")
	public List<Order> getAllOrders() {
		Criteria criteria = createEntityCriteria();
		return (List<Order>) criteria.list();
	}
	
	public Order getOrderById(int orderId) {
		Criteria criteria = createEntityCriteria();
		criteria.add(Restrictions.eq("orderId", orderId));
		return (Order) criteria.uniqueResult();
	}

	public void deleteOrder(int orderId) {
		Query query = getSession().createSQLQuery("DELETE from Orders where orderId = :orderId");
		query.setInteger("orderId", orderId);
		query.executeUpdate();
	}

	public void saveOrder(Order order) {
		persist(order);
	}
}
