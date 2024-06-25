package VNCClient.VNCClientModule.client;

import VNCClient.VNCClientModule.client.exceptions.BaseVncException;
import VNCClient.VNCClientModule.client.exceptions.UnexpectedVncException;
import VNCClient.VNCClientModule.client.exceptions.UnknownMessageTypeException;
import VNCClient.VNCClientModule.client.rendering.Framebuffer;
import VNCClient.VNCClientModule.protocol.messages.Bell;
import VNCClient.VNCClientModule.protocol.messages.FramebufferUpdate;
import VNCClient.VNCClientModule.protocol.messages.ServerCutText;
import VNCClient.VNCClientModule.protocol.messages.SetColorMapEntries;

import java.io.IOException;
import java.io.PushbackInputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class ServerEventHandler {

    private final VNCSession session;
    private final Consumer<BaseVncException> errorHandler;
    private final Framebuffer framebuffer;
    private final ExecutorService executor;

    private volatile boolean running;

    public ServerEventHandler(VNCSession session, Consumer<BaseVncException> errorHandler) {
        this.session = session;
        this.errorHandler = errorHandler;
        this.framebuffer = new Framebuffer(session);
        this.executor = Executors.newSingleThreadExecutor();
    }

    public void start() {
        PushbackInputStream in = new PushbackInputStream(session.getInputStream());
        running = true;

        executor.submit(() -> {
            try {
                int messageType;
                while (running && (messageType = in.read()) != -1) {
                    in.unread(messageType);
                    handleMessage(messageType, in);
                }
            } catch (IOException | BaseVncException e) {
                if (running) {
                    errorHandler.accept(new UnexpectedVncException(e));
                }
            } finally {
                running = false;
            }
        });
    }

    private void handleMessage(int messageType, PushbackInputStream in) throws IOException, BaseVncException {
        switch (messageType) {
            case 0x00:
                FramebufferUpdate framebufferUpdate = FramebufferUpdate.decode(in);
                framebuffer.processUpdate(framebufferUpdate);
                break;
            case 0x01:
                SetColorMapEntries setColorMapEntries = SetColorMapEntries.decode(in);
                framebuffer.updateColorMap(setColorMapEntries);
                break;
            case 0x02:
                Bell.decode(in);
                Consumer<Void> bellListener = session.getConfig().getBellListener();
                if (bellListener != null) {
                    bellListener.accept(null);
                }
                break;
            case 0x03:
                ServerCutText cutText = ServerCutText.decode(in);
                Consumer<String> cutTextListener = session.getConfig().getRemoteClipboardListener();
                if (cutTextListener != null && !cutText.text().isEmpty()) {
                    cutTextListener.accept(cutText.text());
                }
                break;
            default:
                throw new UnknownMessageTypeException(messageType);
        }
    }

    public void stop() {
        running = false;
        executor.shutdownNow();
        try {
            if (!executor.awaitTermination(1, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException ignored) {
        }
    }
}
