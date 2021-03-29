package com.eluon.vepc.mano.common;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.eluon.vepc.mano.vo.AccountUserDetailsVO;
 
public class AccountAuthenticationProvider implements AuthenticationProvider { 
	protected final Log logger = LogFactory.getLog(getClass());
	protected final Log loggerTra = LogFactory.getLog("MANO_PROCESS_TRA");
	protected final Log loggerSin = LogFactory.getLog("MANO_PROCESS_SIN");
     
    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
  
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String userId = (String)authentication.getPrincipal();    
        String password = (String)authentication.getCredentials();
         
         
        loggerTra.debug("사용자가 입력한 로그인정보입니다. {}"+ userId + "/" + password);
         
        if(userId.equals("test")&&password.equals("test")){
        	loggerTra.debug("정상 로그인입니다.");
            List<GrantedAuthority> roles = new ArrayList<GrantedAuthority>();
            roles.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
             
            UsernamePasswordAuthenticationToken result = new UsernamePasswordAuthenticationToken(userId, password, roles);
            result.setDetails(new AccountUserDetailsVO(userId, password));
            return result;         
        }else{
        	loggerTra.debug("사용자 크리덴셜 정보가 틀립니다. 에러가 발생합니다.");
            throw new BadCredentialsException("Bad credentials");
        }
    }
}
