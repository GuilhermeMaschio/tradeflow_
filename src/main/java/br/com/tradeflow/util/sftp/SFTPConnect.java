package br.com.tradeflow.util.sftp;

import com.jcraft.jsch.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SFTPConnect {
	private String host;
	private String username;
	private String password;
	private ProxyHTTP proxy;
	private Session session = null;
	private Channel channel = null;
	private ChannelSftp channelSftp = null;

	public SFTPConnect(String host, String username, String password, ProxyHTTP proxy ){
		this.host = host;
		this.proxy = proxy;
		this.username = username;
		this.password = password;
		this.connect();
	}

	private void connect(){
		try{
			JSch ssh = new JSch();
			session = ssh.getSession(username, host, 22);
			session.setPassword(password);

			if(this.proxy != null) {
				session.setProxy(proxy);
			}

			session.setConfig("StrictHostKeyChecking", "no");
			session.connect();
			channel = session.openChannel("sftp");
			channel.connect();
			channelSftp = (ChannelSftp)channel;
		}
		catch(Exception ex){
			Logger.getLogger(SFTPConnect.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public void close(){
		if (channel != null) {
			channel.disconnect();
		}
		if (session != null) {
			session.disconnect();
		}
	}

	public List<String> listFiles(String diretorio){
		List<String> retorno = new ArrayList<>();
		try {
			channelSftp.cd(diretorio);
			Vector filelist = channelSftp.ls(diretorio);
			for(int i=0; i<filelist.size();i++){
				ChannelSftp.LsEntry entry = (ChannelSftp.LsEntry) filelist.get(i);
				String filename = entry.getFilename();
				if(!filename.equals(".") && !filename.equals("..")) {
					retorno.add(filename);
				}
			}
			return retorno;
		}
		catch(Exception ex){
			Logger.getLogger(SFTPConnect.class.getName()).log(Level.SEVERE, null, ex);
		}
		return null;
	}

	public void moveFile(String path, String destino){
		Session session;
		Channel channel;
		StringBuilder outputBuffer = new StringBuilder();
		try{
			JSch ssh = new JSch();
			// ssh.setKnownHosts("src/test/resources/files/priv");
			session = ssh.getSession(username, host, 22);
			session.setPassword(password);
			session.setConfig("StrictHostKeyChecking", "no");
			session.connect();
			channel = session.openChannel("exec");
			ChannelExec channelExec = (ChannelExec) channel;
			channelExec.setPty(true);
			channelExec.setCommand("sudo cp "+path+" "+destino);
			channelExec.setErrStream(System.err);
			InputStream commandOutput = channel.getInputStream();
			channel.connect();

			int readByte = commandOutput.read();

			while(readByte != 0xffffffff) {
				outputBuffer.append((char)readByte);
				readByte = commandOutput.read();
			}

			channel.disconnect();
			session.disconnect();

		}
		catch(Exception ex){
			Logger.getLogger(SFTPConnect.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public SftpATTRS getAtributos(String path){
		try {
			return channelSftp.lstat(path);
		} catch (SftpException e) {
			throw new RuntimeException(e);
		}
	}

	public void downloadFile(String path, String destino){

		try {
			OutputStream output = new FileOutputStream(destino);
			channelSftp.get(path, output);
			output.close();
		}
		catch (Exception e){
			Logger.getLogger(SFTPConnect.class.getName()).log(Level.SEVERE, null, e);
		}
	}

	public void uploadFile(String path, String destino){
		try {
			channelSftp.cd(path);
			File file = new File(destino);
			channelSftp.put(new FileInputStream(file), file.getName());
		}
		catch (Exception e){
			Logger.getLogger(SFTPConnect.class.getName()).log(Level.SEVERE, null, e);
		}
	}
}
