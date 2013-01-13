package org.cognitor.server.hibernate.type;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.usertype.UserType;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

/**
 * This class is used to so that Hibernate can generate a numerical sequence
 * in the database but the value is stored in a String field in the entity.
 *
 * This is required for Id values that are Strings because they might be used
 * with other persistence solutions which do not require an id to be numerical
 * but Hibernate can not create a String sequence generator within a database.
 *
 * @author Patrick Kranz
 */
public class StringLongType implements UserType {
    @Override
    public int[] sqlTypes() {
        return new int[] {Types.BIGINT};
    }

    @Override
    public Class returnedClass() {
        return String.class;
    }

    @Override
    public boolean equals(Object x, Object y) throws HibernateException {
        return !((x == null ^ y == null) || x == null)  && x.equals(y);
    }

    @Override
    public int hashCode(Object x) throws HibernateException {
        if (x == null)
            throw new HibernateException("Can not calculate hash code for null value");
        return x.hashCode();
    }

    @Override
    public Object nullSafeGet(ResultSet rs, String[] names, SessionImplementor session, Object owner) throws HibernateException, SQLException {
        long persistentValue = rs.getLong(names[0]);
        return Long.toString(persistentValue);
    }

    @Override
    public void nullSafeSet(PreparedStatement st, Object value, int index, SessionImplementor session) throws HibernateException, SQLException {
        try {
            st.setLong(index, Long.parseLong((String) value));
        } catch (NumberFormatException exception) {
            throw new HibernateException("Unable to parse value to long: " + value, exception);
        }
    }

    @Override
    public Object deepCopy(Object value) throws HibernateException {
        return value;
    }

    @Override
    public boolean isMutable() {
        return false;
    }

    @Override
    public Serializable disassemble(Object value) throws HibernateException {
        return (Serializable) deepCopy(value);
    }

    @Override
    public Object assemble(Serializable cached, Object owner) throws HibernateException {
        return deepCopy(cached);
    }

    @Override
    public Object replace(Object original, Object target, Object owner) throws HibernateException {
        return original;
    }
}
