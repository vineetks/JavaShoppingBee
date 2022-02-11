package in.vineetks.shoppingbee.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.vineetks.shoppingbee.dao.FilterDao;
import in.vineetks.shoppingbee.model.Filter;

@Service("filterService")
@Transactional
public class FilterService {
	
	@Autowired
	private FilterDao dao;
	
	public List<Filter> getFiltersByCategory(String category) {
		return dao.getFiltersByCategory(category);
	}
	
	public List<String> getFilterNames(String category) {
		return dao.getFilterNames(category);
	}

	public List<Filter> getFilterByName(String category, String filterName) {
		return dao.getFilterByName(category, filterName);
	}
	
	public Filter getFilterById(int filterId) {
		return dao.getByKey(filterId);
	}
}
