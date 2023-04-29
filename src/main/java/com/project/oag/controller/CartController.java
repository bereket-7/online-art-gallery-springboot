package com.project.oag.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.oag.common.ApiResponse;
import com.project.oag.controller.dto.AddToCartDto;
import com.project.oag.controller.dto.CartDto;
import com.project.oag.entity.Artwork;
import com.project.oag.entity.User;
import com.project.oag.exceptions.AuthenticationFailException;
import com.project.oag.exceptions.CartItemNotExistException;
import com.project.oag.service.ArtworkService;
import com.project.oag.service.AuthenticationService;
import com.project.oag.service.CartService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private CartService cartService;

    @Autowired
    private ArtworkService artworkService;

    @Autowired
    private AuthenticationService authenticationService;
/*
    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addToCart(@RequestBody AddToCartDto addToCartDto,
                                                 @RequestParam("token") String token) throws AuthenticationFailException {
        authenticationService.authenticate(token);
        User user = authenticationService.getUser(token);
        Artwork artwork = artworkService.findById(addToCartDto.getArtworkId());
        System.out.println("product to add"+  artwork.getArtworkName());
        cartService.addToCart(addToCartDto, artwork, user);
        return new ResponseEntity<ApiResponse>(new ApiResponse(true, "Added to cart"), HttpStatus.CREATED);
    }*/
    
    @GetMapping("/")
    public ResponseEntity<CartDto> getCartItems(@RequestParam("token") String token) throws AuthenticationFailException {
        authenticationService.authenticate(token);
        User user = authenticationService.getUser(token);
        CartDto cartDto = cartService.listCartItems(user);
        return new ResponseEntity<CartDto>(cartDto,HttpStatus.OK);
    }
    /**
    @PutMapping("/update/{cartItemId}")
    public ResponseEntity<ApiResponse> updateCartItem(@RequestBody @Valid AddToCartDto cartDto,
                                                      @RequestParam("token") String token) throws AuthenticationFailException,CartItemNotExistException {
        authenticationService.authenticate(token);
        User user = authenticationService.getUser(token);
        Artwork artwork = artworkService.getArtworkById(cartDto.getArtworkId());
        cartService.updateCartItem(cartDto, user,artwork);
        return new ResponseEntity<ApiResponse>(new ApiResponse(true, "Item has been updated"), HttpStatus.OK);
    }*/

    @DeleteMapping("/delete/{cartItemId}")
    public ResponseEntity<ApiResponse> deleteCartItem(@PathVariable("cartItemId") int itemID,@RequestParam("token") String token) throws AuthenticationFailException, CartItemNotExistException {
        authenticationService.authenticate(token);
        Long userId = authenticationService.getUser(token).getId();
        cartService.deleteCartItem(itemID, userId);
        return new ResponseEntity<ApiResponse>(new ApiResponse(true, "Item has been removed"), HttpStatus.OK);
    }

}
/**@RestController
@RequestMapping("/cart")
public class CartController {
	  @Autowired
	  private CartRepository cartRepository;
	  @Autowired
	  private ArtworkRepository artworkRepository;
	  
	  private Map cartItems = new HashMap<>();
	    
	    @GetMapping("/")
	    public List viewCart() {
	        return new ArrayList<>(cartItems.values());
	    }
	    
	    @PostMapping("/add")
	    public void addToCart(@RequestParam Long artworkId) {
	        Artwork artwork = artworkRepository.findById(artworkId)
	            .orElseThrow(() -> new RuntimeException("Artwork not found"));
	        if (cartItems.containsKey(artworkId)) {
	            Cart item = Cart.get(artworkId);
	            item.setQuantity(item.getQuantity() + 1);
	        } else {
	            cart.put(artworkId, new Cart(artwork));
	        }
	    }
	    
	    @PostMapping("/remove")
	    public void removeFromCart(@RequestParam Long artworkId) {
	        cartItems.remove(artworkId);
	    }
	    
	    @GetMapping("/checkout")
	    public void checkout() {
	        // Process the order
	    }
	  
	  
	  @GetMapping("/artworks")
	    public List getAllArtworks() {
	        return cartRepository.findAll();
	    }
	    
	    @PostMapping("/artworks")
	    public Artwork addArtwork(@RequestBody Artwork artwork) {
	        return cartRepository.save(artwork);
	    }
	    
	    @GetMapping("/artworks/{id}")
	    public Cart getArtworkById(@PathVariable("id") Long id) {
	        return cartRepository.findById(id).get();
	    }
	    
	    @PutMapping("/artworks/{id}")
	    public Artwork updateArtwork(@PathVariable("id") Long id, @RequestBody Artwork artwork) {
	    	artwork.setId(id);
	        return cartRepository.save(artwork);
	    }
	    
	    @DeleteMapping("/artworks/{id}")
	    public void deleteArtwork(@PathVariable("id") Long id) {
	        cartRepository.deleteById(id);
	    }}
**/



