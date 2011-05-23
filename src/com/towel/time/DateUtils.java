package com.towel.time;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Componente para validacao de datas
 */
public class DateUtils {
	private static SimpleDateFormat formatter = new SimpleDateFormat(
			"dd/MM/yyyy");

	/**
	 * Verifica se string informada � uma data.
	 * 
	 * @param String
	 *            - data
	 * @return boolean
	 */
	public final static boolean isValidDate(String psDt) {
		Integer dia;
		Integer mes;
		Integer ano;
		Date dataConv;
		Date dataLimiteInf;
		Date dataLimiteSup;
		String a = psDt.trim();
		if (a.length() != 10)
			return false;

		try {
			dia = new Integer(a.substring(0, 2));
			mes = new Integer(a.substring(3, 5));
			ano = new Integer(a.substring(6));
		} catch (Exception ex) {
			return false;
		}

		if (mes.intValue() > 12)
			return false;

		if (mes.intValue() == 2) {
			if (ano.intValue() % 4 == 0) {
				if (dia.intValue() > 29)
					return false;
			} else if (dia.intValue() > 28)
				return false;
		} else if (dia.intValue() > 31)
			return false;
		else if (dia.intValue() > 30
				&& (mes.intValue() == 4 || mes.intValue() == 6
						|| mes.intValue() == 9 || mes.intValue() == 11))
			return false;

		if (dia.intValue() > 31)
			return false;

		try {
			dataConv = formatter.parse(a);
			dataLimiteInf = formatter.parse("01/01/1900");
			dataLimiteSup = formatter.parse("06/06/2079");
		} catch (Exception e) {
			return false;
		}

		if (dataConv.after(dataLimiteSup))
			return false;

		if (dataConv.before(dataLimiteInf))
			return false;

		return true;
	}

	/**
	 * Formate a String and return a Date.
	 * 
	 * @param String
	 *            - date
	 * @param String
	 *            - format
	 * @return Date
	 */
	public final static Date parseDate(String dateString, String psFormato) {
		return parseDate(dateString, new SimpleDateFormat(psFormato));
	}

	public final static Date parseDate(String dateString,
			SimpleDateFormat formatter) {
		try {
			return formatter.parse(dateString);
		} catch (Exception e) {
			return new Date(0);
		}
	}

	/**
	 * Format a String with the defult formatter "dd/MM/yyyy"
	 * 
	 * @param psDt
	 * @return
	 */
	public final static Date parseDate(String psDt) {
		return parseDate(psDt, formatter);
	}

	public final static String format(Date pDt) {
		return formatter.format(pDt);
	}

	public final static String format(Date pDt, String psFormato) {
		return new SimpleDateFormat(psFormato).format(pDt);
	}

	/**
	 * Converte a data para MM/DD/YYYY
	 * 
	 * @param String
	 *            - Data DD/MM/YYYY
	 * @return Sting - Data MM/DD/YYYY
	 */
	public final static String textToMMDDYYYY(String pdata) {
		String aux = "";
		if (!pdata.equals("__/__/____") && !pdata.equals(""))
			aux = pdata.substring(3, 5) + "/" + pdata.substring(0, 2) + "/"
					+ pdata.substring(6);
		return aux;
	}

	public static long msDate(Date pData) {
		return (pData.getTime() + (long) 2209154400000L) / 1000 / 60 / 60 / 24;
	}

	public static long msDate(String pData) {
		Date d = parseDate(pData);
		return (d.getTime() + (long) 2209154400000L) / 1000 / 60 / 60 / 24;
	}

	/**
	 * Metodo para gravar campos do tipo datetime no sql no formato MM/dd/yyyy
	 * HH:mm:ss:SSS com a data atual
	 */
	public static String getDateTime() {
		java.text.SimpleDateFormat formatador = new java.text.SimpleDateFormat(
				"yyyy/MM/dd HH:mm:ss:SSS");
		java.util.Date x = new java.util.Date();
		return formatador.format(x);
	}

	public static String getDateTimeSegundos() {
		java.text.SimpleDateFormat formatador = new java.text.SimpleDateFormat(
				"dd/MM/yyyy HH:mm:ss");
		java.util.Date x = new java.util.Date();
		return formatador.format(x);
	}
}
