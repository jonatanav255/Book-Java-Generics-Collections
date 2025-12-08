package com.example;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Immutable class representing a library fine.
 * Uses BigDecimal for precise monetary calculations.
 */
public final class Fine {
    private final String isbn;
    private final String bookTitle;
    private final String borrowerName;
    private final long daysOverdue;
    private final BigDecimal amountDue;
    private final BigDecimal amountPaid;
    private final LocalDateTime createdAt;
    private final LocalDateTime paidAt;
    private final boolean isWaived;
    private final String waiveReason;

    private Fine(Builder builder) {
        this.isbn = builder.isbn;
        this.bookTitle = builder.bookTitle;
        this.borrowerName = builder.borrowerName;
        this.daysOverdue = builder.daysOverdue;
        this.amountDue = builder.amountDue;
        this.amountPaid = builder.amountPaid;
        this.createdAt = builder.createdAt;
        this.paidAt = builder.paidAt;
        this.isWaived = builder.isWaived;
        this.waiveReason = builder.waiveReason;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public String getBorrowerName() {
        return borrowerName;
    }

    public long getDaysOverdue() {
        return daysOverdue;
    }

    public BigDecimal getAmountDue() {
        return amountDue;
    }

    public BigDecimal getAmountPaid() {
        return amountPaid;
    }

    public BigDecimal getAmountRemaining() {
        return amountDue.subtract(amountPaid);
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getPaidAt() {
        return paidAt;
    }

    public boolean isWaived() {
        return isWaived;
    }

    public String getWaiveReason() {
        return waiveReason;
    }

    public boolean isPaid() {
        return amountPaid.compareTo(amountDue) >= 0 || isWaived;
    }

    public boolean isPartiallyPaid() {
        return amountPaid.compareTo(BigDecimal.ZERO) > 0 &&
               amountPaid.compareTo(amountDue) < 0;
    }

    /**
     * Create a new Fine with a payment applied.
     * Returns a new immutable Fine object.
     */
    public Fine withPayment(BigDecimal payment) {
        if (payment == null || payment.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Payment must be positive");
        }
        if (isWaived) {
            throw new IllegalStateException("Cannot pay a waived fine");
        }

        BigDecimal newAmountPaid = this.amountPaid.add(payment);
        if (newAmountPaid.compareTo(amountDue) > 0) {
            throw new IllegalArgumentException("Payment exceeds amount due");
        }

        return new Builder()
            .isbn(this.isbn)
            .bookTitle(this.bookTitle)
            .borrowerName(this.borrowerName)
            .daysOverdue(this.daysOverdue)
            .amountDue(this.amountDue)
            .amountPaid(newAmountPaid)
            .createdAt(this.createdAt)
            .paidAt(newAmountPaid.compareTo(amountDue) >= 0 ? LocalDateTime.now() : this.paidAt)
            .isWaived(false)
            .waiveReason(null)
            .build();
    }

    /**
     * Create a new Fine with waiver applied.
     * Returns a new immutable Fine object.
     */
    public Fine withWaiver(String reason) {
        if (reason == null || reason.trim().isEmpty()) {
            throw new IllegalArgumentException("Waiver reason required");
        }
        if (isPaid()) {
            throw new IllegalStateException("Cannot waive a paid fine");
        }

        return new Builder()
            .isbn(this.isbn)
            .bookTitle(this.bookTitle)
            .borrowerName(this.borrowerName)
            .daysOverdue(this.daysOverdue)
            .amountDue(this.amountDue)
            .amountPaid(this.amountPaid)
            .createdAt(this.createdAt)
            .paidAt(LocalDateTime.now())
            .isWaived(true)
            .waiveReason(reason.trim())
            .build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Fine fine = (Fine) o;
        return isbn.equals(fine.isbn) &&
               borrowerName.equals(fine.borrowerName) &&
               createdAt.equals(fine.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isbn, borrowerName, createdAt);
    }

    @Override
    public String toString() {
        if (isWaived) {
            return String.format("Fine for '%s' (%s) - $%s WAIVED (%s)",
                bookTitle, borrowerName, amountDue, waiveReason);
        }
        if (isPaid()) {
            return String.format("Fine for '%s' (%s) - $%s PAID",
                bookTitle, borrowerName, amountDue);
        }
        if (isPartiallyPaid()) {
            return String.format("Fine for '%s' (%s) - $%s due ($%s paid, $%s remaining)",
                bookTitle, borrowerName, amountDue, amountPaid, getAmountRemaining());
        }
        return String.format("Fine for '%s' (%s) - $%s due (%d days overdue)",
            bookTitle, borrowerName, amountDue, daysOverdue);
    }

    /**
     * Builder pattern for creating Fine objects.
     */
    public static class Builder {
        private String isbn;
        private String bookTitle;
        private String borrowerName;
        private long daysOverdue;
        private BigDecimal amountDue = BigDecimal.ZERO;
        private BigDecimal amountPaid = BigDecimal.ZERO;
        private LocalDateTime createdAt = LocalDateTime.now();
        private LocalDateTime paidAt;
        private boolean isWaived = false;
        private String waiveReason;

        public Builder isbn(String isbn) {
            this.isbn = isbn;
            return this;
        }

        public Builder bookTitle(String bookTitle) {
            this.bookTitle = bookTitle;
            return this;
        }

        public Builder borrowerName(String borrowerName) {
            this.borrowerName = borrowerName;
            return this;
        }

        public Builder daysOverdue(long daysOverdue) {
            this.daysOverdue = daysOverdue;
            return this;
        }

        public Builder amountDue(BigDecimal amountDue) {
            this.amountDue = amountDue;
            return this;
        }

        public Builder amountPaid(BigDecimal amountPaid) {
            this.amountPaid = amountPaid;
            return this;
        }

        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder paidAt(LocalDateTime paidAt) {
            this.paidAt = paidAt;
            return this;
        }

        public Builder isWaived(boolean isWaived) {
            this.isWaived = isWaived;
            return this;
        }

        public Builder waiveReason(String waiveReason) {
            this.waiveReason = waiveReason;
            return this;
        }

        public Fine build() {
            if (isbn == null || isbn.trim().isEmpty()) {
                throw new IllegalArgumentException("ISBN required");
            }
            if (bookTitle == null || bookTitle.trim().isEmpty()) {
                throw new IllegalArgumentException("Book title required");
            }
            if (borrowerName == null || borrowerName.trim().isEmpty()) {
                throw new IllegalArgumentException("Borrower name required");
            }
            if (daysOverdue <= 0) {
                throw new IllegalArgumentException("Days overdue must be positive");
            }
            if (amountDue == null || amountDue.compareTo(BigDecimal.ZERO) < 0) {
                throw new IllegalArgumentException("Amount due cannot be negative");
            }
            if (amountPaid == null || amountPaid.compareTo(BigDecimal.ZERO) < 0) {
                throw new IllegalArgumentException("Amount paid cannot be negative");
            }
            if (amountPaid.compareTo(amountDue) > 0) {
                throw new IllegalArgumentException("Amount paid cannot exceed amount due");
            }
            if (createdAt == null) {
                throw new IllegalArgumentException("Created timestamp required");
            }

            return new Fine(this);
        }
    }
}
