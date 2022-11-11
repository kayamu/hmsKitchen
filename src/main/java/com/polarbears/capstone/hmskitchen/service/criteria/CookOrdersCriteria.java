package com.polarbears.capstone.hmskitchen.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.polarbears.capstone.hmskitchen.domain.CookOrders} entity. This class is used
 * in {@link com.polarbears.capstone.hmskitchen.web.rest.CookOrdersResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /cook-orders?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CookOrdersCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter kitchenId;

    private LongFilter customerId;

    private LongFilter customerCartId;

    private LongFilter menuItemId;

    private StringFilter menuItemName;

    private StringFilter menuItemCode;

    private LongFilter mealId;

    private IntegerFilter lineNumber;

    private LocalDateFilter requestDate;

    private StringFilter message;

    private LocalDateFilter createdDate;

    private LongFilter cookTransactionsId;

    private Boolean distinct;

    public CookOrdersCriteria() {}

    public CookOrdersCriteria(CookOrdersCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.kitchenId = other.kitchenId == null ? null : other.kitchenId.copy();
        this.customerId = other.customerId == null ? null : other.customerId.copy();
        this.customerCartId = other.customerCartId == null ? null : other.customerCartId.copy();
        this.menuItemId = other.menuItemId == null ? null : other.menuItemId.copy();
        this.menuItemName = other.menuItemName == null ? null : other.menuItemName.copy();
        this.menuItemCode = other.menuItemCode == null ? null : other.menuItemCode.copy();
        this.mealId = other.mealId == null ? null : other.mealId.copy();
        this.lineNumber = other.lineNumber == null ? null : other.lineNumber.copy();
        this.requestDate = other.requestDate == null ? null : other.requestDate.copy();
        this.message = other.message == null ? null : other.message.copy();
        this.createdDate = other.createdDate == null ? null : other.createdDate.copy();
        this.cookTransactionsId = other.cookTransactionsId == null ? null : other.cookTransactionsId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public CookOrdersCriteria copy() {
        return new CookOrdersCriteria(this);
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

    public LongFilter getCustomerId() {
        return customerId;
    }

    public LongFilter customerId() {
        if (customerId == null) {
            customerId = new LongFilter();
        }
        return customerId;
    }

    public void setCustomerId(LongFilter customerId) {
        this.customerId = customerId;
    }

    public LongFilter getCustomerCartId() {
        return customerCartId;
    }

    public LongFilter customerCartId() {
        if (customerCartId == null) {
            customerCartId = new LongFilter();
        }
        return customerCartId;
    }

    public void setCustomerCartId(LongFilter customerCartId) {
        this.customerCartId = customerCartId;
    }

    public LongFilter getMenuItemId() {
        return menuItemId;
    }

    public LongFilter menuItemId() {
        if (menuItemId == null) {
            menuItemId = new LongFilter();
        }
        return menuItemId;
    }

    public void setMenuItemId(LongFilter menuItemId) {
        this.menuItemId = menuItemId;
    }

    public StringFilter getMenuItemName() {
        return menuItemName;
    }

    public StringFilter menuItemName() {
        if (menuItemName == null) {
            menuItemName = new StringFilter();
        }
        return menuItemName;
    }

    public void setMenuItemName(StringFilter menuItemName) {
        this.menuItemName = menuItemName;
    }

    public StringFilter getMenuItemCode() {
        return menuItemCode;
    }

    public StringFilter menuItemCode() {
        if (menuItemCode == null) {
            menuItemCode = new StringFilter();
        }
        return menuItemCode;
    }

    public void setMenuItemCode(StringFilter menuItemCode) {
        this.menuItemCode = menuItemCode;
    }

    public LongFilter getMealId() {
        return mealId;
    }

    public LongFilter mealId() {
        if (mealId == null) {
            mealId = new LongFilter();
        }
        return mealId;
    }

    public void setMealId(LongFilter mealId) {
        this.mealId = mealId;
    }

    public IntegerFilter getLineNumber() {
        return lineNumber;
    }

    public IntegerFilter lineNumber() {
        if (lineNumber == null) {
            lineNumber = new IntegerFilter();
        }
        return lineNumber;
    }

    public void setLineNumber(IntegerFilter lineNumber) {
        this.lineNumber = lineNumber;
    }

    public LocalDateFilter getRequestDate() {
        return requestDate;
    }

    public LocalDateFilter requestDate() {
        if (requestDate == null) {
            requestDate = new LocalDateFilter();
        }
        return requestDate;
    }

    public void setRequestDate(LocalDateFilter requestDate) {
        this.requestDate = requestDate;
    }

    public StringFilter getMessage() {
        return message;
    }

    public StringFilter message() {
        if (message == null) {
            message = new StringFilter();
        }
        return message;
    }

    public void setMessage(StringFilter message) {
        this.message = message;
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

    public LongFilter getCookTransactionsId() {
        return cookTransactionsId;
    }

    public LongFilter cookTransactionsId() {
        if (cookTransactionsId == null) {
            cookTransactionsId = new LongFilter();
        }
        return cookTransactionsId;
    }

    public void setCookTransactionsId(LongFilter cookTransactionsId) {
        this.cookTransactionsId = cookTransactionsId;
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
        final CookOrdersCriteria that = (CookOrdersCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(kitchenId, that.kitchenId) &&
            Objects.equals(customerId, that.customerId) &&
            Objects.equals(customerCartId, that.customerCartId) &&
            Objects.equals(menuItemId, that.menuItemId) &&
            Objects.equals(menuItemName, that.menuItemName) &&
            Objects.equals(menuItemCode, that.menuItemCode) &&
            Objects.equals(mealId, that.mealId) &&
            Objects.equals(lineNumber, that.lineNumber) &&
            Objects.equals(requestDate, that.requestDate) &&
            Objects.equals(message, that.message) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(cookTransactionsId, that.cookTransactionsId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            kitchenId,
            customerId,
            customerCartId,
            menuItemId,
            menuItemName,
            menuItemCode,
            mealId,
            lineNumber,
            requestDate,
            message,
            createdDate,
            cookTransactionsId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CookOrdersCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (kitchenId != null ? "kitchenId=" + kitchenId + ", " : "") +
            (customerId != null ? "customerId=" + customerId + ", " : "") +
            (customerCartId != null ? "customerCartId=" + customerCartId + ", " : "") +
            (menuItemId != null ? "menuItemId=" + menuItemId + ", " : "") +
            (menuItemName != null ? "menuItemName=" + menuItemName + ", " : "") +
            (menuItemCode != null ? "menuItemCode=" + menuItemCode + ", " : "") +
            (mealId != null ? "mealId=" + mealId + ", " : "") +
            (lineNumber != null ? "lineNumber=" + lineNumber + ", " : "") +
            (requestDate != null ? "requestDate=" + requestDate + ", " : "") +
            (message != null ? "message=" + message + ", " : "") +
            (createdDate != null ? "createdDate=" + createdDate + ", " : "") +
            (cookTransactionsId != null ? "cookTransactionsId=" + cookTransactionsId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
