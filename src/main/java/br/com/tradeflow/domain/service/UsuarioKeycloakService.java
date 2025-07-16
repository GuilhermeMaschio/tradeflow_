package br.com.tradeflow.domain.service;

import br.com.tradeflow.domain.entity.Usuario;
import br.com.tradeflow.mapper.UsuarioMapper;
import br.com.tradeflow.util.DummyUtils;
import jakarta.ws.rs.core.Response;
import org.jetbrains.annotations.Nullable;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.*;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.MappingsRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class UsuarioKeycloakService {

	@Value("${keycloak.url}")
	private String keycloakUrl;

	@Value("${keycloak.master.realm}")
	private String keycloakMasterRealm;

	@Value("${keycloak.tradeflow.realm}")
	private String keycloakTradeflowRealm;

	@Value("${keycloak.master.username}")
	private String keycloakMasterUsername;

	@Value("${keycloak.master.password}")
	private String keycloakMasterPassword;

	@Value("${keycloak.master.client-id}")
	private String keycloakMasterClientId;

	@Autowired
	private UsuarioMapper usuarioMapper;

	public UserRepresentation cadastrar(Usuario usuario) {

		RealmResource realmResource = getRealmResource();
		UsersResource usersResource = realmResource.users();
		UserRepresentation userRepresentation = new UserRepresentation();
		fillUserRepresentation(usuario, userRepresentation);

		UserRepresentation user2 = cadastrar(usersResource, userRepresentation);
		if (user2 != null) {
			//se retornou user2 é pq o usuário já estava cadastrado no kc
			userRepresentation = user2;
		}
		else {
			//busca novamente para obser o kcId do usuário
			String usuarioEmail = usuario.getEmail();
			userRepresentation = getUserByEmail(usuarioEmail, usersResource);
		}

		String roles = usuario.getRoles();
		List<String> rolesList = DummyUtils.jsonToObject(roles, List.class);

		cadastrarRoles(userRepresentation, realmResource, rolesList);

		//busca novamente para ter certeza que vai retornar oq realmente foi registrado no kc
		String kcUserId = userRepresentation.getId();
		UserRepresentation user3 = usersResource.get(kcUserId).toRepresentation();
		fillRoles(usersResource, user3);
		return user3;
	}

	private RealmResource getRealmResource() {

		Keycloak keycloak = Keycloak.getInstance(
				keycloakUrl,
				keycloakMasterRealm,
				keycloakMasterUsername,
				keycloakMasterPassword,
				keycloakMasterClientId);

		RealmResource realmResource = keycloak.realm(keycloakTradeflowRealm);
		return realmResource;
	}

	private static void cadastrarRoles(UserRepresentation userRepresentation, RealmResource realmResource, List<String> rolesList) {

		String kcUserId = userRepresentation.getId();
		UsersResource usersResource = realmResource.users();
		UserResource ur = usersResource.get(kcUserId);
		RoleMappingResource rmr = ur.roles();
		RoleScopeResource rsr = rmr.realmLevel();

		RolesResource allRoles = realmResource.roles();

		List<RoleRepresentation> rolesRepresentations = new ArrayList<>();
		for (String role : rolesList) {
			RoleResource rr = allRoles.get(role);
			RoleRepresentation roleRepresentation = rr.toRepresentation();
			rolesRepresentations.add(roleRepresentation);
		}
		rsr.add(rolesRepresentations);
	}

	@Nullable
	private static UserRepresentation cadastrar(UsersResource usersResource, UserRepresentation user) {

		Response response = usersResource.create(user);
		int status = response.getStatus();
		if(status == 409) {//409 = Conflict, ou seja, usuário já cadastrado
			String usuarioEmail = user.getEmail();
			UserRepresentation user2 = getUserByEmail(usuarioEmail, usersResource);
			if (user2 != null) {//se realmente encontrou o usuário cadastrado, retornar ele
				return user2;
			}
		}
		if(status < 200 || status >= 300) {
			String error = response.readEntity(String.class);
			Response.StatusType statusInfo = response.getStatusInfo();
			throw new RuntimeException("erro ao acessar keycloak: " + statusInfo + " - " + error);
		}
		return null;
	}

	private static UserRepresentation getUserByEmail(String usuarioEmail, UsersResource usersResource) {

		List<UserRepresentation> users = usersResource.searchByEmail(usuarioEmail, true);

		if(!users.isEmpty()) {
			UserRepresentation user2 = users.get(0);

			fillRoles(usersResource, user2);

			return user2;
		}
		return null;
	}

	private static void fillRoles(UsersResource usersResource, UserRepresentation user) {

		String kcUserId = user.getId();
		UserResource ur = usersResource.get(kcUserId);
		RoleMappingResource rmr = ur.roles();
		MappingsRepresentation mr = rmr.getAll();
		List<RoleRepresentation> rolesMappingList = mr.getRealmMappings();
		List<String> roleList = new ArrayList<>();
		for (RoleRepresentation roleMapping : rolesMappingList) {
			String roleName = roleMapping.getName();
			try {
				Usuario.Role role = Usuario.Role.valueOf(roleName.toUpperCase());
				roleList.add(roleName);
			}
			catch (IllegalArgumentException e) { }
		}
		user.setRealmRoles(roleList);
	}

	private void fillUserRepresentation(Usuario usuario, UserRepresentation user) {

		user.setEnabled(true);
		String login = usuario.getLogin();
		user.setUsername(login);
		String email = usuario.getEmail();
		user.setEmail(email);

		String nome = usuario.getNome();
		String primeiroNome = nome.substring(0, nome.indexOf(" "));
		String sobrenomeNome = nome.replace(primeiroNome, "").trim();

		user.setFirstName(primeiroNome);
		user.setLastName(sobrenomeNome);

		CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
		credentialRepresentation.setTemporary(true);
		credentialRepresentation.setType(CredentialRepresentation.PASSWORD);
		credentialRepresentation.setValue(login);
		user.setCredentials(Collections.singletonList(credentialRepresentation));

		String rolesJson = usuario.getRoles();
		List roles = DummyUtils.jsonToObject(rolesJson, List.class);
		user.setRealmRoles(roles);
	}

	public void atualizar(Usuario usuario) {

		String kcUserId = usuario.getKeyCloakId();
		RealmResource realmResource = getRealmResource();
		UsersResource usersResource = realmResource.users();
		UserResource ur = usersResource.get(kcUserId);
		UserRepresentation userRepresentation = ur.toRepresentation();

		fillUserRepresentation(usuario, userRepresentation);

		ur.update(userRepresentation);
	}

	public void excluir(Usuario usuario) {

		String kcUserId = usuario.getKeyCloakId();
		RealmResource realmResource = getRealmResource();
		UsersResource usersResource = realmResource.users();

		Response response = usersResource.delete(kcUserId);

		int status = response.getStatus();
		if(status < 200 || status >= 300) {
			String error = response.readEntity(String.class);
			Response.StatusType statusInfo = response.getStatusInfo();
			throw new RuntimeException("erro ao excluir usuário no keycloak: " + statusInfo + " - " + error);
		}
	}
}
