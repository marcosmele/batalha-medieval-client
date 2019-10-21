package br.com.marcosmele.batalha_medieval_client;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class Api {

	private static final String URL_BASE_JOGO = "http://localhost:8080/api/";

	public String post(String metodo, String jsonInput, Map<String,String> headers) throws Exception {
		URL url = new URL(URL_BASE_JOGO + metodo);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("POST");
		con.setRequestProperty("Content-Type", "application/json; utf-8");
		
		if(headers!= null) {
			headers.keySet().forEach(key->{
				con.setRequestProperty(key, headers.get(key));
			});
		}
		
		con.setDoOutput(true);

		if(jsonInput != null) {
			try (OutputStream os = con.getOutputStream()) {
				byte[] input = jsonInput.getBytes("utf-8");
				os.write(input, 0, input.length);
			}
		}
		try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"))) {
			StringBuilder response = new StringBuilder();
			String responseLine = null;
			while ((responseLine = br.readLine()) != null) {
				response.append(responseLine.trim());
			}
			return response.toString();
		} catch (Exception e) {
			return processResponseOutput(con);
		}
	}
	
	private String processResponseOutput(HttpURLConnection con) throws IOException {
		  InputStream in = con.getErrorStream();
		  try {
		    if (in == null) {
		      in = con.getInputStream();
		    }
		    ByteArrayOutputStream result = new ByteArrayOutputStream();
		    byte[] buffer = new byte[1024];
		    int length;
		    while ((length = in.read(buffer)) != -1) {
		      result.write(buffer, 0, length);
		    }
		    return result.toString();
		  } finally {
		    if (in != null) {
		      in.close();
		    }
		  }
		}

	public String get(String metodo) throws Exception {
		URL url = new URL(URL_BASE_JOGO + metodo);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("GET");
		con.setDoOutput(true);

		try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"))) {
			StringBuilder response = new StringBuilder();
			String responseLine = null;
			while ((responseLine = br.readLine()) != null) {
				response.append(responseLine.trim());
			}
			return response.toString();
		}
	}

}
