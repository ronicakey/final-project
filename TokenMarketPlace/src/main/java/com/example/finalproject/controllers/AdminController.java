package com.example.finalproject.controllers;

import com.example.finalproject.enumm.TokenStatus;
import com.example.finalproject.models.Order;
import com.example.finalproject.models.Token;
import com.example.finalproject.models.User;
import com.example.finalproject.services.OrderService;
import com.example.finalproject.services.RoleService;
import com.example.finalproject.services.TokenService;
import com.example.finalproject.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final RoleService roleService;
    private final TokenService tokenService;
    private final OrderService orderService;

    public AdminController(UserService userService, RoleService roleService,
                           TokenService tokenService, OrderService orderService) {
        this.userService = userService;
        this.roleService = roleService;
        this.tokenService = tokenService;
        this.orderService = orderService;
    }

    @GetMapping("")
    public String admin() {
        return "redirect:/admin/tokens";
    }

    @GetMapping("/tokens")
    public String viewProducts(Model model) {
        model.addAttribute("tokens", tokenService.getAllToken());
        return "/admin/token/all";
    }

    @GetMapping("/token/block/{id}")
    public String blockToken(@PathVariable("id") int id) {
        tokenService.updateTokenStatus(id, tokenService.getTokenById(id), TokenStatus.BLOCKED);
        return "redirect:/admin/tokens";
    }

    @GetMapping("/token/approve/{id}")
    public String approveToken(@PathVariable("id") int id) {
        tokenService.updateTokenStatus(id, tokenService.getTokenById(id), TokenStatus.NOT_FOR_SALE);
        return "redirect:/admin/tokens";
    }

    @GetMapping("/users")
    public String viewUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "/admin/user/all";
    }

    @GetMapping("/user/edit/{id}")
    public String promoteUserToCreator(@PathVariable("id") int id) {
        userService.updateUserRole(id, userService.getUserById(id), roleService.getCreatorRole());
        return "redirect:/admin/users";
    }

    @GetMapping("/orders")
    public String viewOrders(@RequestParam(value = "keyword", required = false) String keyword, Model model) {
        List<Order> orders;
        if (keyword == null) {
            orders = orderService.getAllOrders();
        } else {
            orders = orderService.getOrderByNumberEnding(keyword);
            model.addAttribute("keyword", keyword);
        }
        model.addAttribute("orders", orders);
        return "/admin/order/all";
    }

    @GetMapping("/order/block/{id}")
    public String blockOrder(@PathVariable("id") int id) {
        Order order = orderService.getOrderById(id);
        Token token = order.getToken();
        orderService.blockOrder(id, order);
        tokenService.rejectToken(token.getId(), token);
        return "redirect:/admin/orders";
    }

    @GetMapping("/order/approve/{id}")
    public String approveOrder(@PathVariable("id") int id) {
        Order order = orderService.getOrderById(id);
        Token token = order.getToken();
        User user = userService.getUserById(token.getNewOwnerId());
        orderService.approveOrder(id, order);
        tokenService.transferToken(token.getId(), token, user);
        return "redirect:/admin/orders";
    }
}