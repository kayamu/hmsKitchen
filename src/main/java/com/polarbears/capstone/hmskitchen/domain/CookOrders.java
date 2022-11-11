package com.polarbears.capstone.hmskitchen.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A CookOrders.
 */
@Entity
@Table(name = "cook_orders")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CookOrders implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "kitchen_id")
    private Long kitchenId;

    @Column(name = "customer_id")
    private Long customerId;

    @Column(name = "customer_cart_id")
    private Long customerCartId;

    @Column(name = "menu_item_id")
    private Long menuItemId;

    @Column(name = "menu_item_name")
    private String menuItemName;

    @Column(name = "menu_item_code")
    private String menuItemCode;

    @Column(name = "meal_id")
    private Long mealId;

    @Column(name = "line_number")
    private Integer lineNumber;

    @Column(name = "request_date")
    private LocalDate requestDate;

    @Size(max = 1024)
    @Column(name = "message", length = 1024)
    private String message;

    @Column(name = "created_date")
    private LocalDate createdDate;

    @ManyToMany
    @JoinTable(
        name = "rel_cook_orders__cook_transactions",
        joinColumns = @JoinColumn(name = "cook_orders_id"),
        inverseJoinColumns = @JoinColumn(name = "cook_transactions_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "cookOrders" }, allowSetters = true)
    private Set<CookTransactions> cookTransactions = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CookOrders id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getKitchenId() {
        return this.kitchenId;
    }

    public CookOrders kitchenId(Long kitchenId) {
        this.setKitchenId(kitchenId);
        return this;
    }

    public void setKitchenId(Long kitchenId) {
        this.kitchenId = kitchenId;
    }

    public Long getCustomerId() {
        return this.customerId;
    }

    public CookOrders customerId(Long customerId) {
        this.setCustomerId(customerId);
        return this;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getCustomerCartId() {
        return this.customerCartId;
    }

    public CookOrders customerCartId(Long customerCartId) {
        this.setCustomerCartId(customerCartId);
        return this;
    }

    public void setCustomerCartId(Long customerCartId) {
        this.customerCartId = customerCartId;
    }

    public Long getMenuItemId() {
        return this.menuItemId;
    }

    public CookOrders menuItemId(Long menuItemId) {
        this.setMenuItemId(menuItemId);
        return this;
    }

    public void setMenuItemId(Long menuItemId) {
        this.menuItemId = menuItemId;
    }

    public String getMenuItemName() {
        return this.menuItemName;
    }

    public CookOrders menuItemName(String menuItemName) {
        this.setMenuItemName(menuItemName);
        return this;
    }

    public void setMenuItemName(String menuItemName) {
        this.menuItemName = menuItemName;
    }

    public String getMenuItemCode() {
        return this.menuItemCode;
    }

    public CookOrders menuItemCode(String menuItemCode) {
        this.setMenuItemCode(menuItemCode);
        return this;
    }

    public void setMenuItemCode(String menuItemCode) {
        this.menuItemCode = menuItemCode;
    }

    public Long getMealId() {
        return this.mealId;
    }

    public CookOrders mealId(Long mealId) {
        this.setMealId(mealId);
        return this;
    }

    public void setMealId(Long mealId) {
        this.mealId = mealId;
    }

    public Integer getLineNumber() {
        return this.lineNumber;
    }

    public CookOrders lineNumber(Integer lineNumber) {
        this.setLineNumber(lineNumber);
        return this;
    }

    public void setLineNumber(Integer lineNumber) {
        this.lineNumber = lineNumber;
    }

    public LocalDate getRequestDate() {
        return this.requestDate;
    }

    public CookOrders requestDate(LocalDate requestDate) {
        this.setRequestDate(requestDate);
        return this;
    }

    public void setRequestDate(LocalDate requestDate) {
        this.requestDate = requestDate;
    }

    public String getMessage() {
        return this.message;
    }

    public CookOrders message(String message) {
        this.setMessage(message);
        return this;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDate getCreatedDate() {
        return this.createdDate;
    }

    public CookOrders createdDate(LocalDate createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public Set<CookTransactions> getCookTransactions() {
        return this.cookTransactions;
    }

    public void setCookTransactions(Set<CookTransactions> cookTransactions) {
        this.cookTransactions = cookTransactions;
    }

    public CookOrders cookTransactions(Set<CookTransactions> cookTransactions) {
        this.setCookTransactions(cookTransactions);
        return this;
    }

    public CookOrders addCookTransactions(CookTransactions cookTransactions) {
        this.cookTransactions.add(cookTransactions);
        cookTransactions.getCookOrders().add(this);
        return this;
    }

    public CookOrders removeCookTransactions(CookTransactions cookTransactions) {
        this.cookTransactions.remove(cookTransactions);
        cookTransactions.getCookOrders().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CookOrders)) {
            return false;
        }
        return id != null && id.equals(((CookOrders) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CookOrders{" +
            "id=" + getId() +
            ", kitchenId=" + getKitchenId() +
            ", customerId=" + getCustomerId() +
            ", customerCartId=" + getCustomerCartId() +
            ", menuItemId=" + getMenuItemId() +
            ", menuItemName='" + getMenuItemName() + "'" +
            ", menuItemCode='" + getMenuItemCode() + "'" +
            ", mealId=" + getMealId() +
            ", lineNumber=" + getLineNumber() +
            ", requestDate='" + getRequestDate() + "'" +
            ", message='" + getMessage() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            "}";
    }
}
