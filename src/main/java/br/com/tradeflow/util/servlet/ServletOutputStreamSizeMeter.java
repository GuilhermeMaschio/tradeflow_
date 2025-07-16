package br.com.tradeflow.util.servlet;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.WriteListener;

import java.io.IOException;
import java.io.OutputStream;

public class ServletOutputStreamSizeMeter extends ServletOutputStream {

	private OutputStream outputStream;
	private int sizeCount;

	public ServletOutputStreamSizeMeter(OutputStream outputStream) {
		this.outputStream = outputStream;
	}

	@Override
	public void write(int b) throws IOException {
		outputStream.write(b);
		sizeCount++;
	}

	public int getSizeCount() {
		return sizeCount;
	}

	@Override
	public boolean isReady() {
		return false;
	}

	@Override
	public void setWriteListener(WriteListener writeListener) {}
}
