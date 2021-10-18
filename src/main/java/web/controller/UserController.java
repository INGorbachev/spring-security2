package web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import web.model.User;
import web.service.RoleService;
import web.service.UserService;


@Controller
@RequestMapping
public class UserController {

	private UserService userService;

	private RoleService roleService;

	@Autowired
	public UserController(UserService userService, RoleService roleService) {
		this.userService = userService;
		this.roleService = roleService;
	}

	@RequestMapping(value = "/login", method = RequestMethod.GET)
    public String loginPage() {
        return "login";
    }

	@GetMapping("/admin")
	public String getUsers(Model model){
		model.addAttribute("users", userService.listUsers());
		model.addAttribute("roles", roleService.getRoles());
		return "admin";
	}

	@GetMapping("/user")
	public String getUser(Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		model.addAttribute("user",  userService.getUserByFirstName(auth.getName()));
		return "user";
	}

	@PostMapping(value = "/admin/add")
	public String addUser(@ModelAttribute User user, @RequestParam(value = "role") Long[] rolesId){
		userService.add(user, rolesId);
		return "redirect:/admin";
	}

	@PostMapping(value = "/admin/delete")
	public String deleteUser(@ModelAttribute("id") Long id){

		if (!userService.checkUserById(id)) {
			return "redirect:/admin";
		}

		userService.remove(id);
		return "redirect:/admin";
	}

	@PostMapping(value = "/admin/update")
	public String updateUser(@ModelAttribute("user") User user, @RequestParam(value = "role", required = false) Long[] rolesId){
		userService.update(user, rolesId);
		return "redirect:/admin";
	}

	@GetMapping(value = "/admin/update")
	public String updateUser(@ModelAttribute("id") Long id, Model model){

		if (userService.checkUserById(id)) {
			return "redirect:/admin";
		}

		User user = userService.getUserById(id);

		model.addAttribute("roles", roleService.getRoles());
		model.addAttribute("user",user);
		return "update";
	}
}