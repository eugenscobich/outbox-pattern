package com.eugenescobich.iot.utils;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;

public class MQQTPacketGenerator {

    private static String MQQT_USERNAME = "iot-service-pub";
    private static String MQQT_PASSWORD = "5{LJpbX9R*K]Ji7y2o";

    private static String MQQT_TOPIC = "valetron";
    private static String MQQT_VALUE = "helloravi";
    private static String MQQT_PROTOCOL = "MQTT";
    private static String MQQT_CLIENT_ID = "ABCDEF";

    public static void main(String[] arg) {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        connect(byteArrayOutputStream, MQQT_PROTOCOL, MQQT_CLIENT_ID, MQQT_USERNAME, MQQT_PASSWORD);

        pub(byteArrayOutputStream, MQQT_TOPIC, MQQT_VALUE);

        byteArrayOutputStream.write(26);

        byte[] byteArray = byteArrayOutputStream.toByteArray();

        for (int i = 0; i < byteArray.length; i++) {
            String st = String.format("%02X", byteArray[i]);
            System.out.print(st);
            if (i < byteArray.length - 1) {
                System.out.print("\t");
            }
        }
    }

    private static void connect(ByteArrayOutputStream byteArrayOutputStream, String protocol, String clientId, String username, String password) {
        // MQTT Control Packet type + Flags
        byte controlPacketType = 1 << 4; // 00010000;

        // Remaining Length
        byte remainingLength = 0;

        // Variable Header
        // Protocol Name
        byte lengthMSB = 0;
        remainingLength++;
        byte[] protocolName = toBytes(protocol.toCharArray());
        remainingLength = (byte)(remainingLength + protocolName.length);
        byte lengthLSB = (byte)protocolName.length;
        remainingLength++;

        // Protocol version
        byte protocolVersion = 5;
        remainingLength++;

        // Connect Flags
        byte connectFlag = 1 << 1;
        if (username != null) {
            connectFlag = (byte) (connectFlag | 1 << 7);
        }
        if (username != null && password != null) {
            connectFlag = (byte)(connectFlag | 1 << 6);
        }
        remainingLength++;

        // Keep Alive
        byte keepAliveMSB = 0;
        remainingLength++;
        byte keepAliveLSB = 60; // 60sec
        remainingLength++;


        // Property
        byte propertiesLength = 5;
        remainingLength++;
        byte sessionExpiryIntervalIdentifier = 17;
        remainingLength++;
        byte[] sessionExpiryInterval = new byte[] {0, 0, 0, 10};
        remainingLength = (byte)(remainingLength + sessionExpiryInterval.length);


        // Client Identifier (ClientID)
        // Client Name
        byte clientLengthMSB = 0;
        remainingLength++;
        byte[] clientName = toBytes(clientId.toCharArray());
        remainingLength = (byte)(remainingLength + clientName.length);
        byte clientLengthLSB = (byte)clientName.length;
        remainingLength++;

        // Username
        byte userLengthMSB = 0;
        byte[] userName = null;
        byte userLengthLSB = 0;
        if (username != null) {
            remainingLength++;
            userName = toBytes(username.toCharArray());
            remainingLength = (byte)(remainingLength + userName.length);
            userLengthLSB = (byte)userName.length;
            remainingLength++;
        }

        // Password
        byte passwordLengthMSB = 0;
        byte[] passwordName = null;
        byte passwordLengthLSB = 0;
        if (username != null && password != null) {
            remainingLength++;
            passwordName = toBytes(password.toCharArray());
            remainingLength = (byte) (remainingLength + passwordName.length);
            passwordLengthLSB = (byte) passwordName.length;
            remainingLength++;
        }






        byteArrayOutputStream.write(controlPacketType);
        byteArrayOutputStream.write(remainingLength);

        byteArrayOutputStream.write(lengthMSB);
        byteArrayOutputStream.write(lengthLSB);
        byteArrayOutputStream.write(protocolName, 0, protocolName.length);

        byteArrayOutputStream.write(protocolVersion);
        byteArrayOutputStream.write(connectFlag);

        byteArrayOutputStream.write(keepAliveMSB);
        byteArrayOutputStream.write(keepAliveLSB);

        byteArrayOutputStream.write(propertiesLength);
        byteArrayOutputStream.write(sessionExpiryIntervalIdentifier);
        byteArrayOutputStream.write(sessionExpiryInterval, 0, sessionExpiryInterval.length);

        byteArrayOutputStream.write(clientLengthMSB);
        byteArrayOutputStream.write(clientLengthLSB);
        byteArrayOutputStream.write(clientName, 0, clientName.length);

        if (username != null) {
            byteArrayOutputStream.write(userLengthMSB);
            byteArrayOutputStream.write(userLengthLSB);
            byteArrayOutputStream.write(userName, 0, userName.length);
        }
        if (username != null && password != null) {
            byteArrayOutputStream.write(passwordLengthMSB);
            byteArrayOutputStream.write(passwordLengthLSB);
            byteArrayOutputStream.write(passwordName, 0, passwordName.length);
        }
    }


    private static void pub(ByteArrayOutputStream byteArrayOutputStream, String topic, String message) {
        // MQTT Control Packet type + Flags
        byte controlPacketType = 3 << 4; // 00110000;

        // Remaining Length
        byte remainingLength = 0;

        // Topic Name
        byte topicLengthMSB = 0;
        remainingLength++;
        byte[] topicName = toBytes(topic.toCharArray());
        remainingLength = (byte)(remainingLength + topicName.length);
        byte topicLengthLSB = (byte)topicName.length;
        remainingLength++;

        // Property
        byte packetIdentifierMSB = 0;
        remainingLength++;
        byte packetIdentifierLSB = 0;
        remainingLength++;
        byte propertyLength = 0;
        remainingLength++;
        //byte payloadFormatIndicator = 0;
        //remainingLength++;

        // Message
        byte[] messageName = toBytes(message.toCharArray());
        remainingLength = (byte)(remainingLength + messageName.length);

        byteArrayOutputStream.write(controlPacketType);
        byteArrayOutputStream.write(remainingLength);

        byteArrayOutputStream.write(topicLengthMSB);
        byteArrayOutputStream.write(topicLengthLSB);
        byteArrayOutputStream.write(topicName, 0, topicName.length);

        byteArrayOutputStream.write(packetIdentifierMSB);
        byteArrayOutputStream.write(packetIdentifierLSB);
        byteArrayOutputStream.write(propertyLength);

        byteArrayOutputStream.write(messageName, 0, messageName.length);
    }

    static byte[] toBytes(char[] chars) {
        CharBuffer charBuffer = CharBuffer.wrap(chars);
        ByteBuffer byteBuffer = Charset.forName("UTF-8").encode(charBuffer);
        byte[] bytes = Arrays.copyOfRange(byteBuffer.array(),
                byteBuffer.position(), byteBuffer.limit());
        Arrays.fill(byteBuffer.array(), (byte) 0); // clear sensitive data
        return bytes;
    }

}
