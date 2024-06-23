package VNCClient.VNCClient.protocol.initialization;

import VNCClient.VNCClient.client.VNCConfig;
import VNCClient.VNCClient.client.VNCSession;
import VNCClient.VNCClient.client.rendering.ColorDepth;
import VNCClient.VNCClient.protocol.messages.*;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import static VNCClient.VNCClient.protocol.messages.Encoding.*;


public class Initializer {

    public void initialise(VNCSession session) throws IOException {
        OutputStream out = session.getOutputStream();
        VNCConfig config = session.getConfig();

        // Send client initialization message
        new ClientInit(config.isShared()).encode(out);

        // Receive and set server initialization details
        ServerInit serverInit = ServerInit.decode(session.getInputStream());
        session.setServerInit(serverInit);
        session.setFramebufferWidth(serverInit.getFramebufferWidth());
        session.setFramebufferHeight(serverInit.getFramebufferHeight());

        // Get and set pixel format
        PixelFormat pixelFormat = getPixelFormat(config);
        session.setPixelFormat(pixelFormat);

        // Configure and send pixel format
        new SetPixelFormat(pixelFormat).encode(out);

        // Configure and send encodings
        new SetEncodings(getEncodings(config)).encode(out);
    }

    private PixelFormat getPixelFormat(VNCConfig config) {
        ColorDepth colorDepth = config.getColorDepth();
        return new PixelFormat(
                colorDepth.getBitsPerPixel(),
                colorDepth.getDepth(),
                true,
                colorDepth.isTrueColor(),
                colorDepth.getRedMax(),
                colorDepth.getGreenMax(),
                colorDepth.getBlueMax(),
                colorDepth.getRedShift(),
                colorDepth.getGreenShift(),
                colorDepth.getBlueShift());
    }

    private List<Encoding> getEncodings(VNCConfig config) {
        List<Encoding> encodings = new ArrayList<>();

        if (config.isEnableZLibEncoding()) {
            encodings.add(ZLIB);
        }
        if (config.isEnableHextileEncoding()) {
            encodings.add(HEXTILE);
        }
        if (config.isEnableRreEncoding()) {
            encodings.add(RRE);
        }
        if (config.isEnableCopyRectEncoding()) {
            encodings.add(COPYRECT);
        }
        if (config.isEnableExtendedClipboard()) {
            encodings.add(EXTENDED_CLIPBOARD);
        }
        if (config.isUseLocalMousePointer()) {
            encodings.add(CURSOR);
        }

        encodings.add(RAW);
        encodings.add(DESKTOP_SIZE);

        return encodings;
    }

}
