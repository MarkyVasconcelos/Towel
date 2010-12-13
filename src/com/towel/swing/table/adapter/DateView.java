package com.towel.swing.table.adapter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author Vinicius Godoy
 */
public class DateView implements Comparable<DateView>
{
    private Date date;
    private DateFormat format;

    public DateView(Date date)
    {
        this(date, DateFormat.getDateInstance(DateFormat.DEFAULT));
    }

    public DateView(Calendar cal)
    {
        this(cal, DateFormat.getDateInstance(DateFormat.DEFAULT));
    }

    public DateView(Date date, String format)
    {
        this(date, new SimpleDateFormat(format));
    }

    public DateView(Calendar cal, String format)
    {
        this(cal.getTime(), new SimpleDateFormat(format));
    }

    public DateView(Date date, DateFormat format)
    {
        this.date = date;
        this.format = format;
    }

    public DateView(Calendar cal, DateFormat format)
    {
        this(cal.getTime(), format);
    }

    public int compareTo(DateView o)
    {
        return date.compareTo(o.date);
    }

    @Override
    public String toString()
    {
        return format.format(date);
    }

    @Override
    public int hashCode()
    {
        return (date == null) ? 0 : toString().hashCode();
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;

        if (obj == null)
            return false;

        if (getClass() != obj.getClass())
            return false;

        final DateView other = (DateView) obj;

        return toString().equals(other.toString());
    }
}
