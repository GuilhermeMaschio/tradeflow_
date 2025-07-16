package br.com.tradeflow.util;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.leftPad;

public class SplitFileUtils {

	public static List<File> dividirArquivo(File arquivo, File destino, int quantidadeLinhasSplit, long tamanhoLimiteArquivoSplit, boolean temHeader) {

		List<File> arquivosDivididos = new ArrayList<>();

		String fileName = arquivo.getName();
		String extensao = DummyUtils.getExtensao(fileName);
		fileName = fileName.substring(0, fileName.lastIndexOf("."));

		BufferedWriter bw = null;
		try (BufferedReader bf = new BufferedReader(new InputStreamReader(new FileInputStream(arquivo), StandardCharsets.ISO_8859_1))) {

			String primeiraLinha = temHeader ? bf.readLine() : null;

			int contArquivos = 1;
			int contLinhas = 0;
			String line;
			File arquivoDestino = null;
			while ((line = bf.readLine()) != null) {

				if (bw == null
						|| atingiuLimiteDeLinhas(quantidadeLinhasSplit, contLinhas)
						|| atingiuTamanhoLimite(tamanhoLimiteArquivoSplit, arquivoDestino)) {

					if (bw != null) {
						bw.flush();
						bw.close();
					}

					String novoNum = String.valueOf(contArquivos++);
					novoNum = leftPad(novoNum, 4, "0");
					arquivoDestino = new File(destino, fileName + " (" + novoNum + ")." + extensao);

					arquivosDivididos.add(arquivoDestino);

					bw = new BufferedWriter(new PrintWriter(arquivoDestino, StandardCharsets.ISO_8859_1.toString()));

					if(temHeader) {
						bw.append(primeiraLinha).append("\n");
						contLinhas = 1;
					}
					else {
						contLinhas = 0;
					}
				}

				bw.append(line).append("\n");
				contLinhas++;
			}
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		finally {

			if (bw != null) {
				try {
					bw.flush();
					bw.close();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		}

		return arquivosDivididos;
	}

	private static boolean atingiuLimiteDeLinhas(int quantidadeLinhasSplit, int contLinhas) {
		return contLinhas >= quantidadeLinhasSplit;
	}

	private static boolean atingiuTamanhoLimite(long tamanhoLimiteArquivoSplit, File arquivo) {
		long tamenhoLimiteComMargem = tamanhoLimiteArquivoSplit - 500 * 1024; //-500KB para dar margem de segurança no split;
		boolean arquivoAtingiuTamanhoLimite = arquivo != null && arquivo.length() >= tamenhoLimiteComMargem;
		return arquivoAtingiuTamanhoLimite;
	}
}
