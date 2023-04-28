package com.example.finalproject.controllers;

import com.example.finalproject.enumm.OrderStatus;
import com.example.finalproject.models.CartItem;
import com.example.finalproject.models.Collection;
import com.example.finalproject.models.Order;
import com.example.finalproject.models.Token;
import com.example.finalproject.models.User;
import com.example.finalproject.security.UserDetails;
import com.example.finalproject.services.CartService;
import com.example.finalproject.services.CollectionService;
import com.example.finalproject.services.OrderService;
import com.example.finalproject.services.TokenService;
import com.example.finalproject.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
public class UserController {
    private final TokenService tokenService;
    private final CollectionService collectionService;
    private final UserService userService;
    private final CartService cartService;
    private final OrderService orderService;

    @Value("${upload.path}")
    private String uploadPath;

    public UserController(TokenService tokenService, CollectionService collectionService,
                          UserService userService, CartService cartService,
                          OrderService orderService) {
        this.tokenService = tokenService;
        this.cartService = cartService;
        this.userService = userService;
        this.orderService = orderService;
        this.collectionService = collectionService;
    }

    @GetMapping("/user/token/owned")
    public String viewOwnedTokens(Model model) {
        model.addAttribute("current_user_id", getCurrentUserId());
        model.addAttribute("current_user", getCurrentUser());
        model.addAttribute("tokens", tokenService.getTokensByOwnerId(getCurrentUserId()));
        model.addAttribute("cart_ids", getCurrentCartIds(getCurrentUserId(), cartService));
        model.addAttribute("collections",
                collectionService.getCollectionByCreatorId(getCurrentUserId()));
        return "/user/token/owned";
    }

    @GetMapping("/user/token/created")
    public String viewCreatedTokens(Model model) {
        User user = getCurrentUser();
        int userId = getCurrentUserId();
        model.addAttribute("current_user_id", userId);
        model.addAttribute("current_user", user);
        model.addAttribute("tokens",
                tokenService.getTokensByCreatorId(userId));
        model.addAttribute("cart_ids", getCurrentCartIds(userId, cartService));
        model.addAttribute("collections",
                collectionService.getCollectionByCreatorId(userId));
        return "/user/token/created";
    }

    @GetMapping("/user/token/new")
    public String addToken(Model model) {
        User user = getCurrentUser();
        int userId = getCurrentUserId();
        model.addAttribute("token", new Token());
        model.addAttribute("collections",
                collectionService.getCollectionByCreatorId(userId));
        model.addAttribute("current_user_id", userId);
        model.addAttribute("current_user", user);
        return "/user/token/add";
    }

    @PostMapping("/user/token/new")
    public String addToken(@ModelAttribute("token") @Valid Token token,
                           @RequestParam("file") MultipartFile imageFile,
                           @RequestParam("collection") int collectionId,
                           BindingResult bindingResult, Model model) throws IOException {
        User user = getCurrentUser();
        int userId = getCurrentUserId();
        if (bindingResult.hasErrors()){
            model.addAttribute("collections", collectionService.getCollectionByCreatorId(userId));
            model.addAttribute("current_user_id", userId);
            model.addAttribute("current_user", user);
            return "/user/token/add";
        }
        String uuidFile = UUID.randomUUID().toString();
        String fileName = "token_image_" + uuidFile + "." + imageFile.getOriginalFilename();
        imageFile.transferTo(new File(uploadPath + "/" + fileName));
        tokenService.addToken(token, userService.getUserById(userId),
                collectionService.getCollectionById(collectionId), fileName);
        return "redirect:/user/token/created";

    }

    @GetMapping("/user/token/delete/{id}")
    public String deleteProduct(@PathVariable("id") int id) {
        Collection collection = tokenService.getTokenById(id).getCollection();
        tokenService.deleteToken(id, collection);
        return "redirect:/user/token/owned";
    }

    @GetMapping("/token/info/{id}")
    public String infoToken(@PathVariable("id") int id, Model model) {
        User user = getCurrentUser();
        int userId = getCurrentUserId();
        model.addAttribute("current_user_id", userId);
        model.addAttribute("current_user", user);
        model.addAttribute("token", tokenService.getTokenById(id));
        model.addAttribute("cart_ids", getCurrentCartIds(userId, cartService));
        return "/token/token";
    }

    @GetMapping("/token/sell/{id}")
    public String sellToken(@PathVariable("id") int id, Model model) {
        model.addAttribute("token", tokenService.getTokenById(id));
        model.addAttribute("current_user", getCurrentUser());
        return "/user/token/edit";
    }

    @PostMapping("/token/sell/{id}")
    public String sellToken(@ModelAttribute("token") @Valid Token token,
                            BindingResult bindingResult,
                            @PathVariable("id") int id) {
        if (bindingResult.hasErrors()) {
            return "/user/token/edit";
        }
        tokenService.sellToken(id, token);
        return "redirect:/user/token/owned";
    }

    @GetMapping("/token/withdraw/{id}")
    public String withdrawToken(@PathVariable("id") int id) {
        tokenService.withdrawToken(id, tokenService.getTokenById(id));
        return "redirect:/user/token/owned";
    }

    @GetMapping("/user/collections")
    public String viewCollections(Model model) {
        User user = getCurrentUser();
        int userId = getCurrentUserId();
        model.addAttribute("current_user_id", userId);
        model.addAttribute("current_user", user);
        model.addAttribute("collections",
                collectionService.getCollectionByCreatorId(userId));
        return "/user/collection/all";
    }

    @GetMapping("/user/collection/new")
    public String addCollection(Model model) {
        model.addAttribute("current_user_id", getCurrentUserId());
        model.addAttribute("current_user", getCurrentUser());
        model.addAttribute("collection", new Collection());
        return "/user/collection/add";
    }

    @PostMapping("/user/collection/new")
    public String addCollection(@ModelAttribute("collection") @Valid Collection collection,
                                @RequestParam("file") MultipartFile imageFile,
                                BindingResult bindingResult, Model model) throws IOException {
        int userId = getCurrentUserId();
        User user = getCurrentUser();
        if (bindingResult.hasErrors()){
            model.addAttribute("current_user_id", userId);
            model.addAttribute("current_user", user);
            return "/user/collection/add";
        }
        String uuidFile = UUID.randomUUID().toString();
        String fileName = "collection_banner_" + uuidFile + "." + imageFile.getOriginalFilename();
        imageFile.transferTo(new File(uploadPath + "/" + fileName));
        collectionService.addCollection(collection, userService.getUserById(userId), fileName);
        return "redirect:/user/collections"; //todo create single upload method
    }

    @GetMapping("/collection/info/{id}")
    public String infoCollection(@PathVariable("id") int id, Model model) {
        Collection collection = collectionService.getCollectionById(id);
        model.addAttribute("current_user_id", getCurrentUserId());
        model.addAttribute("current_user", getCurrentUser());
        model.addAttribute("collection", collection);
        model.addAttribute("tokens", collection.getTokenList());
        model.addAttribute("cart_ids", getCurrentCartIds(getCurrentUserId(), cartService));
        return "/token/collection";
    }

    @GetMapping("/user/cart/add/{id}")
    public String addTokenToCart(@PathVariable("id") int id) {
        CartItem cartItem = new CartItem(getCurrentUserId(), id);
        cartService.addToCart(cartItem);
        return "redirect:/user/cart";
    }

    @GetMapping("/user/cart")
    public String cart(Model model){
        User user = getCurrentUser();
        int userId = getCurrentUserId();
        List<CartItem> cartList = cartService.getCartItemsByUserId(userId);
        List<Token> tokenList = new ArrayList<>();
        Float total = 0f;
        for (CartItem item: cartList) {
            Token token = tokenService.getTokenById(item.getTokenId());
            tokenList.add(token);
            total += token.getPrice();
        }
        model.addAttribute("current_user", user);
        model.addAttribute("total", String.format("%.2f", total));
        model.addAttribute("tokens", tokenList);
        model.addAttribute("cart_ids", getCurrentCartIds(userId, cartService));
        return "/user/cart";
    }

    @GetMapping("/user/cart/delete/{id}")
    public String deleteTokenFromUserCart(@PathVariable("id") int id) {
        int userId = getCurrentUserId();
        List<CartItem> cartList = cartService.getCartItemsByUserId(userId);
        for (CartItem item: cartList) {
            if (item.getTokenId() == id) {
                cartService.deleteCartItem(item.getId());
                break;
            }
        }
        return "redirect:/user/cart";
    }

    @GetMapping("/user/orders")
    public String orderByUser(Model model){
        User user = getCurrentUser();
        int userId = getCurrentUserId();
        List<Order> orderList = orderService.getOrderByUserId(userId);
        model.addAttribute("orders", orderList);
        model.addAttribute("current_user", user);
        model.addAttribute("cart_ids", getCurrentCartIds(userId, cartService));
        model.addAttribute("collections",
                collectionService.getCollectionByCreatorId(userId));
        return "/user/order/all";
    }

    @GetMapping("/user/order/create")
    public String buyToken() {
        int userId = getCurrentUserId();
        User user = getCurrentUser();
        List<CartItem> cartList = cartService.getCartItemsByUserId(userId);
        int order_item = 0;
        String uuid = UUID.randomUUID().toString();
        for (CartItem item: cartList) {
            int tokenId = item.getTokenId();
            Token token = tokenService.getTokenById(tokenId);
            Order order = new Order(uuid + (++order_item), token, user,
                    token.getPrice(), OrderStatus.PENDING);
            orderService.addOrder(order);
            tokenService.buyToken(tokenId, token, userId);
            cartService.deleteTokenFromAllCarts(tokenId);
        }
        return "redirect:/user/orders";
    }

    protected static User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof AnonymousAuthenticationToken) {
            return null;
        }
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userDetails.getUser();
    }

    protected static int getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof AnonymousAuthenticationToken) {
            return -1;
        }
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userDetails.getUser().getId();
    }

    protected static List<Integer> getCurrentCartIds(int userId, CartService cartService) {
        return cartService.getCartItemsByUserId(userId)
                .stream().map(CartItem::getTokenId).collect(Collectors.toList());
    }
}