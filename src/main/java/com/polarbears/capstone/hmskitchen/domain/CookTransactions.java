package com.polarbears.capstone.hmskitchen.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.polarbears.capstone.hmskitchen.domain.enumeration.KITCHENTYPES;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A CookTransactions.
 */
@Entity
@Table(name = "cook_transactions")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CookTransactions implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "kitchen_id")
    private Long kitchenId;

    @Column(name = "status_changed_date")
    private LocalDate statusChangedDate;

    @Column(name = "transaction_date")
    private LocalDate transactionDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private KITCHENTYPES type;

    @Column(name = "created_date")
    private LocalDate createdDate;

    @ManyToMany(mappedBy = "cookTransactions")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "cookTransactions" }, allowSetters = true)
    private Set<CookOrders> cookOrders = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CookTransactions id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getKitchenId() {
        return this.kitchenId;
    }

    public CookTransactions kitchenId(Long kitchenId) {
        this.setKitchenId(kitchenId);
        return this;
    }

    public void setKitchenId(Long kitchenId) {
        this.kitchenId = kitchenId;
    }

    public LocalDate getStatusChangedDate() {
        return this.statusChangedDate;
    }

    public CookTransactions statusChangedDate(LocalDate statusChangedDate) {
        this.setStatusChangedDate(statusChangedDate);
        return this;
    }

    public void setStatusChangedDate(LocalDate statusChangedDate) {
        this.statusChangedDate = statusChangedDate;
    }

    public LocalDate getTransactionDate() {
        return this.transactionDate;
    }

    public CookTransactions transactionDate(LocalDate transactionDate) {
        this.setTransactionDate(transactionDate);
        return this;
    }

    public void setTransactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
    }

    public KITCHENTYPES getType() {
        return this.type;
    }

    public CookTransactions type(KITCHENTYPES type) {
        this.setType(type);
        return this;
    }

    public void setType(KITCHENTYPES type) {
        this.type = type;
    }

    public LocalDate getCreatedDate() {
        return this.createdDate;
    }

    public CookTransactions createdDate(LocalDate createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public Set<CookOrders> getCookOrders() {
        return this.cookOrders;
    }

    public void setCookOrders(Set<CookOrders> cookOrders) {
        if (this.cookOrders != null) {
            this.cookOrders.forEach(i -> i.removeCookTransactions(this));
        }
        if (cookOrders != null) {
            cookOrders.forEach(i -> i.addCookTransactions(this));
        }
        this.cookOrders = cookOrders;
    }

    public CookTransactions cookOrders(Set<CookOrders> cookOrders) {
        this.setCookOrders(cookOrders);
        return this;
    }

    public CookTransactions addCookOrders(CookOrders cookOrders) {
        this.cookOrders.add(cookOrders);
        cookOrders.getCookTransactions().add(this);
        return this;
    }

    public CookTransactions removeCookOrders(CookOrders cookOrders) {
        this.cookOrders.remove(cookOrders);
        cookOrders.getCookTransactions().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CookTransactions)) {
            return false;
        }
        return id != null && id.equals(((CookTransactions) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CookTransactions{" +
            "id=" + getId() +
            ", kitchenId=" + getKitchenId() +
            ", statusChangedDate='" + getStatusChangedDate() + "'" +
            ", transactionDate='" + getTransactionDate() + "'" +
            ", type='" + getType() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            "}";
    }
}
