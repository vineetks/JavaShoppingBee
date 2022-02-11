package in.vineetks.shoppingbee.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import in.vineetks.shoppingbee.model.UserProfile;
import in.vineetks.shoppingbee.service.ProfileService;

@Controller
@RequestMapping(value = "/")
public class AppController {
	@Autowired
	ProfileService profileService;
	
	@RequestMapping(value = "/",method = RequestMethod.GET)
	public String goToIndex() {
		return "redirect:/static/shoppingbee.com/index.html";
	}
	
	@RequestMapping(value = "/accessDenied",method = RequestMethod.GET)
	public String accessDenied() {
		return "redirect:/static/shoppingbee.com/error.html";
	}
	
	@RequestMapping(value = "/loginStatus",method = RequestMethod.GET, produces="application/json")
	public @ResponseBody String loginPage() {
		String name, role;
		if(getPrincipal()!="anonymousUser"){
			name = profileService.getCurrentUser().getName().split(" ")[0];
			role = profileService.getCurrentUser().getRole();
		}else{
			name = "Guest";
			role = "GUEST";
		}
		return getPrincipal()+"@@"+name+"@@"+role;
	}
	
	@RequestMapping(value="/logout", method = RequestMethod.GET)
    public String logoutPage (HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){    
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/login?logout";
    }
	
	@RequestMapping(value = "/signup/{username}",method = RequestMethod.GET, produces="application/json")
	public @ResponseBody String usernameAvailablity(@PathVariable("username") String username) {
		UserProfile user = profileService.getUserByUsername(username);
		if(user==null){
			return "{\"availability\": \"available\"}";
		}else{
			return "{\"availability\": \"unavailable\"}";
		}
	}
	
	@RequestMapping(value = "/signup",method = RequestMethod.POST)
	public String registerUser(UserProfile user) {
		profileService.createUser(user);
		return "redirect:/";
	}
	
	private String getPrincipal(){
        String username = null;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }
        return username;
    }
}