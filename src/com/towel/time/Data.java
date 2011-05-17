package com.towel.time;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Componente para validacao de datas
 */
public class Data {
	private static SimpleDateFormat DataFormatador = new SimpleDateFormat(
			"dd/MM/yyyy");

	/**
	 * Verifica se string informada � uma data.
	 * 
	 * @param String
	 *            - data
	 * @return boolean
	 */
	public final static boolean isDate(String psDt) {
		Integer Dia;
		Integer Mes;
		Integer Ano;
		Date DataConv;
		Date DataLimiteInf;
		Date DataLimiteSup;
		String a = psDt.trim();
		if (a.length() != 10)
			return false;

		try {
			Dia = new Integer(a.substring(0, 2));
			Mes = new Integer(a.substring(3, 5));
			Ano = new Integer(a.substring(6));
		} catch (Exception ex) {
			return false;
		}

		if (Mes.intValue() > 12)
			return false;

		if (Mes.intValue() == 2) {
			if (Ano.intValue() % 4 == 0) // Bisexto
			{
				if (Dia.intValue() > 29)
					return false;
			} else if (Dia.intValue() > 28)
				return false;
		} else if (Dia.intValue() > 31)
			return false;
		else if (Dia.intValue() > 30
				&& (Mes.intValue() == 4 || Mes.intValue() == 6
						|| Mes.intValue() == 9 || Mes.intValue() == 11))
			return false;

		if (Dia.intValue() > 31)
			return false;

		try {
			DataConv = DataFormatador.parse(a);
			DataLimiteInf = DataFormatador.parse("01/01/1900");
			DataLimiteSup = DataFormatador.parse("06/06/2079");
		} catch (Exception e) {
			return false;
		}

		if (DataConv.after(DataLimiteSup))
			return false;

		if (DataConv.before(DataLimiteInf))
			return false;

		return true;
	}

	/**
	 * Converte string para data.
	 * 
	 * @param String
	 *            - data
	 * @param String
	 *            - formato
	 * @return Date
	 */
	public final static Date cvDate(String psDt, String psFormato) {
		SimpleDateFormat DtFormatador = new SimpleDateFormat(psFormato);
		try {
			Date DataConv = DtFormatador.parse(psDt);
			return DataConv;
		} catch (Exception e)// ParseException
		{
			return new Date(0);
		}
	}

	public final static Date cvDate(String psDt, SimpleDateFormat DtFormatador) {
		try {
			Date DataConv = DtFormatador.parse(psDt);
			return DataConv;
		} catch (Exception e) {
			return new Date(0);
		}
	}

	public final static Date cvDate(String psDt) {
		try {
			Date DataConv = DataFormatador.parse(psDt);
			return DataConv;
		} catch (Exception e) {
			return new Date(0);
		}
	}

	public final static String format(Date pDt) {
		return DataFormatador.format(pDt);
	}

	public final static String format(Date pDt, String psFormato) {
		SimpleDateFormat DtFormatador = new SimpleDateFormat(psFormato);
		return DtFormatador.format(pDt);
	}

	/**
	 * Converte a data para MM/DD/YYYY
	 * 
	 * @param String
	 *            - Data DD/MM/YYYY
	 * @return Sting - Data MM/DD/YYYY
	 */
	public final static String TextMMDDYYYY(String pdata) {
		String aux;
		if (!pdata.equals("__/__/____") && !pdata.equals(""))
			aux = pdata.substring(3, 5) + "/" + pdata.substring(0, 2) + "/"
					+ pdata.substring(6);
		else
			aux = "";
		// aux = "null";
		return aux;
	}

	public static long msDate(Date pData) {
		return (pData.getTime() + (long) 2209154400000L) / 1000 / 60 / 60 / 24;
	}

	public static long msDate(String pData) {
		Date d = cvDate(pData);
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
