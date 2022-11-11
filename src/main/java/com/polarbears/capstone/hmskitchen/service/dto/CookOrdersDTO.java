package com.polarbears.capstone.hmskitchen.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.polarbears.capstone.hmskitchen.domain.CookOrders} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CookOrdersDTO implements Serializable {

    private Long id;

    private Long kitchenId;

    private Long customerId;

    private Long customerCartId;

    private Long menuItemId;

    private String menuItemName;

    private String menuItemCode;

    private Long mealId;

    private Integer lineNumber;

    private LocalDate requestDate;

    @Size(max = 1024)
    private String message;

    private LocalDate createdDate;

    private Set<CookTransactionsDTO> cookTransactions = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getKitchenId() {
        return kitchenId;
    }

    public void setKitchenId(Long kitchenId) {
        this.kitchenId = kitchenId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getCustomerCartId() {
        return customerCartId;
    }

    public void setCustomerCartId(Long customerCartId) {
        this.customerCartId = customerCartId;
    }

    public Long getMenuItemId() {
        return menuItemId;
    }

    public void setMenuItemId(Long menuItemId) {
        this.menuItemId = menuItemId;
    }

    public String getMenuItemName() {
        return menuItemName;
    }

    public void setMenuItemName(String menuItemName) {
        this.menuItemName = menuItemName;
    }

    public String getMenuItemCode() {
        return menuItemCode;
    }

    public void setMenuItemCode(String menuItemCode) {
        this.menuItemCode = menuItemCode;
    }

    public Long getMealId() {
        return mealId;
    }

    public void setMealId(Long mealId) {
        this.mealId = mealId;
    }

    public Integer getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(Integer lineNumber) {
        this.lineNumber = lineNumber;
    }

    public LocalDate getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(LocalDate requestDate) {
        this.requestDate = requestDate;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public Set<CookTransactionsDTO> getCookTransactions() {
        return cookTransactions;
    }

    public void setCookTransactions(Set<CookTransactionsDTO> cookTransactions) {
        this.cookTransactions = cookTransactions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CookOrdersDTO)) {
            return false;
        }

        CookOrdersDTO cookOrdersDTO = (CookOrdersDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, cookOrdersDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CookOrdersDTO{" +
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
            ", cookTransactions=" + getCookTransactions() +
            "}";
    }
}
