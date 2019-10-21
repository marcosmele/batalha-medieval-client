package br.com.marcosmele.batalha_medieval_client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

public class Batalha {

	private String nickname;

	private Api api;

	private List<String> heroisPossiveis;

	private String idBatalha;
	
	private int iniciativaHeroi;
	private int iniciativaMonstro;
	private String iniciativaVencedor;

	private int dadoAtaqueHeroi;
	private int dadoAtaqueMonstro;
	private String resumoAtaque;
	private int totalDano;
	private int vidaHeroi;
	private int vidaMonstro;
	
	private String proximoPasso;

	public Batalha(String nickname) {
		this.api = new Api();
		this.nickname = nickname;
	}
	
	public void iniciar() throws Exception {
		
		idBatalha = api.post("iniciar", new JSONObject().append("nickname", nickname).toString(), null);
		proximoPasso = "heroi";
		if(idBatalha.contains("error")) {
			System.out.println(idBatalha);
			JSONObject erro = new JSONObject(idBatalha);
			idBatalha = erro.getString("idBatalha");
			proximoPasso = erro.getString("proximoPasso");
		}
		System.out.println(idBatalha);
	}

	public List<String> herois() throws Exception {
		String response = api.get("herois");
		List<String> opcoes = new ArrayList<String>();
		JSONArray json = new JSONArray(response);
		for (int i = 0; i < json.length(); i++) {
			JSONObject jsonobject = json.getJSONObject(i);
			opcoes.add(jsonobject.getString("classe"));
		}
		heroisPossiveis = opcoes;
		return opcoes;
	}

	public boolean heroiValido(int indiceHeroi) {
		try {
			String heroi = heroisPossiveis.get(indiceHeroi);
			return heroi != null;
		} catch (Exception e) {
			return false;
		}
	}

	public void escolher(int indiceHeroi) throws Exception {
		Map<String, String> header = new HashMap<String, String>();
		header.put("ID_BATALHA", idBatalha);
		api.post("heroi", heroisPossiveis.get(indiceHeroi), header);
		proximoPasso = "iniciativa";
	}

	public void iniciativa() throws Exception{
		Map<String,String> header = new HashMap<String,String>();
		header.put("ID_BATALHA", idBatalha);
		String response = api.post("iniciativa", null,header);	
		JSONArray json = new JSONArray(response);
		JSONObject iniciativa = json.getJSONObject(json.length()-1);
		iniciativaHeroi = iniciativa.getJSONObject("lancamentos").getJSONObject("HEROI").getInt("total");
		iniciativaMonstro = iniciativa.getJSONObject("lancamentos").getJSONObject("MONSTRO").getInt("total");
		iniciativaVencedor = (iniciativa.getString("vencedor").equals("HEROI")) ? "VENCEU" : "PERDEU";
		proximoPasso = "ataque";
	}
	
	public void ataque() throws Exception{
		Map<String,String> header = new HashMap<String,String>();
		header.put("ID_BATALHA", idBatalha);
		String response = api.post("ataque", null,header);	
		
		JSONObject ataque = new JSONObject(response);
		dadoAtaqueHeroi = ataque.getJSONObject("dadoHeroi").getInt("total");
		dadoAtaqueMonstro = ataque.getJSONObject("dadoMonstro").getInt("total");
		try {
			resumoAtaque = (ataque.getJSONObject("dano").getInt("total")> 0) ? "SUCESSO" : "FRACASSO";
		} catch (Exception e){
			resumoAtaque = "FRACASSO";
		}
		totalDano = ataque.getInt("totalDano");
		vidaHeroi = ataque.getInt("vidaRestanteHeroi");
		vidaMonstro = ataque.getInt("vidaRestanteMonstro");
		proximoPasso = "iniciativa";
	}
	
	public List<String> ranking() throws Exception{
		String response = api.get("ranking");	
		JSONArray json = new JSONArray(response);
		List<String> ranking = new ArrayList<String>();
		for (int i = 0; i < json.length(); i++) {
			JSONObject posicao = json.getJSONObject(json.length()-1);
			ranking.add((i+1) + "º - " + posicao.getString("nickname") + " - " + posicao.getInt("pontuacao") + " pontos");
		}
		return ranking;
	}
	
	public int getIniciativaHeroi() {
		return iniciativaHeroi;
	}

	public int getIniciativaMonstro() {
		return iniciativaMonstro;
	}

	public String isIniciativaVencedor() {
		return iniciativaVencedor;
	}

	public int getDadoAtaqueHeroi() {
		return dadoAtaqueHeroi;
	}

	public int getDadoAtaqueMonstro() {
		return dadoAtaqueMonstro;
	}

	public String getResumoAtaque() {
		return resumoAtaque;
	}

	public int getTotalDano() {
		return totalDano;
	}

	public int getVidaHeroi() {
		return vidaHeroi;
	}

	public int getVidaMonstro() {
		return vidaMonstro;
	}
	
	public String getProximoPasso() {
		return proximoPasso;
	}

}
