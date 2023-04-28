package com.example.finalproject.models;

import com.example.finalproject.enumm.RarityRank;
import com.example.finalproject.enumm.TokenStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "tokens")
public class Token {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "title", nullable = false, columnDefinition = "text", unique = true)
    @NotEmpty(message = "Название не может быть пустым")
    private String title;

    @Column(name = "description", nullable = false, columnDefinition = "text")
    @NotEmpty(message = "Описание не может быть пустым")
    private String description;

    @Column(name = "price")
    @DecimalMin(value = "0.0", inclusive = false, message = "Цена не может быть отрицательной или нулевой")
    private Float price;

    private LocalDateTime dateTime;

    private String image;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name="creator_id", nullable=false)
    private User creator;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name="owner_id", nullable=false)
    private User owner;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name="collection_id", nullable=false)
    private Collection collection;

    private RarityRank rarityRank;

    private TokenStatus tokenStatus;

    private int newOwnerId;

    private boolean deletable;

    @ManyToMany
    @JoinTable(name = "token_cart", joinColumns = @JoinColumn(name = "token_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> userList;

    @OneToMany(mappedBy = "token", fetch = FetchType.LAZY)
    private List<Order> orderList;

    @PrePersist
    private void init(){
        dateTime = LocalDateTime.now();
    }

    public Token() {}

    public Token(String title, String description, RarityRank rarityRank) {
        this.title = title;
        this.description = description;
        this.rarityRank = rarityRank;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public Collection getCollection() {
        return collection;
    }

    public void setCollection(Collection collection) {
        this.collection = collection;
    }

    public RarityRank getRarityRank() {
        return rarityRank;
    }

    public void setRarityRank(RarityRank rarityRank) {
        this.rarityRank = rarityRank;
    }

    public TokenStatus getTokenStatus() {
        return tokenStatus;
    }

    public void setTokenStatus(TokenStatus tokenStatus) {
        this.tokenStatus = tokenStatus;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public void updateDateTime() {
        this.dateTime = LocalDateTime.now();
    }

    public int getNewOwnerId() {
        return newOwnerId;
    }

    public void setNewOwnerId(int newOwnerId) {
        this.newOwnerId = newOwnerId;
    }

    public boolean isDeletable() {
        return deletable;
    }

    public void setDeletable(boolean deletable) {
        this.deletable = deletable;
    }
}