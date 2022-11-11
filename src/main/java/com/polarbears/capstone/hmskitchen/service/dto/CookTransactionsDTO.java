package com.polarbears.capstone.hmskitchen.service.dto;

import com.polarbears.capstone.hmskitchen.domain.enumeration.KITCHENTYPES;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.polarbears.capstone.hmskitchen.domain.CookTransactions} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CookTransactionsDTO implements Serializable {

    private Long id;

    private Long kitchenId;

    private LocalDate statusChangedDate;

    private LocalDate transactionDate;

    private KITCHENTYPES type;

    private LocalDate createdDate;

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

    public LocalDate getStatusChangedDate() {
        return statusChangedDate;
    }

    public void setStatusChangedDate(LocalDate statusChangedDate) {
        this.statusChangedDate = statusChangedDate;
    }

    public LocalDate getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
    }

    public KITCHENTYPES getType() {
        return type;
    }

    public void setType(KITCHENTYPES type) {
        this.type = type;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CookTransactionsDTO)) {
            return false;
        }

        CookTransactionsDTO cookTransactionsDTO = (CookTransactionsDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, cookTransactionsDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CookTransactionsDTO{" +
            "id=" + getId() +
            ", kitchenId=" + getKitchenId() +
            ", statusChangedDate='" + getStatusChangedDate() + "'" +
            ", transactionDate='" + getTransactionDate() + "'" +
            ", type='" + getType() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            "}";
    }
}
