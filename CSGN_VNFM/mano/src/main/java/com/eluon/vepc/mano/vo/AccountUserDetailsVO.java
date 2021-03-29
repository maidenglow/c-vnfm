package com.eluon.vepc.mano.vo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
 

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * AccountUserDetails VO (AccountUserDetailsVO)
 *
 * @author <a href="mailto:simhero@mredriver.com">SimByungChul</a>
 * @version $Id: AccountUserDetailsVO.java,v 1.0 2014/12/15 00:00:00 SimByungChul Express $
 */
public class AccountUserDetailsVO extends BaseVO implements UserDetails {
    private String username;
    private String password;
     
    public AccountUserDetailsVO(String userName, String password){
        this.username = userName;
        this.password = password;
    }
     
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();   
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
         
        return authorities;
    }
  
    @Override
    public String getPassword() {
        return password;
    }
  
    @Override
    public String getUsername() {
        return username;
    }
  
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
  
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
  
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
  
    @Override
    public boolean isEnabled() {
        return true;
    }
}
