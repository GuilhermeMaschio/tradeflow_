package br.com.tradeflow.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import br.com.tradeflow.domain.entity.LogAcesso;
import br.com.tradeflow.exception.MessageKeyException;
import br.com.tradeflow.util.json.ObjectMapper;
import com.jcraft.jsch.SftpATTRS;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.io.*;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Log4j2
public class DummyUtils {

	private static final Locale LOCALE_PT_BR = new Locale("pt", "BR");
	private static final NumberFormat KILOBYTE_NF = NumberFormat.getNumberInstance(LOCALE_PT_BR);
	private static final NumberFormat MEGABYTES_NF = NumberFormat.getNumberInstance(LOCALE_PT_BR);
	private static final String DATE_TIME_FORMAT_FILE = "yyyy.MM.dd-HH.mm";
	private static final String DATE_FORMAT = "dd/MM/yyyy";
	private static final String TIME_FORMAT = "HH:mm";
	private static final String DATE_TIME_FORMAT_2 = "dd/MM/yyyy HH:mm:ss";

	public static String getExtensao(String fileName) {

		fileName = StringUtils.trim(fileName);
		if(StringUtils.isBlank(fileName)) {
			return null;
		}

		int lastIndexOf = fileName.lastIndexOf('.');
		if(lastIndexOf < 0) {
			return null;
		}

		String extensao = fileName.substring(lastIndexOf + 1, fileName.length());
		extensao = StringUtils.lowerCase(extensao);
		return extensao;
	}

	public static String getExceptionMessage(Throwable e) {
		String message = e.getMessage();
		String rootCauseMessage = ExceptionUtils.getRootCauseMessage(e);
		if(rootCauseMessage != null) {
			if(message == null || rootCauseMessage.endsWith(message)) {
				if(e instanceof MessageKeyException) {
					Object[] args = ((MessageKeyException) e).getArgs();
					rootCauseMessage += " " + (args != null ? Arrays.asList((Object[]) args) : "");
				}
				return rootCauseMessage;
			}
			else if(!rootCauseMessage.equals(message)) {
				return message + " Caused by: " + rootCauseMessage;
			}
		}
		return message;
	}

	public static void mudarNomeThread(String nomeThread) {
		Thread current = Thread.currentThread();
		int identityHashCode = System.identityHashCode(current);
		current.setName(nomeThread + "-" + identityHashCode);
	}

	public static String getServer() {

		String server = System.getProperty("getdoc.server");
		if(StringUtils.isNotBlank(server)) {
			return server;
		}

		try {
			File file = new File("/etc/hostname");
			if(file.exists()) {
				FileReader fr = new FileReader(file);
				BufferedReader br = new BufferedReader(fr);
				server = br.readLine();
				br.close();
			}
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}

		if(server == null) {
			server = "localhost";
		}

		return server;
	}

	public static String objectToJson(Object obj) {
		if(obj == null) {
			return null;
		}
		ObjectMapper om = new ObjectMapper();
		String json = om.writeValueAsString(obj);
		return json;
	}

	public static Map<?, ?> jsonStringToMap(String json) {
		if(StringUtils.isBlank(json)) {
			return null;
		}
		Map<?, ?> map = null;
		map = new ObjectMapper().readValue(json, LinkedHashMap.class);
		return map;
	}

	public static <T> T jsonToObject(String jsonAsString, Class<T> clazz) {
		if(StringUtils.isNotBlank(jsonAsString)) {
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			T obj = mapper.readValue(jsonAsString, clazz);
			return obj;
		}
		return null;
	}

	public static Object getParameter(LogAcesso logAcesso, String paramName) {

		if (logAcesso == null) {
			return null;
		}

		String parameters = logAcesso.getParameters();
		Map<Object, Object> paramsMap = (Map<Object, Object>) DummyUtils.jsonStringToMap(parameters);
		if(paramsMap == null || paramsMap.isEmpty()) {
			return null;
		}

		return paramsMap.get(paramName);
	}

	public static void addParameter(LogAcesso logAcesso, String paramName, Object value) {

		if(logAcesso == null) {
			return;
		}

		String parameters = logAcesso.getParameters();
		Map<Object, Object> paramsMap = (Map<Object, Object>) DummyUtils.jsonStringToMap(parameters);
		paramsMap = paramsMap != null ? paramsMap : new LinkedHashMap<>();

		String valueStr = value != null ? value.toString() : "";
		valueStr = valueStr.replace("\"", "'");

		paramsMap.put(paramName, valueStr);

		String parameters2 = DummyUtils.objectToJson(paramsMap);
		logAcesso.setParameters(parameters2);
	}

	public static String toFileSize(long size) {
		double d = size / 1024d / 1024d;
		if((int) d > 0) {
			return toMegabytes(size) + " MB";
		}
		d = size / 1024d;
		if((int) d > 0) {
			return toKilobyte(size) + " KB";
		}
		return size + " B";
	}

	public static String toMegabytes(long size) {
		double mb = size / 1024d / 1024d;
		String mbStr = MEGABYTES_NF.format(mb);
		return mbStr;
	}

	public static String toKilobyte(long size) {
		double kb = size / 1024d;
		String kbStr = KILOBYTE_NF.format(kb);
		return kbStr;
	}

	public static Date parse(String valor, String pattern) {
		return parse(valor, new SimpleDateFormat(pattern));
	}

	public static Date parse(String valor, SimpleDateFormat dateTimeFormat3) {
		if(StringUtils.isBlank(valor)) {
			return null;
		}
		try {
			return dateTimeFormat3.parse(valor);
		}
		catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

	public static String format(Date data, String pattern) {
		return format(data, new SimpleDateFormat(pattern));
	}

	private static String format(Date data, SimpleDateFormat dateTimeFormat) {
		if(data == null) {
			return null;
		}
		return dateTimeFormat.format(data);
	}

	public static Date parseDateTimeFile(String valor) {
		return parse(valor, DATE_TIME_FORMAT_FILE);
	}

	public static String formatDateTimeFile(Date data) {
		return format(data, DATE_TIME_FORMAT_FILE);
	}

	public static String formatDateTime2(Date data) {
		return format(data, DATE_TIME_FORMAT_2);
	}

	public static Date parseDate(String valor) {
		return parse(valor, DATE_FORMAT);
	}

	public static String formatDate(Date data) {
		return format(data, DATE_FORMAT);
	}

	public static Date parseTime(String valor) {
		return parse(valor, TIME_FORMAT);
	}

	public static String formatTime(Date data) {
		return format(data, TIME_FORMAT);
	}

	public static <T, Z> ResultadoAsync<T, Z> verificarFinalExecucao(Map<CompletableFuture<T>, Z> futures, LogAcesso logA) {

		Map<Z, T> results = new LinkedHashMap<>();
		AtomicInteger sucessos = new AtomicInteger();
		Map<Z, Exception> erros = new LinkedHashMap<>();
		List<Z> elementos = new ArrayList<>(futures.values());

		futures.entrySet().forEach(entry ->{
			CompletableFuture<T> future = entry.getKey();
			Z elemento = entry.getValue();

			try {
				T result = future.get();
				results.put(elemento, result);
				sucessos.incrementAndGet();
			} catch (Exception e) {

				String exceptionMessage = getExceptionMessage(e);
				log.error("erro na execução do método assíncrono: " + exceptionMessage, e);

				erros.put(elemento, e);
			}
		});

		DummyUtils.addParameter(logA, "sucessos", sucessos.get());
		DummyUtils.addParameter(logA, "erros", erros.size());

		registrarExeptions(erros, logA);

		ResultadoAsync<T, Z> resultadoAsync = new ResultadoAsync<T, Z>();
		resultadoAsync.setElementos(elementos);
		resultadoAsync.setErrosMap(erros);
		resultadoAsync.setRetornosMap(results);
		resultadoAsync.setSucessos(sucessos.get());
		return resultadoAsync;
	}

	public static Date getDataAlteracaoFtp(SftpATTRS atributos) {
		int dataAlteracaoInt = atributos.getMTime();
		Date dataAlteracao = new Date(dataAlteracaoInt * 1000L);
		return dataAlteracao;
	}

	@Data
	public static final class ResultadoAsync<T, Z> {

		private List<Z> elementos;
		private Map<Z, T> retornosMap;
		private Map<Z, Exception> errosMap;
		private int sucessos;
	}

	private static <Z> void registrarExeptions(Map<Z, Exception> erros, LogAcesso log) {
		if(!erros.isEmpty()) {
			StringBuilder exceptions = new StringBuilder();
			for (Map.Entry<Z, Exception> entry : erros.entrySet()) {
				Z key = entry.getKey();
				Exception e = entry.getValue();
				String stackTrace = ExceptionUtils.getStackTrace(e);

				exceptions.append("erro ao processar ").append(key).append(":\n").append(stackTrace).append("\n\n");
			}

			log.setException(exceptions.toString());
		}
	}

	public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
		return sortByValue(map, Map.Entry.comparingByValue());
	}

	public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map, Comparator<? super Map.Entry<K, V>> comparator) {
		List<Map.Entry<K, V>> list = new ArrayList<>(map.entrySet());
		list.sort(comparator);
		Map<K, V> result = new LinkedHashMap<>();
		for (Map.Entry<K, V> entry : list) {
			result.put(entry.getKey(), entry.getValue());
		}
		return result;
	}

	public static boolean isCepValido(String cep) {
		String regex = "\\d{5}-\\d{3}";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(cep);
		return matcher.matches();
	}
}
