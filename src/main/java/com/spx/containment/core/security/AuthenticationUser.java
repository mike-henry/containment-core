package com.spx.containment.core.security;

import java.util.Collection;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class AuthenticationUser extends AbstractAuthenticationToken {


  private final Object principle;

  /**
   * Creates a token with the supplied array of authorities.
   *
   * @param authorities the collection of <tt>GrantedAuthority</tt>s for the principal represented
   *                    by this authentication object.
   */
  AuthenticationUser(Collection<? extends GrantedAuthority> authorities, Object principle) {
    super(authorities);
    this.principle = principle;
  }


  @Override
  public Object getCredentials() {
    return super.getAuthorities();
  }

  @Override
  public Object getPrincipal() {
    return principle;
  }

  @Override
  public boolean isAuthenticated() {
    return true;
  }
}
