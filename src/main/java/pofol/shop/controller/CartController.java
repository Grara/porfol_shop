package pofol.shop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pofol.shop.domain.Cart;
import pofol.shop.domain.Item;
import pofol.shop.domain.Member;
import pofol.shop.service.CartService;
import pofol.shop.service.ItemService;
import pofol.shop.service.MemberService;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    private final MemberService memberService;
    private final ItemService itemService;

    @GetMapping("/cart")
    public String cart(Model model, Principal principal){
        Member findMember = memberService.findOneByName(principal.getName());
        List<Cart> carts = cartService.findListByMemberFetchItem(findMember);
        model.addAttribute("carts", carts);
        return "carts/cartList";
    }

    @PostMapping("/cart/new")
    public String addItemToCart(@RequestParam("itemId")Long itemId,
                                @RequestParam("count")Integer count,
                                Principal principal,
                                Model model){

        Member member = memberService.findOneByName(principal.getName());
        Item item = itemService.findOne(itemId);
        Optional<Long> existingCartId = cartService.duplicateCheck(member, item);


        //장바구니에 현재 추가하는 아이템과 같은 아이템이 없으면 새로 추가
        if(!existingCartId.isPresent()){
            Cart newCart = new Cart(member, item, count);
            cartService.add(newCart);
        }
        //있으면 기존 장바구니 아이템에 수량만 추가
        else{
            Cart existingCart = cartService.findOne(existingCartId.get()).get();
            existingCart.addCount(count);
            cartService.add(existingCart);
        }

        List<Cart> carts = cartService.findListByMemberFetchItem(member);
        model.addAttribute("carts", carts);

        return "carts/cartList";
    }

}
