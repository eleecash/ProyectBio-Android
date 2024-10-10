package com.example.proyectbio_android;

import static android.Manifest.permission.BLUETOOTH_CONNECT;

import static androidx.activity.result.contract.ActivityResultContracts.*;

import android.Manifest.permission;
import android.os.Build;
import android.os.Bundle;

// ------------------------------------------------------------------
// ------------------------------------------------------------------

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

;

import com.example.proyectbio_android.API.PeticionarioRESTWorker;
import com.example.proyectbio_android.LOGIC.Utilidades;
import com.example.proyectbio_android.POJO.Medicion;
import com.example.proyectbio_android.POJO.TramaIBeacon;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/*
 * Nombre del fichero: MainActivity.java
 * Descripción: Clase principal que maneja la actividad principal de la aplicación de escaneo Bluetooth Low Energy (BTLE).
 *              Esta clase se encarga de la gestión de la conexión Bluetooth, el escaneo de dispositivos BTLE,
 *              la visualización de la información del dispositivo y el envío de datos a través de solicitudes POST.
 * Autores: Carla Rumeu Montesinos y Elena Ruiz De La Blanca
 * Fecha: 30 de Septiembre
 *
 * Este archivo ha sido realizado por Carla Rumeu Montesinos y Elena Ruiz De La Blanca el 30 de Septiembre.
 * Contiene la implementación de las funcionalidades principales para buscar y gestionar dispositivos Bluetooth Low Energy,
 * así como la capacidad de enviar datos a un servidor a través de solicitudes POST.
 *
 * Todos los derechos reservados.
 */

/**
 * @file MainActivity.java
 * @brief Esta es la clase principal que maneja la actividad principal de la aplicación.
 *
 * Esta clase se encarga de la gestión de la conexión Bluetooth, el escaneo de dispositivos Bluetooth,
 * la visualización de la información del dispositivo y el envío de datos a través de POST.
 */
public class MainActivity extends AppCompatActivity {

    // --------------------------------------------------------------
    // --------------------------------------------------------------
    private static final String ETIQUETA_LOG = ">>>>";

    // --------------------------------------------------------------
    // --------------------------------------------------------------
    private BluetoothLeScanner elEscanner;

    private ScanCallback callbackDelEscaneo = null;

    private final String uuidString = "quierocafecafeee";
    private TramaIBeacon tib;

    // Variable para seguir el estado del escaneo
    private boolean isScanning = false;

    public TextView showMajor;
    public Button enviarPostPrueba;
    public Button EncenderEnvioPost;



    /**
     * @brief Método para buscar todos los dispositivos Bluetooth Low Energy (BTLE) disponibles.
     *
     * Este método inicia un escaneo de dispositivos BTLE y registra un callback para manejar los resultados del escaneo.
     */
    // buscarTodosLosDispositivosBTLE()
    private void buscarTodosLosDispositivosBTLE() {
        Log.d(ETIQUETA_LOG, " buscarTodosLosDispositivosBTL(): empieza ");

        Log.d(ETIQUETA_LOG, " buscarTodosLosDispositivosBTL(): instalamos scan callback ");

        this.callbackDelEscaneo = new ScanCallback() {
            @Override
            public void onScanResult(int callbackType, ScanResult resultado) {
                super.onScanResult(callbackType, resultado);
                Log.d(ETIQUETA_LOG, " buscarTodosLosDispositivosBTL(): onScanResult() ");

                mostrarInformacionDispositivoBTLE(resultado);
            }

            @Override
            public void onBatchScanResults(List<ScanResult> results) {
                super.onBatchScanResults(results);
                Log.d(ETIQUETA_LOG, " buscarTodosLosDispositivosBTL(): onBatchScanResults() ");

            }

            @Override
            public void onScanFailed(int errorCode) {
                super.onScanFailed(errorCode);
                Log.d(ETIQUETA_LOG, " buscarTodosLosDispositivosBTL(): onScanFailed() ");

            }
        };

        Log.d(ETIQUETA_LOG, " buscarTodosLosDispositivosBTL(): empezamos a escanear ");

        if (ActivityCompat.checkSelfPermission(this, permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                requestPermissionLuancher.launch(permission.BLUETOOTH_SCAN);
            }
        }
        this.elEscanner.startScan(this.callbackDelEscaneo);

    } // ()

    /**
     * @brief Método para mostrar la información de un dispositivo BTLE escaneado.
     *
     * Este método recibe un resultado de escaneo y muestra la información relevante del dispositivo BTLE.
     *
     * @param resultado El resultado del escaneo de un dispositivo BTLE.
     */
    //resultado: ScanResult -> mostrarInformacionDispositivoBTLE()
    private void mostrarInformacionDispositivoBTLE(ScanResult resultado) {


        BluetoothDevice bluetoothDevice = resultado.getDevice();
        byte[] bytes = Objects.requireNonNull(resultado.getScanRecord()).getBytes();
        int rssi = resultado.getRssi();

        Log.d(ETIQUETA_LOG, " ******");
        Log.d(ETIQUETA_LOG, " * DISPOSITIVO DETECTADO BTLE *** ");
        Log.d(ETIQUETA_LOG, " ******");
        if (ActivityCompat.checkSelfPermission(this, BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                requestPermissionLuancher.launch(BLUETOOTH_CONNECT);
            }
        }
        Log.d(ETIQUETA_LOG, " nombre = " + bluetoothDevice.getName());
        Log.d(ETIQUETA_LOG, " toString = " + bluetoothDevice);

        Log.d(ETIQUETA_LOG, " dirección = " + bluetoothDevice.getAddress());
        Log.d(ETIQUETA_LOG, " rssi = " + rssi);

        Log.d(ETIQUETA_LOG, " bytes = " + new String(bytes));
        Log.d(ETIQUETA_LOG, " bytes (" + bytes.length + ") = " + Utilidades.bytesToHexString(bytes));

        TramaIBeacon tib = new TramaIBeacon(bytes);

        Log.d(ETIQUETA_LOG, " ----------------------------------------------------");
        Log.d(ETIQUETA_LOG, " prefijo  = " + Utilidades.bytesToHexString(tib.getPrefijo()));
        Log.d(ETIQUETA_LOG, "          advFlags = " + Utilidades.bytesToHexString(tib.getAdvFlags()));
        Log.d(ETIQUETA_LOG, "          advHeader = " + Utilidades.bytesToHexString(tib.getAdvHeader()));
        Log.d(ETIQUETA_LOG, "          companyID = " + Utilidades.bytesToHexString(tib.getCompanyID()));
        Log.d(ETIQUETA_LOG, "          iBeacon type = " + Integer.toHexString(tib.getiBeaconType()));
        Log.d(ETIQUETA_LOG, "          iBeacon length 0x = " + Integer.toHexString(tib.getiBeaconLength()) + " ( "
                + tib.getiBeaconLength() + " ) ");
        Log.d(ETIQUETA_LOG, " uuid  = " + Utilidades.bytesToHexString(tib.getUUID()));
        Log.d(ETIQUETA_LOG, " uuid  = " + Utilidades.bytesToString(tib.getUUID()));
        Log.d(ETIQUETA_LOG, " major  = " + Utilidades.bytesToHexString(tib.getMajor()) + "( "
                + Utilidades.bytesToInt(tib.getMajor()) + " ) ");
        Log.d(ETIQUETA_LOG, " minor  = " + Utilidades.bytesToHexString(tib.getMinor()) + "( "
                + Utilidades.bytesToInt(tib.getMinor()) + " ) ");
        Log.d(ETIQUETA_LOG, " txPower  = " + Integer.toHexString(tib.getTxPower()) + " ( " + tib.getTxPower() + " )");
        Log.d(ETIQUETA_LOG, " ******");

    } // ()
    /**
     * @brief Método para buscar un dispositivo BTLE específico.
     *
     * Este método inicia un escaneo de dispositivos BTLE y registra un callback para manejar los resultados del escaneo.
     * Solo se procesan los resultados que corresponden a un dispositivo BTLE específico.
     */
    // buscarEsteDispositivoBTLE()
    private void buscarEsteDispositivoBTLE() {
        Log.d(ETIQUETA_LOG, " buscarEsteDispositivoBTLE(): empieza ");

        Log.d(ETIQUETA_LOG, "  buscarEsteDispositivoBTLE(): instalamos scan callback ");

        this.callbackDelEscaneo = new ScanCallback() {
            @Override
            public void onScanResult(int callbackType, ScanResult resultado) {
                super.onScanResult(callbackType, resultado);
                byte[] bytes = Objects.requireNonNull(resultado.getScanRecord()).getBytes();
                TramaIBeacon scan = new TramaIBeacon(bytes);
                if ( uuidString.equals(Utilidades.bytesToString(scan.getUUID())) ) {
                    mostrarInformacionDispositivoBTLE(resultado);
                    tib = scan;
                    showMajor();
                }
            }

            @Override
            public void onBatchScanResults(List<ScanResult> results) {
                super.onBatchScanResults(results);
                Log.d(ETIQUETA_LOG, "  buscarEsteDispositivoBTLE(): onBatchScanResults() ");
            }

            @Override
            public void onScanFailed(int errorCode) {
                super.onScanFailed(errorCode);
                Log.d(ETIQUETA_LOG, "  buscarEsteDispositivoBTLE(): onScanFailed() ");
            }
        };

        Log.d(ETIQUETA_LOG, "  buscarEsteDispositivoBTLE(): empezamos");

        if (ActivityCompat.checkSelfPermission(this, permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                requestPermissionLuancher.launch(permission.BLUETOOTH_SCAN);
            }
        }

        // Iniciar escaneo con filtro
        this.elEscanner.startScan(this.callbackDelEscaneo);

        // saber si el sensor está escaneando
        isScanning = true;
    } // ()

    /**
     * @brief Método para detener la búsqueda de dispositivos BTLE.
     *
     * Este método detiene el escaneo de dispositivos BTLE que se inició previamente.
     */

    // detenerBusquedaDispositivosBTLE()
    private void detenerBusquedaDispositivosBTLE() {

        if (this.callbackDelEscaneo == null) {
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            ActivityCompat.checkSelfPermission(this, permission.BLUETOOTH_SCAN);
        }
        this.elEscanner.stopScan(this.callbackDelEscaneo);
        this.callbackDelEscaneo = null;

    } // ()

    /**
     * @brief Método para mostrar el valor Major de un dispositivo iBeacon.
     *
     * Este método muestra el valor Major de un dispositivo iBeacon si el escaneo está en curso.
     */
    // showMajor()
    private void showMajor() {
        Log.d(ETIQUETA_LOG, " showMajor()");

        // Comprovar si s'està escanejant
        if (isScanning) {

            String display = getString(R.string.ppm) + (Arrays.toString(tib.getMajor()));
            Log.d(ETIQUETA_LOG, " Major: " + display);
            showMajor.setText(display);

        } else {
            Log.d(ETIQUETA_LOG, " No s'està escanejant.");
        }
    } // ()

    /**
     * @brief Método para manejar el evento de pulsar el botón de búsqueda de dispositivos BTLE.
     *
     * Este método se invoca cuando se pulsa el botón de búsqueda de dispositivos BTLE.
     *
     * @param v La vista del botón pulsado.
     */
    // v: View -> botonBuscarDispositivosBTLEPulsado()
    public void botonBuscarDispositivosBTLEPulsado(View v) {
        Log.d(ETIQUETA_LOG, " boton buscar dispositivos BTLE Pulsado");
        this.buscarTodosLosDispositivosBTLE();
    } // ()

    /**
     * @brief Método para manejar el evento de pulsar el botón de búsqueda de nuestro dispositivo BTLE.
     *
     * Este método se invoca cuando se pulsa el botón de búsqueda de nuestro dispositivo BTLE.
     *
     * @param v La vista del botón pulsado.
     */
    // v: View -> botonBuscarNuestroDispositivoBTLEPulsado()
    public void botonBuscarNuestroDispositivoBTLEPulsado(View v) {
        Log.d(ETIQUETA_LOG, " boton nuestro dispositivo BTLE Pulsado");
        this.buscarEsteDispositivoBTLE();
    } // ()

    /**
     * @brief Método para manejar el evento de pulsar el botón de detener la búsqueda de dispositivos BTLE.
     *
     * Este método se invoca cuando se pulsa el botón de detener la búsqueda de dispositivos BTLE.
     *
     * @param v La vista del botón pulsado.
     */
    // v: View -> botonDetenerBusquedaDispositivosBTLEPulsado()
    public void botonDetenerBusquedaDispositivosBTLEPulsado(View v) {
        Log.d(ETIQUETA_LOG, " boton detener busqueda dispositivos BTLE Pulsado");
        this.detenerBusquedaDispositivosBTLE();
    } // ()

    /**
     * @brief Método para manejar el evento de pulsar el botón de enviar un POST de prueba.
     *
     * Este método se invoca cuando se pulsa el botón de enviar un POST de prueba.
     *
     * @param v La vista del botón pulsado.
     */
    // v: View -> botonEnviarPostPrueba()
    public void botonEnviarPostPrueba(View v) {
        Log.d(ETIQUETA_LOG, " boton Enviar Post Pulsado");
        this.enviarPostPrueba();
    }

    /**
     * @brief Método para manejar el evento de pulsar el botón de enviar el último valor Major.
     *
     * Este método se invoca cuando se pulsa el botón de enviar el último valor Major.
     *
     * @param v La vista del botón pulsado.
     */
    // v: View -> botonEnviarLastMajor()
    public void botonEnviarLastMajor(View v) {
        Log.d(ETIQUETA_LOG, " boton Enviar Last Major Pulsado");
        this.enviarLastMajor();
    }

    /**
     * @brief Método para enviar un POST de prueba.
     *
     * Este método crea una solicitud de trabajo para enviar un POST de prueba a una URL específica.
     */
    // enviarPostPrueba()
    private void enviarPostPrueba() {
        Data inputData = new Data.Builder()
                .putString(PeticionarioRESTWorker.KEY_METHOD, "POST")
                .putString(PeticionarioRESTWorker.KEY_URL, "http://192.168.18.133:3000/mediciones")
                .putString(PeticionarioRESTWorker.KEY_BODY, "{ \"medida\": 5.25, \"lugar\": \"casitaElenita\", \"tipo_gas\": \"CO\", \"hora\": \"2024-09-30 11:00:00\" }")
                .build();
        // Start the Worker to make the request
        OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(PeticionarioRESTWorker.class)
                .setInputData(inputData)
                .build();

        WorkManager.getInstance(this).enqueue(workRequest);
    }

    /**
     * @brief Método para enviar el último valor Major.
     *
     * Este método crea una solicitud de trabajo para enviar el último valor Major a una URL específica.
     */
    //  enviarLastMajor()
    private void enviarLastMajor() {
        if(tib == null) {
            Toast.makeText(this, "No hay datos disponibles", Toast.LENGTH_SHORT).show();
            return;

        }
        Medicion medicion = new Medicion( Utilidades.bytesToIntOK(tib.getMajor()), "Zona Industrial", "CO2");
        Log.d(ETIQUETA_LOG, " Medicion: " + medicion.toString());
        String json = medicion.toJson();
        Log.d(ETIQUETA_LOG, " JSON: " + json);
        Data inputData = new Data.Builder()
                .putString(PeticionarioRESTWorker.KEY_METHOD, "POST")
                .putString(PeticionarioRESTWorker.KEY_URL, "http://192.168.18.134:3000/mediciones")
                .putString(PeticionarioRESTWorker.KEY_BODY, json)
                .build();
        OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(PeticionarioRESTWorker.class)
                .setInputData(inputData)
                .build();

        WorkManager.getInstance(this).enqueue(workRequest);
    }

    /**
     * @brief Método para manejar la creación de la actividad.
     *
     * Este método se invoca cuando se crea la actividad. Se encarga de la inicialización de la actividad.
     *
     * @param savedInstanceState El estado guardado de la instancia.
     */
    // savedInstanceState: Bundle -> onCreate()
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //SET XML VARIABLES
        showMajor = findViewById(R.id.showMajor);
        enviarPostPrueba = findViewById(R.id.enviarPostPrueba);
        EncenderEnvioPost = findViewById(R.id.ToggleEnviarPost);

        enviarPostPrueba.setOnClickListener(this::botonEnviarPostPrueba);
        EncenderEnvioPost.setOnClickListener(this::botonEnviarLastMajor);

        //SET SCANNER
        BluetoothAdapter elAdaptadorBT = BluetoothAdapter.getDefaultAdapter();
        this.elEscanner = elAdaptadorBT.getBluetoothLeScanner();

    } // ()

    /**
     * @brief Lanzador de resultados de actividad para solicitar permisos.
     *
     * Este lanzador de resultados de actividad se registra para el resultado de una solicitud de permiso.
     * Si el permiso es concedido, se registra un mensaje de éxito en el log.
     * Si el permiso es denegado, se registra un mensaje de error en el log.
     */
    // RequestPermission, isGranted -> onRequestPermissionResult() -> [Texto]
    private final ActivityResultLauncher<String> requestPermissionLuancher =
            registerForActivityResult(new RequestPermission(), isGranted ->
            {
                if (isGranted) {
                    Log.d(ETIQUETA_LOG, " onRequestPermissionResult(): permisos concedidos  !!!!");
                    // Permission is granted. Continue the action or workflow
                    // in your app.
                } else {
                    Log.d(ETIQUETA_LOG, " onRequestPermissionResult(): Socorro: permisos NO concedidos  !!!!");

                }
            });

} // class
// --------------------------------------------------------------
// --------------------------------------------------------------
// --------------------------------------------------------------
// --------------------------------------------------------------