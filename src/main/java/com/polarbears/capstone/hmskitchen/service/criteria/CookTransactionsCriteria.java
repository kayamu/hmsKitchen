package com.polarbears.capstone.hmskitchen.service.criteria;

import com.polarbears.capstone.hmskitchen.domain.enumeration.KITCHENTYPES;
import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.polarbears.capstone.hmskitchen.domain.CookTransactions} entity. This class is used
 * in {@link com.polarbears.capstone.hmskitchen.web.rest.CookTransactionsResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /cook-transactions?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CookTransactionsCriteria implements Serializable, Criteria {

    /**
     * Class for filtering KITCHENTYPES
     */
    public static class KITCHENTYPESFilter extends Filter<KITCHENTYPES> {

        public KITCHENTYPESFilter() {}

        public KITCHENTYPESFilter(KITCHENTYPESFilter filter) {
            super(filter);
        }

        @Override
        public KITCHENTYPESFilter copy() {
            return new KITCHENTYPESFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter kitchenId;

    private LocalDateFilter statusChangedDate;

    private LocalDateFilter transactionDate;

    private KITCHENTYPESFilter type;

    private LocalDateFilter createdDate;

    private LongFilter cookOrdersId;

    private Boolean distinct;

    public CookTransactionsCriteria() {}

    public CookTransactionsCriteria(CookTransactionsCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.kitchenId = other.kitchenId == null ? null : other.kitchenId.copy();
        this.statusChangedDate = other.statusChangedDate == null ? null : other.statusChangedDate.copy();
        this.transactionDate = other.transactionDate == null ? null : other.transactionDate.copy();
        this.type = other.type == null ? null : other.type.copy();
        this.createdDate = other.createdDate == null ? null : other.createdDate.copy();
        this.cookOrdersId = other.cookOrdersId == null ? null : other.cookOrdersId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public CookTransactionsCriteria copy() {
        return new CookTransactionsCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LongFilter getKitchenId() {
        return kitchenId;
    }

    public LongFilter kitchenId() {
        if (kitchenId == null) {
            kitchenId = new LongFilter();
        }
        return kitchenId;
    }

    public void setKitchenId(LongFilter kitchenId) {
        this.kitchenId = kitchenId;
    }

    public LocalDateFilter getStatusChangedDate() {
        return statusChangedDate;
    }

    public LocalDateFilter statusChangedDate() {
        if (statusChangedDate == null) {
            statusChangedDate = new LocalDateFilter();
        }
        return statusChangedDate;
    }

    public void setStatusChangedDate(LocalDateFilter statusChangedDate) {
        this.statusChangedDate = statusChangedDate;
    }

    public LocalDateFilter getTransactionDate() {
        return transactionDate;
    }

    public LocalDateFilter transactionDate() {
        if (transactionDate == null) {
            transactionDate = new LocalDateFilter();
        }
        return transactionDate;
    }

    public void setTransactionDate(LocalDateFilter transactionDate) {
        this.transactionDate = transactionDate;
    }

    public KITCHENTYPESFilter getType() {
        return type;
    }

    public KITCHENTYPESFilter type() {
        if (type == null) {
            type = new KITCHENTYPESFilter();
        }
        return type;
    }

    public void setType(KITCHENTYPESFilter type) {
        this.type = type;
    }

    public LocalDateFilter getCreatedDate() {
        return createdDate;
    }

    public LocalDateFilter createdDate() {
        if (createdDate == null) {
            createdDate = new LocalDateFilter();
        }
        return createdDate;
    }

    public void setCreatedDate(LocalDateFilter createdDate) {
        this.createdDate = createdDate;
    }

    public LongFilter getCookOrdersId() {
        return cookOrdersId;
    }

    public LongFilter cookOrdersId() {
        if (cookOrdersId == null) {
            cookOrdersId = new LongFilter();
        }
        return cookOrdersId;
    }

    public void setCookOrdersId(LongFilter cookOrdersId) {
        this.cookOrdersId = cookOrdersId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final CookTransactionsCriteria that = (CookTransactionsCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(kitchenId, that.kitchenId) &&
            Objects.equals(statusChangedDate, that.statusChangedDate) &&
            Objects.equals(transactionDate, that.transactionDate) &&
            Objects.equals(type, that.type) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(cookOrdersId, that.cookOrdersId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, kitchenId, statusChangedDate, transactionDate, type, createdDate, cookOrdersId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CookTransactionsCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (kitchenId != null ? "kitchenId=" + kitchenId + ", " : "") +
            (statusChangedDate != null ? "statusChangedDate=" + statusChangedDate + ", " : "") +
            (transactionDate != null ? "transactionDate=" + transactionDate + ", " : "") +
            (type != null ? "type=" + type + ", " : "") +
            (createdDate != null ? "createdDate=" + createdDate + ", " : "") +
            (cookOrdersId != null ? "cookOrdersId=" + cookOrdersId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
