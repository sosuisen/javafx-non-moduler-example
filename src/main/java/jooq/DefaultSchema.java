/*
 * This file is generated by jOOQ.
 */
package jooq;


import java.util.Arrays;
import java.util.List;

import jooq.tables.Message;

import org.jooq.Catalog;
import org.jooq.Table;
import org.jooq.impl.DSL;
import org.jooq.impl.SchemaImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes", "this-escape" })
public class DefaultSchema extends SchemaImpl {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>DEFAULT_SCHEMA</code>
     */
    public static final DefaultSchema DEFAULT_SCHEMA = new DefaultSchema();

    /**
     * The table <code>message</code>.
     */
    public final Message MESSAGE = Message.MESSAGE;

    /**
     * No further instances allowed
     */
    private DefaultSchema() {
        super(DSL.name(""), null, DSL.comment(""));
    }


    @Override
    public Catalog getCatalog() {
        return DefaultCatalog.DEFAULT_CATALOG;
    }

    @Override
    public final List<Table<?>> getTables() {
        return Arrays.asList(
            Message.MESSAGE
        );
    }
}
