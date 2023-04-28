package com.example.finalproject.services;

import com.example.finalproject.models.Collection;
import com.example.finalproject.models.User;
import com.example.finalproject.repositories.CollectionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class CollectionService {
    private final CollectionRepository collectionRepository;

    public CollectionService(CollectionRepository collectionRepository) {
        this.collectionRepository = collectionRepository;
    }

    public Collection getCollectionById(int id){
        Optional<Collection> optionalCollection = collectionRepository.findById(id);
        return optionalCollection.orElse(null);
    }

    public List<Collection> getCollectionByCreatorId(int id) {
        return collectionRepository.findByCreatorId(id);
    }

    public List<Collection> getAllCollection() {
        return collectionRepository.findByOrderByDateTimeDesc();
    }

    @Transactional
    public void addCollection(Collection collection, User user, String banner) {
        collection.setBanner(banner);
        collection.setCreator(user);
        collectionRepository.save(collection);
    }
}
