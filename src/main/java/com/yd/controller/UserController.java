package com.yd.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.yd.common.util.AppsUtil;
import com.yd.persistence.model.ModelUser;
import com.yd.persistence.repository.DocstoreRepository;
import com.yd.persistence.repository.RoleRepository;
import com.yd.persistence.repository.UserDocstoreLinkRepository;
import com.yd.persistence.repository.UserRepository;
import com.yd.persistence.repository.UserRoleRepository;
import com.yd.persistence.repository.model.Docstore;
import com.yd.persistence.repository.model.Role;
import com.yd.persistence.repository.model.User;
import com.yd.persistence.repository.model.UserDocstoreLink;
import com.yd.persistence.repository.model.UserRole;
import com.yd.security.AppsUserDetails;

@Controller
public class UserController {

	private static final String USERS = "users";
	private static final String ALERT_SUCCESS = "alertSuccess";
	private static final String VIEW = "/view";
	private static final String CANNOT_SHOW_USER_DOES_NOT_EXIST = "Cannot show user, does not exist or user not allowed to view";
	private static final String ALERT_WARNING = "alertWarning";
	private static final String ACTION = "action";
	private static final String USER = "user";
	private static final String ROLE_LIST = "roleList";
	private static final String USERMANAGEMENT = "usermanagement";
	private static final String DOCSTORE_LIST = "docstoreList";

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private UserRoleRepository userRoleRepository;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private Environment env;
	@Autowired
	private DocstoreRepository docstoreRepository;
	@Autowired
	private UserDocstoreLinkRepository userDocstoreLinkRepository;

	private int countPaging(int start, long maxPage) {
		float resultPage = (float) (Math.ceil((double) start / 10) - 1);

		if (resultPage > maxPage - 1)
			return (int) maxPage - 1;

		return (int) resultPage;
	}

	private Pageable createPagingForUserManagement(long totalUser, int start) {
		long maxPage = (long) Math.ceil((double) totalUser / 10);
		int page;
		int forStart = start;
		if (forStart <= 0) {
			forStart = 10;
			page = countPaging(forStart, maxPage);
		} else {
			page = countPaging(forStart, maxPage);
		}

		return new PageRequest(page, 10, new Sort(Sort.Direction.ASC,
				"username"));
	}

	@RequestMapping("/users")
	public String users(
			Map<String, Object> model,
			@RequestParam(required = false, defaultValue = "10") int start,
			@RequestParam(value = "searchParam", required = false) String searchParam,
			@RequestParam(value = "selectedProducts", required = false) String selectedProducts,
			@RequestParam(value = "selectedUsers", required = false) String selectedUsers,
			@RequestParam(value = "action", required = false) String action) {
		long totalUser = userRepository.count();

		if (totalUser > 0) {
			Page<User> userPages;
			userPages = userRepository.findAll(createPagingForUserManagement(
					totalUser, start));

			model.put(USERS, userPages.getContent());
			model.put("searchParam", searchParam);
		} else {
			model.put(USERS, new ArrayList<User>());
		}

		model.put("start", start);
		model.put("count", totalUser);
		return USERS;
	}

	@RequestMapping("/users/{username}")
	public String users(Map<String, Object> model,
			@PathVariable("username") String username,
			@RequestParam(required = false, defaultValue = "10") int start) {
		List<User> users = new ArrayList<>();
		if (StringUtils.isNotBlank(username))
			users = userRepository.findByUsernameStartingWith(username);

		long totalUser = users.isEmpty() ? 0 : users.size();

		if (totalUser > 0) {
			Page<User> userPages = userRepository.findByUsernameStartingWith(
					username, createPagingForUserManagement(totalUser, start));
			model.put(USERS, userPages.getContent());
		} else {
			model.put(USERS, new ArrayList<User>());
		}

		model.put("start", start);
		model.put("username", username);
		model.put("count", totalUser);
		return USERS;
	}

	@RequestMapping(value = "/user/add", method = RequestMethod.GET)
	public String addUser(Map<String, Object> model) {
		User newUser = new User();
		newUser.setUuid(UUID.randomUUID().toString());

		ModelUser user = new ModelUser(newUser, "add");
		List<Role> roles = roleRepository.findAll();

		List<Docstore> docstores = docstoreRepository
				.findAllByOrderByReferenceAsc();

		model.put(USER, user);
		model.put(ACTION, "add");
		model.put(USERMANAGEMENT, true);
		model.put(ROLE_LIST, roles);
		model.put(DOCSTORE_LIST, docstores);
		return USER;
	}

	@RequestMapping(value = "/user", method = RequestMethod.POST)
	public String addUser(Map<String, Object> model,
			@Valid @ModelAttribute ModelUser user, BindingResult result,
			RedirectAttributes redirect) throws IOException {
		String action = user.getAction();
		checkUserValidation(user, result);
		if (result != null && result.hasErrors()) {
			if (!"add".equals(action)) {
				model.put(ACTION, "edit");
			} else {
				model.put(ACTION, action);
			}
			List<Role> roles = roleRepository.findAll();
			List<Docstore> docstores = docstoreRepository
					.findAllByOrderByReferenceAsc();

			model.put(USER, user);
			model.put("result", result);
			model.put(ROLE_LIST, roles);
			model.put(DOCSTORE_LIST, docstores);
			addUserManagement(model);
			return USER;
		} else {
			User newUser = userRepository.findOne(user.getUuid());
			// if user does not exist, then create a new one
			if (newUser == null) {
				if (userRepository.findByUsername(user.getUsername()) != null) {
					addUserManagement(model);
					if ("add".equals(action)) {
						model.put(ACTION, "add");
						model.put(USER, user);
						model.put("result", result);
						model.put(ALERT_WARNING, "Account name already exist");
						addUserManagement(model);
						return USER;
					} else {
						model.put(ALERT_WARNING,
								"Cannot update user, user exist or you are not allowed to update user");
					}
					return USER;
				}
				newUser = new User();
				newUser.setUsername(user.getUsername());
			}
			newUser.setEmailAddress(user.getEmailAddress());
			newUser.setFullName(user.getFullName());
			newUser.setMobileNumber(user.getMobileNumber());
			newUser.setPassword("$2a$10$E6Bpa4EsexSuJclB.87ziupCcY6xBIq0baVYxUwA0.6AtQlO/qGNq");
			newUser.setEnabled(true);
			newUser.setJobTitle(user.getJobTitle());

			userRepository.saveAndFlush(newUser);

			// if (hasUserManagementAuthority()) {
			updateUserDocstoreAndRole(user, newUser);
			reloadDocstoreAndRoleSession();
			// }

			redirect.addFlashAttribute(ALERT_SUCCESS,
					"User profile has been updated.");

			return "redirect:user/" + newUser.getUuid() + VIEW;
		}
	}

	@RequestMapping(value = "/user/{userId}/view", method = RequestMethod.GET)
	public String viewUser(Map<String, Object> model,
			@PathVariable("userId") String userId) {
		ModelUser user = getUserProfile(userId, "view");

		model.put(ACTION, "view");
		addUserManagement(model);

		if (user == null) {
			model.put(ALERT_WARNING, CANNOT_SHOW_USER_DOES_NOT_EXIST);
			return USER;
		}
		Set<UserRole> roleNames = user.getUserRoles();
		user.setUserRoles(roleNames);
		user.setDocstores(user.getDocstores());
		model.put(USER, user);
		return USER;
	}

	@RequestMapping(value = "/user/{userId}/edit", method = RequestMethod.GET)
	public String editUserProfile(Map<String, Object> model,
			@PathVariable("userId") String userId) {
		model.put(ACTION, "edit");
		addUserManagement(model);
		if (AppsUtil.isSwitchedUser()) {
			model.put(ALERT_WARNING, CANNOT_SHOW_USER_DOES_NOT_EXIST);
			return USER;
		}

		ModelUser user = getUserProfile(userId, "edit");

		if (user == null) {
			model.put(ALERT_WARNING, CANNOT_SHOW_USER_DOES_NOT_EXIST);
			return USER;
		}
		List<Role> roles = roleRepository.findAll();
		List<Docstore> docstores = docstoreRepository
				.findAllByOrderByReferenceAsc();
		model.put(DOCSTORE_LIST, docstores);
		model.put(ROLE_LIST, roles);
		model.put(USER, user);
		return USER;
	}

	@RequestMapping(value = "/user/{userId}/delete", method = RequestMethod.POST)
	public String deleteUser(@PathVariable("userId") String userId,
			RedirectAttributes redirect) {

		User user = userRepository.findOne(userId);

		String username;

		username = user.getUsername();
		List<UserRole> deleteUserRole = userRoleRepository
				.findByUser(userRepository.findOne(user.getUuid()));
		for (UserRole ur : deleteUserRole) {
			userRoleRepository.delete(ur);
		}

		List<UserDocstoreLink> deleteUserDocstoreLink = userDocstoreLinkRepository
				.findByUser(userRepository.findOne(user.getUuid()));
		for (UserDocstoreLink udl : deleteUserDocstoreLink) {
			userDocstoreLinkRepository.delete(udl);
		}

		userRepository.delete(user);

		redirect.addFlashAttribute(ALERT_WARNING, "User " + username
				+ " has been deleted");

		return "redirect:/users";

	}

	private void updateUserDocstoreAndRole(ModelUser user, User newUser) {
		Set<UserDocstoreLink> userDocstoreLink = new HashSet<>();
		for (String uuid : user.getDocstores()) {
			Docstore docstore = docstoreRepository.findOne(uuid);
			UserDocstoreLink udl = new UserDocstoreLink();
			udl.setDocstore(docstore);
			udl.setUser(newUser);
			userDocstoreLink.add(udl);
		}
		List<UserDocstoreLink> deleteUserDocstoreLink = userDocstoreLinkRepository
				.findByUser(userRepository.findOne(user.getUuid()));
		for (UserDocstoreLink udl : deleteUserDocstoreLink) {
			userDocstoreLinkRepository.delete(udl);
		}
		Set<UserRole> userRoles = new HashSet<>();
		if (user.getRoleNames() != null) {
			for (String roleName : user.getRoleNames()) {
				UserRole ur = new UserRole();
				ur.setRole(roleRepository.findByRoleName(roleName));
				ur.setUser(newUser);
				userRoles.add(ur);
			}
		}
		List<UserRole> deleteUserRole = userRoleRepository
				.findByUser(userRepository.findOne(user.getUuid()));
		for (UserRole ur : deleteUserRole) {
			userRoleRepository.delete(ur);
		}
		userRoleRepository.save(userRoles);
		userRoleRepository.flush();
		userDocstoreLinkRepository.save(userDocstoreLink);
		userDocstoreLinkRepository.flush();
	}

	private void reloadDocstoreAndRoleSession() {
		AppsUserDetails principal = AppsUtil.getPrincipal();

		List<UserDocstoreLink> userDocstoreLinks = userDocstoreLinkRepository
				.findByUser(principal.getUser());

		Set<Docstore> docstores = new HashSet<>();
		for (UserDocstoreLink userDocstoreLink : userDocstoreLinks) {
			docstores.add(userDocstoreLink.getDocstore());
		}

		principal.setDocstores(docstores);

		User user = userRepository.getOne(principal.getUser().getUuid());
		if (!user.hasDocstore(principal.getDocstore())) {
			Docstore docstore = docstores.iterator().next();
			principal.setDocstore(docstore);

			UserDocstoreLink udl = user.getDocstore(docstore);
			udl.setLastSelectedTime(new Date());
			userDocstoreLinkRepository.saveAndFlush(udl);
		}

		Set<GrantedAuthority> authorities = new HashSet<>();
		for (UserRole userRole : user.getUserRole()) {
			authorities.add(new SimpleGrantedAuthority("ROLE_"
					+ userRole.getRole().getName()));
		}
		principal.setAuthorities(authorities);
		Authentication authentication = new UsernamePasswordAuthenticationToken(
				principal, AppsUtil.getAuthentication().getCredentials(),
				authorities);
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}

	private ModelUser getUserProfile(String userId, String action) {
		String currentUuid = AppsUtil.getActiveUser().getUuid();

		// if (hasUserManagementAuthority()) {
		if (StringUtils.isNotBlank(userId)) {
			return new ModelUser(userRepository.findOne(userId), action);
		} else {
			return new ModelUser(userRepository.findOne(currentUuid), action);
		}
		// }

		// if (userId == null)
		// return new ModelUser(userRepository.findOne(currentUuid), action);
		//
		// if (StringUtils.equals(userId, currentUuid))
		// return new ModelUser(userRepository.findOne(currentUuid), action);
		// else
		// return null;

	}

	protected void checkUserValidation(ModelUser user, BindingResult result) {
		if (user.getUsername().contains(" ") && "add".equals(user.getAction())) {
			result.addError(new ObjectError("userProfiles",
					"Username couldn't contained space character"));
		}
	}

	private void addUserManagement(Map<String, Object> model) {
		// if (hasUserManagementAuthority())
		model.put(USERMANAGEMENT, true);
		// else
		// model.put(USERMANAGEMENT, false);
	}

}