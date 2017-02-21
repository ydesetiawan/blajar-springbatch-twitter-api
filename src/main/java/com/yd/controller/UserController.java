package com.yd.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.yd.persistence.model.ModelUser;
import com.yd.persistence.repository.RoleRepository;
import com.yd.persistence.repository.UserRepository;
import com.yd.persistence.repository.model.Role;
import com.yd.persistence.repository.model.User;

@Controller
public class UserController {

	private static final String USERS = "users";
	private static final String FALSE = "false";
	private static final String ALERT_SUCCESS = "alertSuccess";
	private static final String VIEW = "/view";
	private static final String REDIRECT_USERPROFILE = "redirect:/userprofile/";
	private static final String CANNOT_SHOW_USER_DOES_NOT_EXIST = "Cannot show user, does not exist or user not allowed to view";
	private static final String ALERT_WARNING = "alertWarning";
	private static final String ACTION = "action";
	private static final String USER = "user";
	private static final String ROLE_LIST = "roleList";
	private static final String USERMANAGEMENT = "usermanagement";
	private static final String ACC_DISABLE_ATTR = "nsAccountLock";
	private static final String ACC_UNLOCK_TIME_ATTR = "accountUnlockTime";
	private static final String RESET_TIMESTAMP = "19700101000000Z";

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private Environment env;

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

		model.put(USER, user);
		model.put(ACTION, "add");
		model.put(USERMANAGEMENT, true);
		model.put(ROLE_LIST, roles);
		return USER;
	}
}