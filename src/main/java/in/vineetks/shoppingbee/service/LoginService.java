package in.vineetks.shoppingbee.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.vineetks.shoppingbee.model.UserProfile;

@Service("loginService")
public class LoginService implements UserDetailsService{
	
	@Autowired
    private ProfileService profileService;
    private String errorSource = "password"; 
    
    @SuppressWarnings("unused")
	@Transactional(readOnly=true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserProfile user = profileService.getUserByUsername(username);
        System.out.println("User : "+ user.getUsername());
        if(user==null){
            System.out.println("User not found");
            setErrorSource("username");
            throw new UsernameNotFoundException("Username not found");
        }
            return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), 
                 true, true, true, true, getGrantedAuthorities(user));
    }
 
     
    private List<GrantedAuthority> getGrantedAuthorities(UserProfile user){
    	List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
    	String role= user.getRole();
    	authorities.add(new SimpleGrantedAuthority("ROLE_"+role));
        return authorities;
    }

	public String getErrorSource() {
		return errorSource;
	}

	public void setErrorSource(String errorSource) {
		this.errorSource = errorSource;
	}	
}
