package br.com.tradeflow.util;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.jetbrains.annotations.NotNull;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class PdfUtil {

	public static List<BufferedImage> extrairPaginasPDF(InputStream inputStream) throws IOException {

		List<BufferedImage> imagens = new ArrayList<>();

		try (PDDocument document = PDDocument.load(inputStream)) {
			PDFRenderer pdfRenderer = new PDFRenderer(document);
			for (int page = 0; page < document.getNumberOfPages(); ++page) {
				BufferedImage bImage = pdfRenderer.renderImageWithDPI(page, 300); // Ajuste o DPI conforme necessário
				imagens.add(bImage);
			}
		}

		return imagens;
	}

	public static String extrairTexto(InputStream is) throws IOException {
		PdfReader reader = new PdfReader(is);
		return extrairTexto(reader, null, null);
	}

	@NotNull
	public static String extrairTexto(PdfReader reader, Integer paginaInicial, Integer paginaFinal) throws IOException {

		int pages = reader.getNumberOfPages();
		StringBuilder textoSb = new StringBuilder();
		for (int i = 1; i <= pages; i++) {

			if(paginaInicial != null && i < paginaInicial) {
				continue;
			}
			if(paginaFinal != null && i > paginaFinal) {
				continue;
			}

			String pageText = PdfTextExtractor.getTextFromPage(reader, i);
			textoSb.append(pageText).append("\n");
		}
		reader.close();

		String texto = textoSb.toString();
		return texto;
	}
}
