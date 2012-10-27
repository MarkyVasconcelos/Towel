package com.towel.swing.table;

import java.awt.Component;
import java.awt.FontMetrics;

import javax.swing.JTable;
import javax.swing.table.TableColumn;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

/**
 * 
 * 
 * @author Arthur Gregorio
 * @version 1.0.0
 * @since Release 1.0.0
 */

public class Resizer {

	/**
	 * Método que ajuasta a coluna conforme da JTable conforme o tamanho do
	 * conteúdo carregado no model
	 * 
	 * @param coluna
	 *            Recebe a coluna que sera redimensionada
	 * @param margin
	 *            espaço de bonus colocado caso a contagem dos campos esteja
	 *            errada
	 * @param tabela
	 *            tabela que será implementada o redimensionamento
	 */

	public static void fitColumn(int coluna, int margin, JTable tabela) {
		int width = 0;

		TableColumnModel colModel = tabela.getColumnModel();
		TableColumn col = colModel.getColumn(coluna);

		/** Obtem a largura do cabecalho */
		TableCellRenderer renderer = col.getHeaderRenderer();

		if (renderer == null) {
			renderer = tabela.getTableHeader().getDefaultRenderer();
		}

		Component comp = renderer.getTableCellRendererComponent(tabela,
				col.getHeaderValue(), false, false, 0, 0);
		width = comp.getPreferredSize().width;

		for (int r = 0; r < tabela.getRowCount(); r++) {
			renderer = tabela.getCellRenderer(r, coluna);
			comp = renderer.getTableCellRendererComponent(tabela,
					tabela.getValueAt(r, coluna), false, false, r, coluna);
			width = Math.max(width, comp.getPreferredSize().width);
		}

		width += 2 * margin;
		col.setPreferredWidth(width);
	}

	public static void fitColumnsByHeader(int margin, JTable table) {
		TableColumnModel colModel = table.getColumnModel();
		for (int i = 0; i < colModel.getColumnCount(); i++)
			fitColumnByHeader(i, margin, table);
	}

	public static void fitColumnByHeader(int column, int margin, JTable table) {
		TableColumnModel colModel = table.getColumnModel();
		TableColumn col = colModel.getColumn(column);
		String x = col.getHeaderValue().toString();
		FontMetrics metrics = table.getFontMetrics(table.getFont());
		int width = metrics.stringWidth(x);
		col.setMinWidth(width);
//		col.setPreferredWidth(width);
	}

	public static void fitAllColumns(JTable table) {
		TableColumnModel colModel = table.getColumnModel();
		for (int i = 0; i < colModel.getColumnCount(); i++)
			fitColumn(i, 0, table);
	}

}
