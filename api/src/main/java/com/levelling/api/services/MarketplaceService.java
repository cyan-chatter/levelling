package com.levelling.api.services;

import com.levelling.api.models.Item;
import com.levelling.api.models.User;
import com.levelling.api.repositories.ItemRepository;
import com.levelling.api.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MarketplaceService {

    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private UserRepository userRepository;

    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

    @Transactional
    public void buyItem(String username, String itemId) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found"));

        // 1. Check if user already owns the item
        if (user.getOwnedItemIds().contains(itemId)) {
            throw new RuntimeException("You already own this item.");
        }

        User.Currencies userCurrencies = user.getCurrencies();
        Item.InGamePrice itemPrice = item.getGamePrice();

        // 2. Check if user has enough currency
        if (userCurrencies.getPoints() < itemPrice.getPoints() ||
                userCurrencies.getYellowGems() < itemPrice.getYellowGems() ||
                userCurrencies.getRoyalBlueGems() < itemPrice.getRoyalBlueGems() ||
                userCurrencies.getCrimsonRedGems() < itemPrice.getCrimsonRedGems() ||
                userCurrencies.getEmeraldGreenGems() < itemPrice.getEmeraldGreenGems()) {
            throw new RuntimeException("You do not have enough currency to purchase this item.");
        }

        // 3. Deduct currency
        userCurrencies.setPoints(userCurrencies.getPoints() - itemPrice.getPoints());
        userCurrencies.setYellowGems(userCurrencies.getYellowGems() - itemPrice.getYellowGems());
        userCurrencies.setRoyalBlueGems(userCurrencies.getRoyalBlueGems() - itemPrice.getRoyalBlueGems());
        userCurrencies.setCrimsonRedGems(userCurrencies.getCrimsonRedGems() - itemPrice.getCrimsonRedGems());
        userCurrencies.setEmeraldGreenGems(userCurrencies.getEmeraldGreenGems() - itemPrice.getEmeraldGreenGems());

        // 4. Add item to user's inventory
        user.getOwnedItemIds().add(itemId);

        // 5. Save the updated user
        userRepository.save(user);
    }
}