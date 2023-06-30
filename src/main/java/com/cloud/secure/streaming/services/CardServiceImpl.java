package com.cloud.secure.streaming.services;

import com.cloud.secure.streaming.entities.UserCard;
import com.cloud.secure.streaming.repositories.CardRepository;
import org.springframework.stereotype.Service;

import java.util.List;
/**
 * @author 689Cloud
 */
@Service
public class CardServiceImpl implements CardService {

    final CardRepository cardRepository;

    public CardServiceImpl (CardRepository cardRepository){
        this.cardRepository = cardRepository;
    }

    @Override
    public UserCard save(UserCard card) {
        return cardRepository.save(card);
    }

    @Override
    public void delete(UserCard card) {
        cardRepository.delete(card);
    }

    @Override
    public UserCard getById(String id) {
        return cardRepository.findById(id).orElse(null);
    }

    @Override
    public List<UserCard> getAllByIdIn(List<String> ids) {
        return cardRepository.findAllByIdIn(ids);
    }

    @Override
    public void deleteByIdIn(List<String> ids) {
       cardRepository.deleteAllByIdIn(ids);
    }
}
