/*
 * This file is generated by jOOQ.
 */
package jooq.tables.records;


import jooq.tables.Messages;

import org.jooq.Record1;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes", "this-escape" })
public class MessagesRecord extends UpdatableRecordImpl<MessagesRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>messages.id</code>.
     */
    public void setId(Integer value) {
        set(0, value);
    }

    /**
     * Getter for <code>messages.id</code>.
     */
    public Integer getId() {
        return (Integer) get(0);
    }

    /**
     * Setter for <code>messages.name</code>.
     */
    public void setName(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>messages.name</code>.
     */
    public String getName() {
        return (String) get(1);
    }

    /**
     * Setter for <code>messages.message</code>.
     */
    public void setMessage(String value) {
        set(2, value);
    }

    /**
     * Getter for <code>messages.message</code>.
     */
    public String getMessage() {
        return (String) get(2);
    }

    /**
     * Setter for <code>messages.date</code>.
     */
    public void setDate(String value) {
        set(3, value);
    }

    /**
     * Getter for <code>messages.date</code>.
     */
    public String getDate() {
        return (String) get(3);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<Integer> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached MessagesRecord
     */
    public MessagesRecord() {
        super(Messages.MESSAGES);
    }

    /**
     * Create a detached, initialised MessagesRecord
     */
    public MessagesRecord(Integer id, String name, String message, String date) {
        super(Messages.MESSAGES);

        setId(id);
        setName(name);
        setMessage(message);
        setDate(date);
        resetTouchedOnNotNull();
    }

    /**
     * Create a detached, initialised MessagesRecord
     */
    public MessagesRecord(jooq.tables.pojos.Messages value) {
        super(Messages.MESSAGES);

        if (value != null) {
            setId(value.getId());
            setName(value.getName());
            setMessage(value.getMessage());
            setDate(value.getDate());
            resetTouchedOnNotNull();
        }
    }
}
