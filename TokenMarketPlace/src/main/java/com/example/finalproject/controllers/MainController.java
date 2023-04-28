package com.example.finalproject.controllers;

import com.example.finalproject.enumm.RarityRank;
import com.example.finalproject.models.Token;
import com.example.finalproject.models.User;
import com.example.finalproject.services.CartService;
import com.example.finalproject.services.CollectionService;
import com.example.finalproject.services.TokenService;
import com.example.finalproject.services.UserService;
import com.example.finalproject.util.UserValidator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.example.finalproject.controllers.UserController.getCurrentCartIds;
import static com.example.finalproject.controllers.UserController.getCurrentUser;
import static com.example.finalproject.controllers.UserController.getCurrentUserId;

@Controller
public class MainController {
    private final UserValidator userValidator;
    private final UserService userService;
    private final TokenService tokenService;
    private final CartService cartService;
    private final CollectionService collectionService;

    @Value("${upload.path}")
    private String uploadPath;

    public MainController(UserValidator userValidator, UserService userService,
                          TokenService tokenService, CartService cartService,
                          CollectionService collectionService) {
        this.userValidator = userValidator;
        this.userService = userService;
        this.tokenService = tokenService;
        this.cartService = cartService;
        this.collectionService = collectionService;
    }

    @GetMapping("/user")
    public String user(Model model) {
        String role = getCurrentUser().getRole().getName();
        if (role.equals("ROLE_ADMIN")){
            return "redirect:/admin";
        }
        model.addAttribute("current_user", getCurrentUser());
        return "redirect:/user/token/owned";
    }

    @GetMapping("/register")
    public String registration(@ModelAttribute("user") User user){
        return "register";
    }

    @PostMapping("/register")
    public String resultRegistration(@ModelAttribute("user") @Valid User user,
                                     @RequestParam("file") MultipartFile imageFile,
                                     BindingResult bindingResult) throws IOException {
        userValidator.validate(user, bindingResult);
        if (bindingResult.hasErrors()){
            return "register";
        }
        String uuidFile = UUID.randomUUID().toString();
        String fileName = "user_avatar_" + uuidFile + "." + imageFile.getOriginalFilename();
        imageFile.transferTo(new File(uploadPath + "/" + fileName));
        userService.register(user, fileName);
        return "redirect:/user";
    }

    @GetMapping("/index")
    public String viewTokens(@RequestParam(value = "keyword", required = false) String keyword,
                             @RequestParam(value = "min", defaultValue = "0") String min,
                             @RequestParam(value = "max", defaultValue = "1000") String max,
                             @RequestParam(value = "collection", defaultValue = "") String collection,
                             @RequestParam(value = "rarity", defaultValue = "") String rarity,
                             @RequestParam(value = "sorting", defaultValue = "default") String sorting,
                             Model model) {
        model.addAttribute("current_user_id", getCurrentUserId());
        model.addAttribute("current_user", getCurrentUser());
        model.addAttribute("cart_ids", getCurrentCartIds(getCurrentUserId(), cartService));

        model.addAttribute("collections", collectionService.getAllCollection());

        List<Sort.Order> orders = new ArrayList<>();
        List<Token> tokens;

        if (Float.parseFloat(min) > 0) {
            model.addAttribute("min", min);
        }
        if (Float.parseFloat(max) < 1000) {
            model.addAttribute("max", max);
        }

        //sort
        if (!"default".equals(sorting)) {
            model.addAttribute("sorting", sorting);
            if ("asc_title".equals(sorting)) {
                orders.add(new Sort.Order(Sort.Direction.ASC, "title"));
            }else if ("asc_price".equals(sorting)) {
                orders.add(new Sort.Order(Sort.Direction.ASC, "price"));
            } else if ("desc_price".equals(sorting)) {
                orders.add(new Sort.Order(Sort.Direction.DESC, "price"));
            } else if ("asc_rarity".equals(sorting)) {
                orders.add(new Sort.Order(Sort.Direction.ASC, "rarityRank"));
            } else if ("desc_rarity".equals(sorting)) {
                orders.add(new Sort.Order(Sort.Direction.DESC, "rarityRank"));
            } else if ("desc_time".equals(sorting)) {
                orders.add(new Sort.Order(Sort.Direction.DESC, "dateTime"));
            }
        }

        //search and filter
        if (keyword != null && !keyword.isEmpty()) {
            model.addAttribute("keyword", keyword);
            if (!collection.isEmpty()) {
                if (!rarity.isEmpty()) {
                    tokens = tokenService.getTokensByTitleAndCollectionAndRarityRank(
                            Float.parseFloat(min), Float.parseFloat(max), keyword,
                            Integer.parseInt(collection), RarityRank.valueOf(rarity), Sort.by(orders));
                } else {
                    tokens = tokenService.getTokensByTitleAndCollection(Float.parseFloat(min),
                            Float.parseFloat(max), keyword, Integer.parseInt(collection), Sort.by(orders));
                }
            } else {
                if (!rarity.isEmpty()) {
                    tokens = tokenService.getTokensByTitleAndRarityRank(
                            Float.parseFloat(min), Float.parseFloat(max),
                            keyword, RarityRank.valueOf(rarity), Sort.by(orders));
                } else {
                    tokens = tokenService.getTokensByTitle(Float.parseFloat(min),
                            Float.parseFloat(max), keyword, Sort.by(orders));
                }
            }
        } else {
            if (!collection.isEmpty()) {
                if (!rarity.isEmpty()) {
                    tokens = tokenService.getTokensByCollectionAndRarityRank(Float.parseFloat(min),
                            Float.parseFloat(max), Integer.parseInt(collection),
                            RarityRank.valueOf(rarity), Sort.by(orders));
                } else {
                    tokens = tokenService.getTokensByCollection(Float.parseFloat(min),
                            Float.parseFloat(max), Integer.parseInt(collection), Sort.by(orders));
                }
            } else {
                if (!rarity.isEmpty()) {
                    tokens = tokenService.getTokensByRarityRank(Float.parseFloat(min),
                            Float.parseFloat(max), RarityRank.valueOf(rarity), Sort.by(orders));
                } else {
                    tokens = tokenService.getTokensByPrice(Float.parseFloat(min), Float.parseFloat(max), Sort.by(orders));
                }
            }
        }
        model.addAttribute("tokens", tokens);
        return "/index";
    }
}