package br.com.tradeflow.config;

import br.com.tradeflow.config.security.KeycloakLogoutHandler;
import br.com.tradeflow.domain.entity.Usuario;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Configuration
@EnableWebSecurity
class SecurityConfig {

	private static final String GROUPS = "groups";
	private static final String REALM_ACCESS_CLAIM = "realm_access";
	private static final String ROLES_CLAIM = "roles";

	private final KeycloakLogoutHandler keycloakLogoutHandler;

	SecurityConfig(KeycloakLogoutHandler keycloakLogoutHandler) {
		this.keycloakLogoutHandler = keycloakLogoutHandler;
	}

	@Bean
	public SessionRegistry sessionRegistry() {
		return new SessionRegistryImpl();
	}

	@Bean
	protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
		return new RegisterSessionAuthenticationStrategy(sessionRegistry());
	}

	@Bean
	public HttpSessionEventPublisher httpSessionEventPublisher() {
		return new HttpSessionEventPublisher();
	}

	@Bean
	public SecurityFilterChain resourceServerFilterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests(auth -> auth
				.requestMatchers(new AntPathRequestMatcher("/customers*")).hasRole("admin")
				.requestMatchers(new AntPathRequestMatcher("/swagger-ui/*")).hasRole("admin")
				.requestMatchers(new AntPathRequestMatcher("/v3/api-docs")).hasRole("admin")
				.requestMatchers(new AntPathRequestMatcher("/v3/api-docs/swagger-config")).hasRole("admin")
				.requestMatchers(new AntPathRequestMatcher("/")).permitAll()
				.anyRequest().authenticated()
		);
		http.oauth2ResourceServer((oauth2) -> oauth2
				.jwt(Customizer.withDefaults())
		);
		http.oauth2Login(Customizer.withDefaults()).logout(logout ->
				logout.addLogoutHandler(keycloakLogoutHandler).logoutSuccessUrl("/")
		);
		return http.build();
	}

	@Bean
	public GrantedAuthoritiesMapper userAuthoritiesMapperForKeycloak() {
		return authorities -> {
			Set<GrantedAuthority> mappedAuthorities = new HashSet<>();
			var authority = authorities.iterator().next();
			boolean isOidc = authority instanceof OidcUserAuthority;

			if (isOidc) {
				var oidcUserAuthority = (OidcUserAuthority) authority;
				var userInfo = oidcUserAuthority.getUserInfo();

				// Tokens can be configured to return roles under
				// Groups or REALM ACCESS hence have to check both
				if (userInfo.hasClaim(REALM_ACCESS_CLAIM)) {
					var realmAccess = userInfo.getClaimAsMap(REALM_ACCESS_CLAIM);
					var roles = (Collection<String>) realmAccess.get(ROLES_CLAIM);
					mappedAuthorities.addAll(generateAuthoritiesFromClaim(roles));
				}
				else if (userInfo.hasClaim(GROUPS)) {
					Collection<String> roles = (Collection<String>) userInfo.getClaim(GROUPS);
					mappedAuthorities.addAll(generateAuthoritiesFromClaim(roles));
				}
			}
			else {
				var oauth2UserAuthority = (OAuth2UserAuthority) authority;
				Map<String, Object> userAttributes = oauth2UserAuthority.getAttributes();

				if (userAttributes.containsKey(REALM_ACCESS_CLAIM)) {
					Map<String, Object> realmAccess = (Map<String, Object>) userAttributes.get(REALM_ACCESS_CLAIM);
					Collection<String> roles = (Collection<String>) realmAccess.get(ROLES_CLAIM);
					mappedAuthorities.addAll(generateAuthoritiesFromClaim(roles));
				}
			}
			return mappedAuthorities;
		};
	}

	Collection<GrantedAuthority> generateAuthoritiesFromClaim(Collection<String> roles) {
		Collection<GrantedAuthority> list = new ArrayList<>();
		Set<String> rolesNomes = Arrays.stream(Usuario.Role.values()).map(r -> r.name()).collect(Collectors.toSet());
		for (String role : roles) {
			if(rolesNomes.contains(role.toUpperCase())) {
				list.add(new SimpleGrantedAuthority("ROLE_" + role));
			}
		}
		return list;
	}
}
