package com.epump.epumpterminal.tcp

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.OutputStream
import java.net.Socket
import java.nio.charset.Charset
import java.util.*

class TcpClient  {
    private val serverIp  = "192.168.4.1"
    private val serverPort = 5555
    private val connection  = Socket(serverIp,serverPort)
    private var connected = true
    private val scanner = Scanner(connection.getInputStream())
    private val dataOutputStream: OutputStream = connection.getOutputStream()

    init {
        println("Connected to server at $serverIp on port $serverPort")
    }

    fun run(){
        CoroutineScope(Dispatchers.IO).launch {
            read()
        }
        while (connected) {
            val input = readLine() ?: ""
            if ("exit" in input) {
                connected = false
                scanner.close()
                connection.close()
            } else {
                write(input)
            }
        }

    }

    fun write(message: String) {
        dataOutputStream.write((message + '\n').toByteArray(Charset.defaultCharset()))
    }

     fun read() : String {
        while (connected)
            return (scanner.nextLine())

        return  ""
    }






}