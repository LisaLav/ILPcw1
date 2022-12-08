package uk.ac.ed.inf.enums;

/**
 * An enumeration for all the possible outcomes for a single order
 * There are 2 cases where the order is valid, and the rest are for when it's invalid
 */
public enum OrderOutcome {

    Delivered,
    ValidButNotDelivered,
    InvalidCardNumber,
    InvalidExpiryDate,
    InvalidCvv,
    InvalidTotal,
    InvalidPizzaNotDefined,
    InvalidPizzaCount,
    InvalidPizzaCombinationMultipleSuppliers,
    Invalid

}
