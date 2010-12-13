package com.towel.swing.table.headerpopup;

import java.util.EventListener;

/**
 * @author Vinicius Godoy
 */
public interface HeaderPopupListener extends EventListener
{
    void elementSelected(HeaderPopupEvent e);
}
