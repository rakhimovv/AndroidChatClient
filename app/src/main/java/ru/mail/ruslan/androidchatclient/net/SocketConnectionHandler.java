package ru.mail.ruslan.androidchatclient.net;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class SocketConnectionHandler implements ConnectionHandler {

    private static final String TAG = "SocketConnectionHandler";

    private List<SocketListener> mListeners = new ArrayList<>();
    private BlockingQueue<String> outboundDataQueue;
    private boolean isShutDown;
    private String mHost;
    private int mPort;
    private Socket socket;
    private InputStream mInputStream;
    private OutputStream mOutputStream;
    private Thread outThread;
    private Thread inThread;

    SocketConnectionHandler(String host, int port) throws IOException {
        Log.d(TAG, "constructor started");
        mHost = host;
        mPort = port;
        isShutDown = false;
        outboundDataQueue = new LinkedBlockingQueue<>();
        Log.d(TAG, "constructor ended");
    }

    public boolean isShutDown() {
        return isShutDown;
    }

    @Override
    public void sendData(String data) {
        Log.e(TAG, "SendData: " + data);
        outboundDataQueue.add(data);
    }

    @Override
    public void addListener(SocketListener listener) {
        mListeners.add(listener);
    }

    @Override
    public void run() {
        Log.d(TAG, "run() started");

        try {
            socket = new Socket(mHost, mPort);
            mInputStream = socket.getInputStream();
            mOutputStream = socket.getOutputStream();

            for (SocketListener listener : mListeners) {
                listener.onConnected();
            }

            inThread = new Thread(new inboundConnection());
            inThread.setName("SocketConnectionHandler inbound connection thread");
            inThread.setDaemon(true);

            outThread = new Thread(new outboundConnection());
            outThread.setName("SocketConnectionHandler outbound connection thread");
            outThread.setDaemon(true);

            inThread.start();
            outThread.start();

            Log.d(TAG, "inThread, outThread started");
        } catch (Exception e) {
            for (SocketListener listener : mListeners) {
                listener.onConnectionFailed();
            }
            e.printStackTrace();
        }
    }

    @Override
    public void stop() {
        Log.d(TAG, "stop() started");
        synchronized (this) {
            isShutDown = true;
        }
        if (outThread != null && !outThread.isInterrupted()) {
            outThread.interrupt();
        }
        if (inThread != null && !inThread.isInterrupted()) {
            inThread.interrupt();
        }
        if (socket != null && !socket.isClosed()) {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (!Thread.currentThread().isInterrupted()) {
            Thread.currentThread().interrupt();
        }
        Log.d(TAG, "stop() ended");
    }

    private class inboundConnection implements Runnable {
        public void run() {

            final byte[] buf = new byte[1024 * 64];
            while (!inThread.isInterrupted()) {
                try {
                    int read = mInputStream.read(buf);
                    if (read > 0) {
                        String data = new String(Arrays.copyOf(buf, read), "UTF-8");
                        for (SocketListener listener : mListeners) {
                            listener.onDataReceived(data);
                        }
                    }


                } catch (Exception e) {
                    for (SocketListener listener : mListeners) {
                        listener.onConnectionFailed();
                    }
                    Log.d(TAG, "Failed to handle inboundConnection");
                    e.printStackTrace();
                    stop();
                }
            }
        }
    }

    private class outboundConnection implements Runnable {
        public void run() {
            while (!outThread.isInterrupted()) {
                try {
                    String data = outboundDataQueue.take();
                    if (data != null) {
                        Log.e(TAG, "NEW SEND DATA: " + data);
                        mOutputStream.write(data.getBytes("UTF-8"));
                        mOutputStream.flush();
                    }
                } catch (InterruptedException i) {
                    outThread.interrupt();
                } catch (Exception e) {
                    for (SocketListener listener : mListeners) {
                        listener.onConnectionFailed();
                    }
                    Log.d(TAG, "Failed to handle inboundConnection");
                    e.printStackTrace();
                    stop();
                }
            }
        }
    }
}