package com.example.proyectbio_android.POJO;

// -----------------------------------------------------------------------------------
import java.util.Arrays;

/*
 * Nombre del fichero: TramaIBeacon.java
 * Descripción: Clase que representa la trama de un iBeacon.
 * Autores: Carla Rumeu Montesinos y Elena Ruiz de la Blanca
 * Fecha: 1 de octubre de 2024
 *
 * Este archivo ha sido realizado por Carla Rumeu Montesinos y Elena Ruiz de la Blanca el 1 de octubre de 2024.
 * Contiene la definición de la clase TramaIBeacon, que incluye información sobre la trama de un iBeacon,
 * como el prefijo, UUID, major, minor, txPower, y otros campos relacionados.
 *
 * Todos los derechos reservados.
 */

/**
 * Clase TramaIBeacon que representa la trama de un iBeacon.
 */
public class TramaIBeacon {
    private byte[] prefijo = null; // 9 bytes
    private byte[] uuid = null; // 16 bytes
    private byte[] major = null; // 2 bytes
    private byte[] minor = null; // 2 bytes
    private byte txPower = 0; // 1 byte

    private byte[] losBytes;

    private byte[] advFlags = null; // 3 bytes
    private byte[] advHeader = null; // 2 bytes
    private byte[] companyID = new byte[2]; // 2 bytes
    private byte iBeaconType = 0 ; // 1 byte
    private byte iBeaconLength = 0 ; // 1 byte

    /**
     * Obtiene el prefijo de la trama.
     * @return El prefijo de la trama.
     */
    // getPrefijo() -> byte[]
    public byte[] getPrefijo() {
        return prefijo;
    }

    /**
     * Obtiene el UUID de la trama.
     * @return El UUID de la trama.
     */

    // getUUID() -> byte[]
    public byte[] getUUID() {
        return uuid;
    }

    /**
     * Obtiene el major de la trama.
     * @return El major de la trama.
     */

    // getMajor() -> byte[]
    public byte[] getMajor() {
        return major;
    }

    /**
     * Obtiene el minor de la trama.
     * @return El minor de la trama.
     */

    // getMinor() -> byte[]
    public byte[] getMinor() {
        return minor;
    }

    /**
     * Obtiene el txPower de la trama.
     * @return El txPower de la trama.
     */

    // getTxPower() -> byte
    public byte getTxPower() {
        return txPower;
    }

    /**
     * Obtiene los bytes de la trama.
     * @return Los bytes de la trama.
     */

    // getLosBytes() -> byte[]
    public byte[] getLosBytes() {
        return losBytes;
    }

    /**
     * Obtiene los advFlags de la trama.
     * @return Los advFlags de la trama.
     */
    // getAdvFlags() -> byte[]
    public byte[] getAdvFlags() {
        return advFlags;
    }

    /**
     * Obtiene el advHeader de la trama.
     * @return El advHeader de la trama.
     */
    // getAdvHeader() -> byte[]
    public byte[] getAdvHeader() {
        return advHeader;
    }

    /**
     * Obtiene el companyID de la trama.
     * @return El companyID de la trama.
     */

    // getCompanyID() -> byte[]
    public byte[] getCompanyID() {
        return companyID;
    }

    /**
     * Obtiene el iBeaconType de la trama.
     * @return El iBeaconType de la trama.
     */
    // getiBeaconType() -> byte
    public byte getiBeaconType() {
        return iBeaconType;
    }

    /**
     * Obtiene el iBeaconLength de la trama.
     * @return El iBeaconLength de la trama.
     */
    // getiBeaconLength() -> byte
    public byte getiBeaconLength() {
        return iBeaconLength;
    }

    /**
     * Constructor de la clase TramaIBeacon.
     * @param bytes Los bytes que representan la trama de un iBeacon.
     */
    // bytes: byte[] ->TramaIBeacon()
    public TramaIBeacon(byte[] bytes ) {
        this.losBytes = bytes;

        prefijo = Arrays.copyOfRange(losBytes, 0, 8+1 ); // 9 bytes
        uuid = Arrays.copyOfRange(losBytes, 9, 24+1 ); // 16 bytes
        major = Arrays.copyOfRange(losBytes, 25, 26+1 ); // 2 bytes
        minor = Arrays.copyOfRange(losBytes, 27, 28+1 ); // 2 bytes
        txPower = losBytes[ 29 ]; // 1 byte

        advFlags = Arrays.copyOfRange( prefijo, 0, 2+1 ); // 3 bytes
        advHeader = Arrays.copyOfRange( prefijo, 3, 4+1 ); // 2 bytes
        companyID = Arrays.copyOfRange( prefijo, 5, 6+1 ); // 2 bytes
        iBeaconType = prefijo[ 7 ]; // 1 byte
        iBeaconLength = prefijo[ 8 ]; // 1 byte

    } // ()
} // class
// -----------------------------------------------------------------------------------
// -----------------------------------------------------------------------------------
// -----------------------------------------------------------------------------------
// -----------------------------------------------------------------------------------