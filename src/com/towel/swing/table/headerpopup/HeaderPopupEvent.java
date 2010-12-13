package com.towel.swing.table.headerpopup;

import java.util.EventObject;

/**
 * @author Vinicius Godoy
 */
public class HeaderPopupEvent extends EventObject
{
    private int modelIndex;

    public HeaderPopupEvent(Object source, int modelIndex)
    {
        super(source);
        this.modelIndex = modelIndex;
    }

    public int getModelIndex()
    {
        return modelIndex;
    }
}
