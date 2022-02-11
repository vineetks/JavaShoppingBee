package in.vineetks.shoppingbee.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import in.vineetks.shoppingbee.model.Filter;
import in.vineetks.shoppingbee.service.FilterService;

@RestController
@RequestMapping(value = "/filters")
public class FilterController {
	
	@Autowired
	FilterService filterService;
	
	@RequestMapping(value = "/{category}", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody List<Filter> listFiltersByCategory(@PathVariable("category") String category) {
		return filterService.getFiltersByCategory(category);
	}
	
	@RequestMapping(value = "/{category}/filterName", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody List<String> listFilterNames(@PathVariable("category") String category) {
		return filterService.getFilterNames(category);
	}
	
	@RequestMapping(value = "/{category}/{filterName}", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody List<Filter> listFilterByName(@PathVariable("category") String category, @PathVariable("filterName") String filterName) {
		return filterService.getFilterByName(category, filterName);
	}
}
