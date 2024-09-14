/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.rentabilidade.rentabilidademensal;

/**
 *
 * @author Leonardo
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class RentabilidadeMensal {

    private static final int TARGET_YEAR = 2023;
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");

    public static void main(String[] args) {
        Map<String, Double> rentabilidadesMensais = new TreeMap<>();

        try (InputStream is = RentabilidadeMensal.class.getClassLoader().getResourceAsStream("rentabilidades.txt")) {
            if (is == null) {
                System.err.println("Arquivo rentabilidades.txt não encontrado.");
                return;
            }

            try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
                String linha;
                br.readLine();

                while ((linha = br.readLine()) != null) {
                    String[] partes = linha.split(";");
                    if (partes.length != 2) continue;

                    String dataStr = partes[0];
                    String rentabilidadeStr = partes[1];

                    try {
                        Date data = DATE_FORMAT.parse(dataStr);
                        double rentabilidade = Double.parseDouble(rentabilidadeStr);

                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(data);

                        int ano = calendar.get(Calendar.YEAR);
                        int mes = calendar.get(Calendar.MONTH) + 1; // Meses começam do zero

                        if (ano == TARGET_YEAR) {
                            String chave = ano + "-" + String.format("%02d", mes);
                            rentabilidadesMensais.put(chave, rentabilidadesMensais.getOrDefault(chave, 0.0) + rentabilidade);
                        }
                    } catch (ParseException | NumberFormatException e) {
                        System.err.println("Erro ao processar linha: " + linha + " | " + e.getMessage());
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo: " + e.getMessage());
        }

        List<Map.Entry<String, Double>> lista = new ArrayList<>(rentabilidadesMensais.entrySet());
        lista.sort(Map.Entry.<String, Double>comparingByValue().reversed());

        System.out.println("Rentabilidades Mensais de " + TARGET_YEAR + " (ordem decrescente):");
        for (Map.Entry<String, Double> entrada : lista) {
            System.out.println(entrada.getKey() + ": " + entrada.getValue());
        }
    }
}



