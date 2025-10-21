package com.example.bookreading.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

@Service
public class UdpBroadcastService {

    @Value("${udp.broadcast.port:9876}")
    private int broadcastPort;

    public void broadcastMessage(String message) {
        try (DatagramSocket socket = new DatagramSocket()) {
            byte[] buffer = message.getBytes();
            InetAddress address = InetAddress.getByName("255.255.255.255"); // Broadcast address
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, broadcastPort);
            socket.setBroadcast(true);
            socket.send(packet);
            System.out.println("UDP Broadcast sent: " + message);
        } catch (Exception e) {
            System.err.println("Error sending UDP broadcast: " + e.getMessage());
        }
    }
}
