package com.example.proyectbio_android.API;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.transform.Result;
/*
 * Nombre del fichero: PeticionarioRESTWorker.java
 * Descripción: Clase que extiende Worker para realizar peticiones REST a la API de mediciones.
 * Maneja distintos tipos de solicitudes HTTP (GET, POST, etc.) y procesa las respuestas de la API,
 * mostrando mensajes al usuario a través de Toasts.
 * Autores: Carla Rumeu Montesinos y Elena Ruiz de la Blanca
 * Fecha: 7 de octubre de 2024
 *
 * Este archivo ha sido realizado por Carla Rumeu Montesinos y Elena Ruiz de la Blanca el 7 de octubre de 2024.
 * Contiene la implementación de un Worker de Android para realizar solicitudes REST y manejar las respuestas de la API.
 * Incluye manejo de errores y muestra notificaciones al usuario según el código de respuesta.
 *
 * Todos los derechos reservados.
 */

/**
 * Worker para hacer solicitudes REST a la API de mediciones.
 */
public class PeticionarioRESTWorker extends Worker {

    // Clave para el método HTTP (GET, POST, etc.)
    public static final String KEY_METHOD = "KEY_METHOD";

    // Clave para la URL de destino
    public static final String KEY_URL = "KEY_URL";

    // Clave para el cuerpo de la solicitud (por ejemplo, la carga útil JSON)
    public static final String KEY_BODY = "KEY_BODY";

    // Clave para el código de respuesta de la solicitud HTTP
    public static final String KEY_RESPONSE_CODE = "KEY_RESPONSE_CODE";

    // Clave para el cuerpo de la respuesta (por ejemplo, la respuesta JSON)
    public static final String KEY_RESPONSE_BODY = "KEY_RESPONSE_BODY";

    private final Context context;

    /**
     * Constructor para el Worker.
     *
     * @param context El contexto de la aplicación
     * @param params Parámetros para el Worker
     */

    // Context, WorkerParameters -> PeticionarioRESTWorker()
    public PeticionarioRESTWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
        this.context = context;
    }

    /**
     * Ejecuta la tarea del Worker, que es hacer una solicitud REST.
     *
     * @return Resultado de la ejecución (éxito o fracaso)
     */

    //KEY_METHOD: Texto, KEY_URL: Texto, KEY_BODY: Texto -> doWork() -> KEY_RESPONSE_CODE: Z, KEY_RESPONSE_BODY: Texto, Result: Result
    @NonNull
    @Override
    public Result doWork() {
        // Recupera los valores dinámicos pasados en tiempo de ejecución a través de Data
        String method = getInputData().getString(KEY_METHOD); // Método HTTP
        String urlDestination = getInputData().getString(KEY_URL); // URL de la API
        String requestBody = getInputData().getString(KEY_BODY); // Cuerpo de la solicitud (carga útil)

        int responseCode;
        String responseBody = "";

        try {
            // Crea la conexión HTTP y envía la solicitud
            HttpURLConnection connection = getHttpURLConnection(urlDestination, method, requestBody);

            // Obtiene el código de respuesta y el cuerpo
            responseCode = connection.getResponseCode();
            StringBuilder responseAccumulator = new StringBuilder();
            try {
                InputStream is = connection.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                String line;
                while ((line = br.readLine()) != null) {
                    responseAccumulator.append(line);
                }
                responseBody = responseAccumulator.toString();
                connection.disconnect();
            } catch (IOException ex) {
                Log.d("PeticionarioRESTWorker", "No hay cuerpo de respuesta.");
            }

            // Muestra un Toast dependiendo del código de respuesta
            if (responseCode == 201) { // Creación exitosa
                showToast("¡Medición enviada con éxito!");
            } else if (responseCode == 400) { // Solicitud incorrecta
                showToast("Error: Datos de medición inválidos.");
            } else {
                showToast("Respuesta inesperada: " + responseCode);
            }

        } catch (Exception e) {
            Log.d("PeticionarioRESTWorker", "Ocurrió una excepción: " + e.getMessage());
            showToast("Error al enviar la medición: " + e.getMessage());
            return Result.failure();
        }

        // Devuelve el código de respuesta y el cuerpo como datos de salida
        Data outputData = new Data.Builder()
                .putInt(KEY_RESPONSE_CODE, responseCode)
                .putString(KEY_RESPONSE_BODY, responseBody)
                .build();

        return Result.success(outputData);
    }

    /**
     * Configura la conexión HTTP para la solicitud REST.
     *
     * @param urlDestination URL de destino
     * @param method Método HTTP (GET, POST, etc.)
     * @param requestBody Cuerpo de la solicitud (para métodos POST o PUT)
     * @return HttpURLConnection configurada
     * @throws IOException Si hay un problema de conexión
     */

    //urlDestination: Texto, method: Texto, requestBody: Texto -> getHttpURLConnection() -> HttpURLConnection
    private static @NonNull HttpURLConnection getHttpURLConnection(String urlDestination, String method, String requestBody) throws IOException {
        URL url = new URL(urlDestination);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
        connection.setRequestMethod(method);
        connection.setDoInput(true);

        // Si el método no es GET, añade el cuerpo de la solicitud
        if (!"GET".equals(method) && requestBody != null) {
            connection.setDoOutput(true);
            DataOutputStream dos = new DataOutputStream(connection.getOutputStream());
            dos.writeBytes(requestBody);
            dos.flush();
            dos.close();
        }
        return connection;
    }

    /**
     * Muestra un mensaje Toast en el hilo de la interfaz de usuario.
     *
     * @param message El mensaje a mostrar en el Toast
     */

    //message: Texto -> showToast()
    private void showToast(final String message) {
        // Asegurándose de que el Toast se ejecute en el hilo de la interfaz de usuario
        new android.os.Handler(android.os.Looper.getMainLooper()).post(() -> Toast.makeText(context, message, Toast.LENGTH_SHORT).show());
    }
}