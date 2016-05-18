package ru.mail.ruslan.androidchatclient.net;

import android.util.JsonReader;
import android.util.JsonWriter;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import ru.mail.ruslan.androidchatclient.msg.Message;

public class SocketConnectionHandler implements ConnectionHandler {

    private static final String TAG = "SocketConnectionHandler";

    private List<SocketListener> mListeners = new ArrayList<>();
    private BlockingQueue<Message> outboundMessageQueue;
    private BlockingQueue<Message> inboundMessageQueue;
    private boolean isShutDown;
    private Socket socket;
    private InputStream mInputStream;
    private OutputStream mOutputStream;
    private Thread outThread;
    private Thread inThread;

    SocketConnectionHandler(String host, int port) throws UnknownHostException, IOException {
        Log.d(TAG, "constructor started");
        isShutDown = false;
        outboundMessageQueue = new LinkedBlockingQueue<Message>();
        inboundMessageQueue = new LinkedBlockingQueue<Message>();

        try {
            socket = new Socket(host, port);
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
        } catch (IOException e) {
            for (SocketListener listener : mListeners) {
                listener.onConnectionFailed();
            }
            e.printStackTrace();
        }
        Log.d(TAG, "constructor ended");
    }

    @Override
    public void sendMessage(Message msg) {
        outboundMessageQueue.add(msg);
    }

    @Override
    public void addListener(SocketListener listener) {
        mListeners.add(listener);
    }

    Message receiveMessage() throws InterruptedException {
        return inboundMessageQueue.take();
    }

    boolean isMessageAvailable() {
        return !inboundMessageQueue.isEmpty();
    }

    @Override
    public void start() {
        Log.d(TAG, "start() started");
        synchronized (this) {
            if (isShutDown) {
                throw new IllegalStateException("The SocketConnectionHandler has been shut down");
            }
        }
        inThread.start();
        outThread.start();
        Log.d(TAG, "start() ended");
        /*
        try {
            while (true) {
                if (!isMessageAvailable()) {
                    Message msg = receiveMessage();
                    // TODO Узнать, правильно ли так делать
                    for (SocketListener listener : mListeners) {
                        listener.onDataReceived(msg);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        */
    }

    @Override
    public void stop() {
        Log.d(TAG, "stop() started");
        synchronized (this) {
            isShutDown = true;
        }
        outThread.interrupt();
        inThread.interrupt();

        if (socket != null && !socket.isClosed()) {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Log.d(TAG, "stop() ended");
    }

    private class inboundConnection implements Runnable {

        public void run() {
            while (!inThread.isInterrupted()) {
                JsonReader in = new JsonReader(new InputStreamReader(mInputStream));
                // TODO считать message из in (decode)
                // inboundMessageQueue.add(message);
                for (SocketListener listener : mListeners) {
                    //listener.onDataReceived(msg);
                }
            }
        }
    }

    private class outboundConnection implements Runnable {

        public void run() {

            while (!outThread.isInterrupted()) {
                Message message = null;
                try {
                    message = outboundMessageQueue.take();
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                }
                if (message != null) {
                    JsonWriter out = new JsonWriter(new OutputStreamWriter(mOutputStream));
                    // TODO Записать message в out (encode)
                }
            }
        }
    }


}