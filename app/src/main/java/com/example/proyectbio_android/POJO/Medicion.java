package com.example.proyectbio_android.POJO;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/*
 * Nombre del fichero: Medicion.java
 * Descripción: Clase que representa una medición de gas.
 * Autores: Carla Rumeu Montesinos y Elena Ruiz de la Blanca
 * Fecha: 2 de octubre de 2024
 *
 * Este archivo ha sido realizado por Carla Rumeu Montesinos y Elena Ruiz de la Blanca el 2 de octubre de 2024.
 * Contiene la definición de la clase Medicion, que incluye información sobre una medición de gas,
 * como la medida, el lugar, el tipo de gas y la hora de la medición.
 *
 * Todos los derechos reservados.
 */

/**
 * @brief Clase que representa una medición de gas.
 *
 * Esta clase contiene información sobre una medición de gas, incluyendo la medida, el lugar, el tipo de gas y la hora de la medición.
 */
public class Medicion {
    private double medida; ///< La medida del gas.
    private String lugar; ///< El lugar de la medición.
    private String tipo_gas; ///< El tipo de gas medido.
    private final String hora; ///< La hora de la medición.

    /**
     * @brief Constructor para la clase Medicion.
     *
     * @param medida La medida del gas.
     * @param lugar El lugar de la medición.
     * @param tipo_gas El tipo de gas medido.
     */
    // medida: R, lugar: Texto, tipo_gas: Texto -> Medicion()
    public Medicion(double medida, String lugar, String tipo_gas) {
        this.medida = medida;
        this.lugar = lugar;
        this.tipo_gas = tipo_gas;

        // Formateando la hora a String con el formato deseado
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        this.hora = sdf.format(new Date()); // Asignar la hora actual en el formato correcto
    }

    /**
     * @brief Getter para la medida.
     *
     * @return La medida del gas.
     */
    // getMedida() -> R
    public double getMedida() {
        return medida;
    }

    /**
     * @brief Getter para el lugar.
     *
     * @return El lugar de la medición.
     */

    // getLugar() -> Texto
    public String getLugar() {
        return lugar;
    }

    /**
     * @brief Getter para el tipo de gas.
     *
     * @return El tipo de gas medido.
     */
    // getTipo_gas() -> Texto
    public String getTipo_gas() {
        return tipo_gas;
    }

    /**
     * @brief Getter para la hora.
     *
     * @return La hora de la medición.
     */

    // getHora() -> Texto
    public String getHora() {
        return hora;
    }

    /**
     * @brief Setter para la medida.
     *
     * @param medida La nueva medida del gas.
     */

    // medida: R -> setMedida()
    public void setMedida(double medida) {
        this.medida = medida;
    }

    /**
     * @brief Setter para el lugar.
     *
     * @param lugar El nuevo lugar de la medición.
     */
    // lugar: Texto -> setLugar()
    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    /**
     * @brief Setter para el tipo de gas.
     *
     * @param tipo_gas El nuevo tipo de gas medido.
     */
    // tipo_gas: Texto -> setTipo_gas()
    public void setTipo_gas(String tipo_gas) {
        this.tipo_gas = tipo_gas;
    }

    /**
     * @brief Método para convertir el objeto a JSON con el orden deseado.
     *
     * @return El objeto en formato JSON como cadena.
     */

    // toJson() -> Texto
    public String toJson() {
        JsonObject jsonObject = new JsonObject();

        // Añadiendo los campos en el orden deseado
        jsonObject.addProperty("medida", medida);
        jsonObject.addProperty("lugar", lugar);
        jsonObject.addProperty("tipo_gas", tipo_gas);
        jsonObject.addProperty("hora", hora);

        return jsonObject.toString(); // Retorna el JSON como cadena
    }

    /**
     * @brief Método toString para mostrar el objeto en formato cadena.
     *
     * @return El objeto en formato cadena.
     */

    // toString() -> Texto
    @NonNull
    @Override
    public String toString() {
        return "Medicion{" +
                "medida=" + medida +
                ", lugar='" + lugar + '\'' +
                ", tipoGas='" + tipo_gas + '\'' +
                ", hora='" + hora + '\'' +
                '}';
    }
}