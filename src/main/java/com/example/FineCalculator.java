package com.example;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Thread-safe calculator for library fines.
 * Uses configurable rates and enforces maximum fine caps.
 */
public class FineCalculator {
    private static final BigDecimal DEFAULT_RATE_PER_DAY = new BigDecimal("0.50");
    private static final BigDecimal DEFAULT_MAX_FINE = new BigDecimal("25.00");
    private static final int SCALE = 2; // Two decimal places for money

    private final BigDecimal ratePerDay;
    private final BigDecimal maxFine;

    /**
     * Create calculator with default rates.
     */
    public FineCalculator() {
        this(DEFAULT_RATE_PER_DAY, DEFAULT_MAX_FINE);
    }

    /**
     * Create calculator with custom rates.
     *
     * @param ratePerDay Fine charged per day overdue
     * @param maxFine Maximum fine cap (prevents infinite fines)
     */
    public FineCalculator(BigDecimal ratePerDay, BigDecimal maxFine) {
        if (ratePerDay == null || ratePerDay.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Rate per day must be positive");
        }
        if (maxFine == null || maxFine.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Max fine must be positive");
        }
        if (ratePerDay.compareTo(maxFine) > 0) {
            throw new IllegalArgumentException("Rate per day cannot exceed max fine");
        }

        this.ratePerDay = ratePerDay.setScale(SCALE, RoundingMode.HALF_UP);
        this.maxFine = maxFine.setScale(SCALE, RoundingMode.HALF_UP);
    }

    /**
     * Calculate fine for given days overdue.
     * Applies max fine cap automatically.
     *
     * @param daysOverdue Number of days book is overdue
     * @return Calculated fine amount (capped at maxFine)
     */
    public BigDecimal calculateFine(long daysOverdue) {
        if (daysOverdue <= 0) {
            return BigDecimal.ZERO;
        }

        BigDecimal calculated = ratePerDay
            .multiply(BigDecimal.valueOf(daysOverdue))
            .setScale(SCALE, RoundingMode.HALF_UP);

        // Apply max fine cap
        return calculated.min(maxFine);
    }

    /**
     * Create a Fine object for an overdue book.
     */
    public Fine createFine(Book book) {
        if (book == null) {
            throw new IllegalArgumentException("Book cannot be null");
        }
        if (!book.isOverdue()) {
            throw new IllegalStateException("Book is not overdue");
        }
        if (!book.isBorrowed()) {
            throw new IllegalStateException("Book is not borrowed");
        }

        long daysOverdue = book.getDaysOverdue();
        BigDecimal amount = calculateFine(daysOverdue);

        return new Fine.Builder()
            .isbn(book.getIsbn())
            .bookTitle(book.getTitle())
            .borrowerName(book.getBorrowedBy())
            .daysOverdue(daysOverdue)
            .amountDue(amount)
            .build();
    }

    public BigDecimal getRatePerDay() {
        return ratePerDay;
    }

    public BigDecimal getMaxFine() {
        return maxFine;
    }

    /**
     * Check if a fine amount has reached the maximum cap.
     */
    public boolean isAtMaxFine(BigDecimal amount) {
        return amount != null && amount.compareTo(maxFine) >= 0;
    }

    /**
     * Calculate days until max fine is reached.
     */
    public long getDaysUntilMaxFine() {
        return maxFine.divide(ratePerDay, 0, RoundingMode.UP).longValue();
    }

    @Override
    public String toString() {
        return String.format("FineCalculator($%s/day, max $%s, cap at %d days)",
            ratePerDay, maxFine, getDaysUntilMaxFine());
    }
}
