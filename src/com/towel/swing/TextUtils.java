package com.towel.swing;

import java.awt.FontMetrics;
import java.awt.Point;

import javax.swing.JScrollPane;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.JTextComponent;
import javax.swing.text.View;

/**
 * Util static methods to becomes easy operation with Text Components. All
 * functionalities are available to JTextComponent as well as a Document.
 * 
 * @author Farina, Andre
 */
public class TextUtils
{

    /**
     * Returns the line number of the caret in a text component.
     * 
     * @param txtCmp Text Component
     * @return Line number
     */
    public static int getCaretLine(JTextComponent txtCmp)
    {
        return getLine(txtCmp.getDocument(), txtCmp.getCaretPosition());
    }

    /**
     * Given an offset, returns the line number of a text component.
     * 
     * @param txtCmp Text Component
     * @param offset Offset position
     * @return Line number starting in 0.
     */
    public static int getLine(JTextComponent txtCmp, int offset)
    {
        return getLine(txtCmp.getDocument(), offset);
    }

    /**
     * Given an offset, returns the line number in a text component.
     * 
     * @param doc JTextComponent document
     * @param offset Offset position
     * @return Line number starting in 0.
     */
    public static int getLine(Document doc, int offset)
    {
        Element root = doc.getDefaultRootElement();
        return root.getElementIndex(offset);
    }

    /**
     * Given an offset, returns the column number in a text component.
     * 
     * @param offset Offset position
     * @return Column number starting in 0.
     */
    public static int getCaretColumn(JTextComponent txtCmp)
    {
        return getColumn(txtCmp, txtCmp.getCaretPosition());

    }

    /**
     * Given an offset, returns the column number in a text component.
     * 
     * @param offset Offset position
     * @return Column number starting in 0.
     */
    public static int getColumn(JTextComponent txtCmp, int offset)
    {
        return txtCmp.getCaretPosition() - getOffsetStartLine(txtCmp, offset);
    }

    /**
     * Given an offset, returns the offset where the line begins.
     * 
     * @param offset Offset position.
     * @return Offset where the line begins.
     */
    public static int getOffsetStartLine(JTextComponent txtCmp, int offset)
    {
        return getOffsetStartLine(txtCmp.getDocument(), offset);
    }

    /**
     * Given an offset, returns the offset where the line begins.
     * 
     * @param doc JTextComponent document
     * @param offset Offset position.
     * @return Offset where the line begins.
     */
    public static int getOffsetStartLine(Document doc, int offset)
    {
        int line = getLine(doc, offset);
        return getWhereLineStarts(doc, line);
    }

    /**
     * Given an offset, returns the offset where the line ends.
     * 
     * @param offset Offset position.
     * @return Offset where the line ends.
     */
    public static int getOffsetEndLine(JTextComponent txtCmp, int offset)
    {
        return getOffsetEndLine(txtCmp.getDocument(), offset);
    }

    /**
     * Given an offset, returns the offset where the line ends.
     * 
     * @param doc JTextComponent document
     * @param offset Offset position.
     * @return Offset where the line ends.
     */
    public static int getOffsetEndLine(Document doc, int offset)
    {
        int line = getLine(doc, offset);
        return getWhereLineEnds(doc, line);
    }

    /**
     * Given a line returns the offset number where the line starts.
     * 
     * @param line Line number
     * @return Offset number
     */
    public static int getWhereLineStarts(JTextComponent txtCmp, int line)
    {
        return getWhereLineStarts(txtCmp.getDocument(), line);
    }

    /**
     * Given a line returns the offset number where the line starts.
     * 
     * @param doc JTextComponent document
     * @param line Line number
     * @return Offset number
     */
    public static int getWhereLineStarts(Document doc, int line)
    {
        Element el = doc.getDefaultRootElement().getElement(line);
        if (el != null)
            return el.getStartOffset();

        return doc.getStartPosition().getOffset();
    }

    /**
     * Given a line returns the offset number where the line ends.
     * 
     * @param line Line number
     * @return Offset number
     */
    public static int getWhereLineEnds(JTextComponent txtCmp, int line)
    {
        return getWhereLineEnds(txtCmp.getDocument(), line);
    }

    /**
     * Given a line returns the offset number where the line ends.
     * 
     * @param doc JTextComponent document
     * @param line Line number
     * @return Offset number
     */
    public static int getWhereLineEnds(Document doc, int line)
    {
        Element el = doc.getDefaultRootElement().getElement(line);
        if (el != null)
            return el.getEndOffset();

        return doc.getEndPosition().getOffset();
    }

    /**
     * Count the line number.
     * 
     * @return Amount of lines.
     */
    public static int countLines(JTextComponent txtCmp)
    {
        return countLines(txtCmp.getDocument());
    }

    /**
     * Count the line number.
     * 
     * @param doc JTextComponent document
     * @return Amount of lines.
     */
    public static int countLines(Document doc)
    {
        return doc.getDefaultRootElement().getElementCount();
    }

    /**
     * Get number of rows in the content being shown. Independent of the text's
     * size.
     * 
     * @return Amount of rows.
     */
    public static int getRows(JTextComponent txtCmp)
    {
        FontMetrics fm = txtCmp.getFontMetrics(txtCmp.getFont());
        int fontHeight = fm.getHeight();

        int ybaseline = txtCmp.getY() + fm.getAscent();
        int yend = ybaseline + txtCmp.getHeight();

        int rows = 1;

        while (ybaseline < yend)
        {
            ybaseline += fontHeight;
            rows++;
        }

        return rows;
    }

    /**
     * Get first visible line of the text component in a JScrollPane.
     * 
     * @param txtCmp Text component.
     * @return The line.
     */
    public static int getTopLine(JTextComponent txtCmp)
    {
        FontMetrics fm = txtCmp.getFontMetrics(txtCmp.getFont());
        int fontHeight = fm.getHeight();

        return (Math.abs(txtCmp.getY()) + fm.getAscent()) / fontHeight + 1;
    }

    /**
     * Get last visible line of the text component in a JScrollPane.
     * 
     * @param txtCmp Text component.
     * @return The line.
     */
    public static int getBottomLine(JTextComponent txtCmp)
    {
        FontMetrics fm = txtCmp.getFontMetrics(txtCmp.getFont());
        int fontHeight = fm.getHeight();

        View view = txtCmp.getUI().getRootView(txtCmp).getView(0);
        int height = view.getContainer().getParent().getHeight();

        return (Math.abs(txtCmp.getY()) + fm.getAscent() + height) / fontHeight;
    }

    /**
     * Scroll the pane until offset becomes visible.
     * 
     * @param scroll
     * @param offset
     */
    public static void scrollToVisible(final JScrollPane scroll, int offset)
    {
        if (!(scroll.getViewport().getView() instanceof JTextComponent))
            return;

        JTextComponent txtCmp = (JTextComponent) scroll.getViewport().getView();
        Element root = txtCmp.getDocument().getDefaultRootElement();
        int line = root.getElementIndex(offset);

        scrollLineToVisible(scroll, line);
    }

    /**
     * Scroll the pane until line becomes visible.
     * 
     * @param scroll
     * @param line
     */
    public static void scrollLineToVisible(final JScrollPane scroll, int line)
    {
        if (!(scroll.getViewport().getView() instanceof JTextComponent))
            return;
        
        JTextComponent txtCmp = (JTextComponent) scroll.getViewport().getView();
        FontMetrics fm = txtCmp.getFontMetrics(txtCmp.getFont());
        int fontHeight = fm.getHeight();
        scroll.getViewport().setViewPosition(new Point(0, fontHeight*line));
    }
    
    public static String generateEscapeRegex(String text)
    {
        text = text.replaceAll("\\\\", "\\\\\\\\");
        text = text.replaceAll("\\.", "\\\\.");
        text = text.replaceAll("\\*", "\\\\*");
        text = text.replaceAll("\\?", "\\\\?");
        text = text.replaceAll("\\(", "\\\\(");
        text = text.replaceAll("\\)", "\\\\)");
        text = text.replaceAll("\\[", "\\\\[");
        text = text.replaceAll("\\]", "\\\\]");
        text = text.replaceAll("\\{", "\\\\{");
        text = text.replaceAll("\\}", "\\\\}");
        text = text.replaceAll("\\^", "\\\\^");
        text = text.replaceAll("\\$", "\\\\\\$");
        text = text.replaceAll("\\+", "\\\\+");
        text = text.replaceAll("\\|", "\\\\|");
        return text;
    }
}
