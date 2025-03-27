package seedu.address.model.company;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Objects;

/**
 * Represents a Company with a name
 */
public class Company {
    private final CompanyName name;

    private final CompanyAddress address;

    /**
     * Constructs a Company with the specified job title and company name.
     *
     * @param name   The name of the company.
     * @param address  The address of the company.
     */
    public Company(CompanyName name, CompanyAddress address) {
        requireAllNonNull(name, address);
        this.name = name;
        this.address = address;
    }

    public CompanyName getName() {
        return this.name;
    }

    public CompanyAddress getAddress() {
        return this.address;
    }

    /**
     * Returns true if this Company is the same as the specified object.
     *
     * @param other The object to compare to.
     * @return True if the objects are equal, false otherwise.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof Company otherCompany)) {
            return false;
        }
        return this.name.equals(otherCompany.name);
    }

    /**
     * Returns the hash code of this company.
     *
     * @return The hash code.
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.name);
    }
}
