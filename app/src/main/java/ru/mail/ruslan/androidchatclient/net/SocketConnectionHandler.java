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
    private static final long RECONNECT_DELAY = 3000L;

    private List<SocketListener> mListeners;
    private BlockingQueue<String> outboundDataQueue;
    private volatile boolean mStopped;
    private String mHost;
    private int mPort;
    private Socket socket;
    private InputStream mInputStream;
    private OutputStream mOutputStream;
    private Thread mOutThread;
    private Thread inThread;

    SocketConnectionHandler(String host, int port) throws IOException {
        mHost = host;
        mPort = port;
        mListeners = new ArrayList<>();
        outboundDataQueue = new LinkedBlockingQueue<>();
    }

    @Override
    public void sendData(String data) {
        outboundDataQueue.add(data);
    }

    @Override
    public void addListener(SocketListener listener) {
        mListeners.add(listener);
    }

    @Override
    public void run() {
        while (!mStopped) {
            try {
                Log.e(TAG, "Attempt to connect to " + mHost + ":" + mPort);
                socket = new Socket(mHost, mPort);
                mInputStream = socket.getInputStream();
                mOutputStream = socket.getOutputStream();

                inThread = new Thread(new inboundConnection());
                inThread.setName("SocketConnectionHandler inbound connection thread");

                mOutThread = new Thread(new outboundConnection());
                mOutThread.setName("SocketConnectionHandler outbound connection thread");

                inThread.start();
                mOutThread.start();

                for (SocketListener listener : mListeners) {
                    listener.onConnected();
                }
                break;
            } catch (IOException e) {
                try {
                    Thread.sleep(RECONNECT_DELAY);
                } catch (InterruptedException ex) {
                    // Ignore
                }
            }
        }

        if (mStopped) {
            return;
        }
    }

    @Override
    public void stop() {
        mOutThread.interrupt();
        inThread.interrupt();
        Thread.currentThread().interrupt();
        mStopped = true;

        if (socket != null && !socket.isClosed()) {
            try {
                socket.close();
            } catch (IOException e) {
                // Ignore
            }
        }
    }

    private class inboundConnection implements Runnable {
        public void run() {
            final byte[] buf = new byte[1024 * 64];
            while (!inThread.isInterrupted()) {
                try {
                    int read = mInputStream.read(buf);
                    if (read > 0) {
                        String data = new String(Arrays.copyOf(buf, read), "UTF-8");
                        String[] dataStr = data.split("\\}\\{");
                        for (SocketListener listener : mListeners) {
                            if (dataStr.length == 1) {
                                listener.onDataReceived(dataStr[0]);
                            } else {
                                for (int i = 0; i < dataStr.length; i++) {
                                    if (i % 2 == 0) {
                                        listener.onDataReceived(dataStr[i] + "}");
                                    } else {
                                        listener.onDataReceived("{" + dataStr[i]);
                                    }
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Failed to handle inboundConnection: " + e.getMessage());
                    for (SocketListener listener : mListeners) {
                        listener.onConnectionFailed();
                    }
                    mOutThread.interrupt();
                    inThread.interrupt();
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    private class outboundConnection implements Runnable {
        public void run() {
            while (!mOutThread.isInterrupted()) {
                try {
                    String data = outboundDataQueue.take();
                    if (data != null) {
                        mOutputStream.write(data.getBytes("UTF-8"));
                        mOutputStream.flush();
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Failed to handle outboundConnection: " + e.getMessage());
                    for (SocketListener listener : mListeners) {
                        listener.onConnectionFailed();
                    }
                    mOutThread.interrupt();
                    inThread.interrupt();
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
}