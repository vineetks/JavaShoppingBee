package in.vineetks.shoppingbee.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import in.vineetks.shoppingbee.model.Filter;

@Repository("filterDao")
@SuppressWarnings("unchecked")
public class FilterDao extends AbstractDao<Integer, Filter>{

	public List<Filter> getFiltersByCategory(String category) {
		Criteria criteria = createEntityCriteria();
		criteria.add(Restrictions.eq("category", category));
		return criteria.list();
	}

	public List<String> getFilterNames(String category) {
		Query query = getSession().createSQLQuery("SELECT DISTINCT filterName from Filter where category = :category");
		query.setString("category", category);
		return (List<String>) query.list();
	}

	public List<Filter> getFilterByName(String category, String filterName) {
		Criteria criteria = createEntityCriteria();
		criteria.add(Restrictions.eq("category", category));
		criteria.add(Restrictions.eq("filterName", filterName));
		return criteria.list();
	}
}
