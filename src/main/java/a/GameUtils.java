package a;

import a.a.C16;
import c.IComponent;
import c.DialogConfig;
import game.C4;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.InputStream;
import java.util.Random;
import java.util.Vector;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import me.kitakeyos.ManagedInputStream;

public final class GameUtils {
    private static int DEFAULT_VALUE = 5;
    private static int MAX_SIZE = 300;
    private static int[] precomputedValues = null;
    private static Random randomGenerator;
    private static int[][] chunkInfo = new int[20][2];
    private static int[] C26_f381;
    private static int[] colors = new int[]{1862801, 15673612, 12067264, 11689977, 16359727, 16711680, 255};
    private static char[] punctuationMarks = new char[]{'，', '。', '？', '！', '：', '；', '’', '”', '、'};
    private static int currentTextPage = 0;
    public static String[] textChunks;
    private static int displayTextStart = 0;
    public static int pageCount = 1;
    private static int totalTextPages = 0;

    public static short[] concatenateShortArrays(short[] var0, short[] var1) {
        int var2 = 0;
        if (var0 != null) {
            var2 = var0.length;
        }

        short[] var3 = new short[var2 + var1.length];
        if (var0 != null) {
            System.arraycopy(var0, 0, var3, 0, var0.length);
        }

        System.arraycopy(var1, 0, var3, var2, var1.length);
        return var3;
    }

    public static void loadStreamToByteArray(byte[] var0, String var1, int var2) {
        try {
            InputStream var4;
            if (var1.substring(0, 1).endsWith("/")) {
                var4 = ManagedInputStream.openStream(var1);
            } else {
                var4 = ManagedInputStream.openStream("/" + var1);
            }

            DataInputStream var5;
            (var5 = new DataInputStream(var4)).skip(0L);
            var5.read(var0, 0, var0.length);
            var5.close();
        } catch (Exception var3) {
            System.out.println("GameInf err at getStream()  :  " + var3);
        }
    }

    public static short[] readShortArray(byte[] var0, int[] var1) {
        short var2 = readShort(var0, var1);
        short var3 = readShort(var0, var1);
        if (var2 == 0) {
            return null;
        } else {
            short[] var6 = new short[var2 * var3];

            for (int var7 = 0; var7 < var6.length; ++var7) {
                byte var4 = var0[var1[0]];
                var1[0]++;
                byte var5 = var0[var1[0]];
                var1[0]++;
                var6[var7] = (short) (var4 << 8 | var5 & 255);
            }

            return var6;
        }
    }

    public static short[][] readShortMatrix(byte[] var0, int[] var1) {
        short var3 = readShort(var0, var1);
        short var4 = readShort(var0, var1);
        if (var3 == 0) {
            return null;
        } else {
            short[][] var2 = new short[var3][];

            for (int var5 = 0; var5 < var3; ++var5) {
                short var6 = readShort(var0, var1);
                var2[var5] = new short[var6 * var4];

                for (int var9 = 0; var9 < var2[var5].length; ++var9) {
                    byte var7 = var0[var1[0]];
                    var1[0]++;
                    byte var8 = var0[var1[0]];
                    var1[0]++;
                    var2[var5][var9] = (short) (var7 << 8 | var8 & 255);
                }
            }

            return var2;
        }
    }

    private static short readShort(byte[] var0, int[] var1) {
        return readShortFromBytes(var0, var1);
    }

    public static InputStream openInputStream(String var0) {
        try {
            return ManagedInputStream.openStream(var0);
        } catch (Exception var1) {
            return null;
        }
    }

    public static short[][] readShortMatrix(InputStream inputStream) {
        short[][] var1 = null;
        if (inputStream == null) {
            return null;
        } else {
            try {
                DataInputStream var2 = new DataInputStream(inputStream);
                short var5 = (var2).readShort();
                if (var5 >= 0) {
                    var1 = new short[var5][];
                }

                for (int var3 = 0; var3 < var1.length; ++var3) {
                    var5 = var2.readShort();
                    var1[var3] = new short[var5];

                    for (int var6 = 0; var6 < var1[var3].length; ++var6) {
                        var1[var3][var6] = var2.readShort();
                    }
                }
            } catch (Exception var4) {
            }

            return var1;
        }
    }

    public static byte[][] readByteMatrix(InputStream var0) {
        byte[][] var1 = null;
        if (var0 == null) {
            return null;
        } else {
            try {
                DataInputStream var2;
                short var5;
                if ((var5 = (var2 = new DataInputStream(var0)).readShort()) >= 0) {
                    var1 = new byte[var5][];
                }

                for (int var3 = 0; var3 < var1.length; ++var3) {
                    var5 = var2.readShort();
                    var1[var3] = new byte[var5];

                    for (int var6 = 0; var6 < var1[var3].length; ++var6) {
                        var1[var3][var6] = var2.readByte();
                    }
                }
            } catch (Exception var4) {
            }

            return var1;
        }
    }

    public static String[][] readStringMatrix(InputStream var0) {
        String[][] var1 = null;
        StringBuilder var2 = new StringBuilder();

        try {
            short var3;
            DataInputStream var8;
            if ((var3 = (var8 = new DataInputStream(var0)).readShort()) == 0) {
                return null;
            }

            if (var3 > 0) {
                var1 = new String[var3][];
            }

            for (int var4 = 0; var4 < var1.length; ++var4) {
                var3 = var8.readShort();
                var1[var4] = new String[var3];

                for (int var9 = 0; var9 < var1[var4].length; ++var9) {
                    int var5;
                    if ((var5 = var8.readUnsignedByte()) == 255) {
                        var5 = var8.readShort();
                    }

                    for (int var6 = 0; var6 < var5; ++var6) {
                        var2.append((char) var8.readShort());
                    }

                    var1[var4][var9] = var2.toString();
                    var2 = new StringBuilder();
                }
            }
        } catch (Exception var7) {
            var7.printStackTrace();
        }

        return var1;
    }

    public static int calculateEuclideanDistance(int var0, int var1, int var2, int var3) {
        var0 = (var0 - var2) * (var0 - var2) + (var1 - var3) * (var1 - var3);
        var1 = 0;

        for (var2 = 1073741824; var2 > 0; var2 >>= 2) {
            if (var0 >= var1 + var2) {
                var0 -= var1 + var2;
                var1 = (var1 >> 1) + var2;
            } else {
                var1 >>= 1;
            }
        }

        return var1;
    }

    public static int fastSqrt(int var0, int var1) {
        int var2;
        int var3;
        if (precomputedValues == null) {
            precomputedValues = new int[MAX_SIZE / DEFAULT_VALUE + 1];
            var2 = DEFAULT_VALUE * DEFAULT_VALUE;

            for (var3 = precomputedValues.length - 1; var3 >= 0; --var3) {
                precomputedValues[var3] = var3 * var3 * var2;
            }
        }

        if (var0 < 0) {
            return -1;
        } else {
            if (var0 <= precomputedValues[30]) {
                if (var0 <= precomputedValues[15]) {
                    if (var0 <= precomputedValues[5]) {
                        for (var2 = 1; var2 <= 5 && var0 > precomputedValues[var2]; ++var2) {
                        }
                    } else if (var0 <= precomputedValues[10]) {
                        for (var2 = 6; var2 <= 10 && var0 > precomputedValues[var2]; ++var2) {
                        }
                    } else {
                        for (var2 = 11; var2 <= 15 && var0 > precomputedValues[var2]; ++var2) {
                        }
                    }
                } else if (var0 <= precomputedValues[20]) {
                    for (var2 = 16; var2 <= 20 && var0 > precomputedValues[var2]; ++var2) {
                    }
                } else if (var0 <= precomputedValues[25]) {
                    for (var2 = 21; var2 <= 25 && var0 > precomputedValues[var2]; ++var2) {
                    }
                } else {
                    for (var2 = 26; var2 <= 30 && var0 > precomputedValues[var2]; ++var2) {
                    }
                }
            } else if (var0 <= precomputedValues[45]) {
                if (var0 <= precomputedValues[35]) {
                    for (var2 = 31; var2 <= 35 && var0 > precomputedValues[var2]; ++var2) {
                    }
                } else if (var0 <= precomputedValues[40]) {
                    for (var2 = 36; var2 <= 40 && var0 > precomputedValues[var2]; ++var2) {
                    }
                } else {
                    for (var2 = 41; var2 <= 45 && var0 > precomputedValues[var2]; ++var2) {
                    }
                }
            } else if (var0 <= precomputedValues[50]) {
                for (var2 = 46; var2 <= 50 && var0 > precomputedValues[var2]; ++var2) {
                }
            } else if (var0 <= precomputedValues[55]) {
                for (var2 = 51; var2 <= 55 && var0 > precomputedValues[var2]; ++var2) {
                }
            } else {
                for (var2 = 56; var2 <= 60 && var0 > precomputedValues[var2]; ++var2) {
                }
            }

            for (var3 = (var2 *= DEFAULT_VALUE) - DEFAULT_VALUE + 1; var3 <= var2; ++var3) {
                if (var3 * var3 >= var0) {
                    if (var1 == 0) {
                        return var3;
                    }

                    if (var1 == 1) {
                        return var3 - 1;
                    }
                }
            }

            return -1;
        }
    }

    public static int getRandomInt(int var0) {
        if (randomGenerator == null) {
            randomGenerator = new Random(System.currentTimeMillis());
        }

        return randomGenerator.nextInt(var0);
    }

    public static int getRandomOffset(int var0) {
        if (randomGenerator == null) {
            randomGenerator = new Random(System.currentTimeMillis());
        }

        return -2 + randomGenerator.nextInt(4);
    }

    public static int getRandomInRange(int var0, int var1) {
        if (randomGenerator == null) {
            randomGenerator = new Random(System.currentTimeMillis());
        }

        return (randomGenerator.nextInt() >>> 1) % (var1 - var0 + 1) + var0;
    }

    public static int findFirstGreaterOrEqual(int[] var0, int var1) {
        int var2;
        for (var2 = 0; var2 < var0.length && var1 < var0[var2]; ++var2) {
        }

        return var2;
    }

    public static boolean checkCollision(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8) {
        if (var8 % 2 != 0) {
            var8 = var2;
            var2 = var3;
            var3 = var8;
        }

        return var0 + var2 >= var4 && var4 + var6 >= var0 && var1 + var3 > var5 && var5 + var7 > var1;
    }

    public static boolean isPointInRectangle(int var0, int var1, int var2, int var3, int var4, int var5) {
        return var0 >= var2 && var0 <= var2 + var4 && var1 >= var3 && var1 <= var3 + 40;
    }

    public static boolean checkCollisionBetweenShortArrays(int var0, int var1, int var2, int var3, short[] var4, short[] var5) {
        return var0 + var4[0] + var4[2] >= var2 + var5[0] && var0 + var4[0] <= var2 + var5[0] + var5[2] && var1 + var4[1] <= var3 + var5[1] + var5[3] && var1 + var4[1] + var4[3] >= var3 + var5[1];
    }

    public static boolean checkCollisionWithShortArray(int var0, int var1, int var2, int var3, int var4, int var5, short[] var6) {
        return var0 + var2 >= var4 + var6[0] && var0 <= var4 + var6[0] + var6[2] && var1 <= var5 + var6[1] + var6[3] && var1 + var3 >= var5 + var6[1];
    }

    public static boolean isPointInShortArrayRectangle(int var0, int var1, int var2, int var3, short[] var4) {
        return var0 >= var2 + var4[0] && var0 <= var2 + var4[0] + var4[2] && var1 <= var3 + var4[1] + var4[3] && var1 >= var3 + var4[1];
    }

    public static Image loadPNG(String var0, String var1) {
        try {
            return Image.createImage(var0 + var1 + ".png");
        } catch (Exception var3) {
            C16.a(var3, var0 + var1 + ".png error!!");
            return null;
        }
    }

    public static Image loadImage(String basePath, String fileName) {
        byte[] data = loadStream(basePath + fileName + ".mid");
        return Image.createImage(data, 0, data.length);
    }

    private static byte[] processStream(String var0) {
        byte[] var1 = null;

        try {
            int var2 = 0;
            byte var4 = 0;
            InputStream inputStream = ManagedInputStream.openStream(var0);
            DataInputStream dataInputStream = new DataInputStream(inputStream);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            outputStream.write(longToBytes(-1991225785L, 4));
            outputStream.write(longToBytes(218765834L, 4));
            outputStream.write(longToBytes(13L, 4));
            outputStream.write(longToBytes(1229472850L, 4));
            outputStream.write(longToBytes(dataInputStream.readInt(), 4));
            outputStream.write(longToBytes(dataInputStream.readInt(), 4));
            outputStream.write(dataInputStream.readByte());
            byte var10 = dataInputStream.readByte();
            chunkInfo = new int[20][2];
            outputStream.write(var10);
            outputStream.write(longToBytes(0L, 3));
            outputStream.write(longToBytes(0L, 4));
            chunkInfo[0][0] = 8;
            chunkInfo[0][1] = 13;
            int var3 = 8 + chunkInfo[0][1] + 12;
            int var13 = var4 + 1;
            int var8;
            byte[] var11;
            if (var10 != 3) {
                if (var10 == 6) {
                    var2 = dataInputStream.readInt();
                }
            } else {
                var2 = dataInputStream.readInt();
                outputStream.write(longToBytes((long) var2, 4));
                outputStream.write(longToBytes(1347179589L, 4));
                var11 = new byte[var2];

                for (var8 = 0; var8 < var11.length; ++var8) {
                    var11[var8] = (byte) dataInputStream.read();
                }

                outputStream.write(var11);
                outputStream.write(longToBytes(0L, 4));
                chunkInfo[1][0] = var3;
                chunkInfo[1][1] = var2;
                var3 += chunkInfo[1][1] + 12;
                ++var13;
                if ((var2 = dataInputStream.readInt()) != 1951551059) {
                    chunkInfo[2][0] = var3;
                    chunkInfo[2][1] = var2;
                    ++var13;
                } else {
                    int var12;
                    if ((var12 = dataInputStream.read()) == 0) {
                        var12 = 256;
                    }

                    outputStream.write(longToBytes((long) var12, 4));
                    outputStream.write(longToBytes(1951551059L, 4));
                    var8 = dataInputStream.read();

                    for (var2 = 0; var2 < var12; ++var2) {
                        if (var2 == var8) {
                            outputStream.write(0);
                        } else {
                            outputStream.write(255);
                        }
                    }

                    outputStream.write(longToBytes(0L, 4));
                    chunkInfo[2][0] = var3;
                    chunkInfo[2][1] = var12;
                    var3 += chunkInfo[2][1] + 12;
                    ++var13;
                    var2 = dataInputStream.readInt();
                }
            }

            outputStream.write(longToBytes(var2, 4));
            outputStream.write(longToBytes(1229209940L, 4));
            var11 = new byte[var2];

            for (var8 = 0; var8 < var11.length; ++var8) {
                var11[var8] = (byte) dataInputStream.read();
            }

            outputStream.write(var11);
            outputStream.write(longToBytes(0L, 4));
            chunkInfo[var13][0] = var3;
            chunkInfo[var13][1] = var2;
            ++var13;
            outputStream.write(longToBytes(0L, 4));
            outputStream.write(longToBytes(1229278788L, 4));
            outputStream.write(longToBytes(-1371381630L, 4));
            var1 = outputStream.toByteArray();
            inputStream.close();
            outputStream.close();

            for (var8 = 0; var8 < var13; ++var8) {
                int var10001 = chunkInfo[var8][0];
                var3 = chunkInfo[var8][1];
                var2 = var10001;
                var10001 = var2 + 4;
                int var15 = var3 + 4;
                int var14 = ~ab(var1, var10001, var15);
                var1[var2 + 8 + var3] = (byte) (var14 >> 24);
                var1[var2 + 8 + var3 + 1] = (byte) (var14 >> 16);
                var1[var2 + 8 + var3 + 2] = (byte) (var14 >> 8);
                var1[var2 + 8 + var3 + 3] = (byte) var14;
            }
        } catch (Exception var9) {
        }

        return var1;
    }

    private static byte[] longToBytes(long value, int byteCount) {
        byte[] bytes = new byte[byteCount];
        int currentByteIndex = byteCount - 1;

        for (int i = 0; currentByteIndex >= 0; i++) {
            bytes[i] = (byte) ((value >> (currentByteIndex * 8)) & 0xFF);
            currentByteIndex--;
        }

        return bytes;
    }

    private static int ab(byte[] var0, int var1, int var2) {
        int var3 = -1;
        int var4;
        if (C26_f381 == null) {
            C26_f381 = new int[256];

            for (int var5 = 0; var5 < 256; ++var5) {
                var4 = var5;

                for (int var6 = 0; var6 < 8; ++var6) {
                    if ((var4 & 1) == 1) {
                        var4 = -306674912 ^ var4 >>> 1;
                    } else {
                        var4 >>>= 1;
                    }
                }

                C26_f381[var5] = var4;
            }
        }

        for (var4 = var1; var4 < var2 + var1; ++var4) {
            var3 = C26_f381[(var3 ^ var0[var4]) & 255] ^ var3 >>> 8;
        }

        return var3;
    }

    public static int[] extractImageRGB(Image var0) {
        int var1 = var0.getWidth();
        int var2 = var0.getHeight();
        int[] var3 = new int[var1 * var2];
        var0.getRGB(var3, 0, var1, 0, 0, var1, var2);
        return var3;
    }

    public static Image createRGBImage(int[] rgb, int width, int height, boolean processAlpha) {
        return Image.createRGBImage(rgb, width, height, processAlpha);
    }

    public static boolean a() {
        return false;
    }

    public static int findFirstAvailableSlot(int[] var0) {
        int var1 = 0;

        for (int var2 = 0; var2 < var0.length; ++var2) {
            if (var0[var2] == -1) {
                var1 = var2;
                break;
            }
        }

        return var1;
    }

    public static IComponent findChildById(IComponent var0, int var1) {
        if (var0.getSelectedComponentId() == var1) {
            return var0;
        } else {
            for (int var2 = 0; var2 < var0.getComponents().length && var0.getComponents()[var2] != null; ++var2) {
                if (var0.getComponents()[var2].getZIndex() == 0) {
                    IComponent var3;
                    if ((var3 = findChildById(var0.getComponents()[var2], var1)) != null) {
                        return var3;
                    }
                } else if (var0.getComponents()[var2].getSelectedComponentId() == var1) {
                    return var0.getComponents()[var2];
                }
            }

            return null;
        }
    }

    public static int[] initializeArrayWithMinusOne(int var0) {
        int[] var1 = new int[var0];

        for (int var2 = 0; var2 < var0; ++var2) {
            var1[var2] = -1;
        }

        return var1;
    }

    public static void insertAtStart(int[] var0, int var1, int var2) {
        if (0 < var0.length) {
            var1 = 0;

            int var3;
            for (var3 = 0; var3 < var0.length; ++var3) {
                if (var0[var3] == -1) {
                    var1 = var3;
                    break;
                }
            }

            for (var3 = var1 + 0 - 1; var3 >= 0; --var3) {
                if (var3 + 0 + 1 < var0.length) {
                    var0[var3 + 1] = var0[var3];
                }
            }

            var0[0] = var2;
        }
    }

    public static void readBytesFromStream(byte[] var0, String var1, int var2) {
        try {
            var2 = 0;

            for (int var3 = 0; var3 < var1.length(); ++var3) {
                if (var1.charAt(var3) != '/') {
                    var2 = var3;
                    break;
                }
            }

            String var7 = "/" + var1.substring(var2);
            (new Object()).getClass();
            InputStream var5 = ManagedInputStream.openStream(var7);
            DataInputStream var6;
            (var6 = new DataInputStream(var5)).skip(0L);
            var6.read(var0, 0, var0.length);
            var6.close();
        } catch (Exception var4) {
            System.out.println("GameInf err at getStream()  :  " + var4);
        }
    }

    public static byte readByte(byte[] var0, int[] var1) {
        int var10004 = var1[0];
        int var10001 = var1[0];
        var1[0] = var10004 + 1;
        return (byte) var0[var10001];
    }

    public static short readShortFromBytes(byte[] var0, int[] var1) {
        int var10004 = var1[0];
        int var10001 = var1[0];
        var1[0] = var10004 + 1;
        int var10000 = (var0[var10001] & 255) << 8;
        int var10005 = var1[0];
        int var10002 = var1[0];
        var1[0] = var10005 + 1;
        return (short) (var10000 | var0[var10002] & 255);
    }

    public static int e(byte[] var0, int[] var1) {
        int var10004 = var1[0];
        int var10001 = var1[0];
        var1[0] = var10004 + 1;
        int var10000 = (var0[var10001] & 255) << 24;
        int var10005 = var1[0];
        int var10002 = var1[0];
        var1[0] = var10005 + 1;
        var10000 |= (var0[var10002] & 255) << 16;
        var10005 = var1[0];
        var10002 = var1[0];
        var1[0] = var10005 + 1;
        var10000 |= (var0[var10002] & 255) << 8;
        var10005 = var1[0];
        var10002 = var1[0];
        var1[0] = var10005 + 1;
        return var10000 | var0[var10002] & 255;
    }

    public static String a(byte[] var0) {
        char[] var1 = new char[var0.length >> 1];

        for (int var2 = 0; var2 < var0.length; var2 += 2) {
            var1[var2 >> 1] = (char) (var0[var2] << 8 | var0[var2 + 1] & 255);
        }

        return new String(var1);
    }

    public static void a(IComponent var0, int var1, int var2, IComponent var3) {
        var0.setOffsetX(var0.getOffsetX() + var1, var3);
        var0.setOffsetY(var0.getOffsetY() + var2, var3);
        if (var0.getZIndex() != 1 && var0.getZIndex() == 0) {
            for (int var4 = 0; var4 < var0.getComponents().length && var0.getComponents()[var4] != null; ++var4) {
                a(var0.getComponents()[var4], var1, var2, var3);
            }
        }

    }

    public static void a(Graphics var0, String var1, int var2, int var3, int var4, int var5, int var6, int var7, Font var8, boolean var9, int var10, int[] var11, int var12, byte var13, DialogConfig var14, byte var15, boolean[] var16) {
        if (var13 != -1 && var14 != null) {
            var5 = C4.C4_f33;
        }

        int[] var20;
        (var20 = new int[1 + colors.length])[0] = var2;

        for (var2 = 1; var2 < var20.length; ++var2) {
            var20[var2] = colors[var2 - 1];
        }

        C27 var18 = a(var1, var5, var6, var9, var8, var13, var14);
        int var17 = 0;
        if (var18.C27_f392 > 0) {
            if (var9) {
                if (var18.C27_f389[var18.C27_f392 - 1][3] + var5 > var7) {
                    var16[0] = true;
                    if (var18.C27_f389[var18.C27_f392 - 1][3] + var5 > var11[1]) {
                        var11[1] += var12;
                    } else {
                        var11[1] = -var7;
                        var16[1] = true;
                    }
                } else {
                    var16[0] = var16[1] = true;
                    var11[1] = 0;
                }
            } else {
                var17 = var8.stringWidth(var1);
                if (var13 != -1 && var14 != null) {
                    var17 = C4.a(var1);
                }

                if (var17 > var6) {
                    var16[1] = true;
                    if (var17 > var11[0]) {
                        var11[0] += var12;
                        if (var11[0] >= var17 - var6) {
                            var16[0] = true;
                        }
                    } else {
                        var11[0] = -var6;
                    }
                } else {
                    var16[1] = var16[0] = true;
                    var11[0] = 0;
                }
            }
        }

        for (int var19 = 0; var19 < var18.C27_f392; ++var19) {
            if (var9) {
                var0.clipRect(var3, var4, var6, var7);
                a(var0, var1.substring(var18.C27_f389[var19][0], var18.C27_f389[var19][1]), var20[var18.C27_f389[var19][4]], var3 + var18.C27_f389[var19][2], var4 + var18.C27_f389[var19][3] - var11[1], 20, var14);
                var0.clipRect(0, 0, C44.g(), C44.h());
            } else {
                var12 = 0;
                int var21 = 0;
                switch (var10) {
                    case 0:
                        var12 = var3;
                        var21 = var4;
                        break;
                    case 1:
                        var12 = var3 + (var6 - (var18.C27_f391 + var18.C27_f390)) / 2;
                        var21 = var4;
                        break;
                    case 2:
                        var12 = var3 + (var6 - (var18.C27_f391 + var18.C27_f390));
                        var21 = var4;
                        break;
                    case 3:
                        var12 = var3;
                        var21 = var4 + (var7 - var5) / 2;
                        break;
                    case 4:
                        if (var17 > var6) {
                            var12 = var3;
                        } else {
                            var12 = var3 + (var6 - (var18.C27_f391 + var18.C27_f390)) / 2;
                        }

                        var21 = var4 + (var7 - var5) / 2;
                        break;
                    case 5:
                        var12 = var3 + (var6 - (var18.C27_f391 + var18.C27_f390));
                        var21 = var4 + (var7 - var5) / 2;
                        break;
                    case 6:
                        var12 = var3;
                        var21 = var4 + (var7 - var5);
                        break;
                    case 7:
                        if (var17 > var6) {
                            var12 = var3;
                        } else {
                            var12 = var3 + (var6 - (var18.C27_f391 + var18.C27_f390)) / 2;
                        }

                        var21 = var4 + (var7 - var5);
                        break;
                    case 8:
                        var12 = var3 + (var6 - (var18.C27_f391 + var18.C27_f390));
                        var21 = var4 + (var7 - var5);
                }

                var0.clipRect(var3, var4, var6, var7);
                a(var0, var1.substring(var18.C27_f389[var19][0], var18.C27_f389[var19][1]), var20[var18.C27_f389[var19][4]], var12 + var18.C27_f389[var19][2] - var11[0], var21 + var18.C27_f389[var19][3], 20, var14);
                var0.clipRect(0, 0, C44.g(), C44.h());
            }
        }

    }

    private static C27 a(String var0, int var1, int var2, boolean var3, Font var4, byte var5, DialogConfig var6) {
        C27 var7 = new C27();
        int var8 = 0;
        int var9 = 0;
        int var10 = 0;
        int var11 = 0;
        int var12 = var0.length() - 1;
        int var13 = var2 - C4.C4_f38;

        for (int var14 = 0; var14 <= var12; ++var14) {
            int var15;
            char var16;
            if ((var16 = var0.charAt(var14)) == '#' && var14 != var12) {
                if (var0.charAt(var14 + 1) == 'n') {
                    var7.C27_f389[var7.C27_f392++] = new int[]{var8, var14, var9, var10, var11};
                    var9 = 0;
                    var10 += var1 + 1;
                    var8 = var14 + 2;
                } else if (var0.charAt(var14 + 1) >= '0' && var0.charAt(var14 + 1) <= '7') {
                    var7.C27_f391 += var7.C27_f390;
                    var7.C27_f389[var7.C27_f392++] = new int[]{var8, var14, var9, var10, var11};
                    var15 = var4.charsWidth(var0.substring(var8, var14).toCharArray(), 0, var14 - var8);
                    if (var5 != -1 && var6 != null) {
                        var15 = DialogConfig.a(var0.substring(var8, var14), 0, var14 - var8);
                    }

                    var9 += var15;
                    var11 = var0.charAt(var14 + 1) - 48;
                    var8 = var14 + 2;
                }
            } else if (var14 - var8 >= 0) {
                var15 = DialogConfig.a(var0.substring(var8, var14 + 1), 0, var14 - var8 + 1);
                var7.C27_f390 = var15;
                if (var3 && (var9 + var15 >= var2 || var16 == ' ' && var9 + var15 >= var13)) {
                    var7.C27_f389[var7.C27_f392++] = new int[]{var8, var14, var9, var10, var11};
                    var9 = 0;
                    var10 += var1 + 1;
                    var8 = var14;
                }
            }

            if (var14 == var12) {
                if (var14 == var8) {
                    for (var15 = 0; var15 < punctuationMarks.length; ++var15) {
                        if (var0.charAt(var14) == punctuationMarks[var15]) {
                            var7.C27_f389[var7.C27_f392 - 1][1] = var14 + 1;
                            break;
                        }
                    }

                    if (var15 < punctuationMarks.length) {
                        continue;
                    }
                } else if (var14 + 1 - var8 <= 0) {
                    continue;
                }

                var7.C27_f389[var7.C27_f392++] = new int[]{var8, var14 + 1, var9, var10, var11};
            }
        }

        return var7;
    }

    private static void a(Graphics var0, String var1, int var2, int var3, int var4, int var5, DialogConfig var6) {
        DialogConfig.a(var1, var3, var4, 20, var2, var0);
    }

    public static void a(Graphics var0, String var1, int var2, int var3, int var4, int var5, int var6, DialogConfig var7, int var8) {
        switch (var8) {
            case 0:
                if (var7 != null) {
                    DialogConfig.a(var1, var3 - 1, var4, 17, 8607289, var0);
                    DialogConfig.a(var1, var3 + 1, var4, 17, 8607289, var0);
                } else {
                    var0.setColor(8607289);
                    var0.drawString(var1, var3 - 1, var4, 17);
                    var0.drawString(var1, var3 + 1, var4, 17);
                }
                break;
            case 1:
                if (var7 != null) {
                    DialogConfig.a(var1, var3, var4 - 1, 17, 8607289, var0);
                    DialogConfig.a(var1, var3, var4 + 1, 17, 8607289, var0);
                } else {
                    var0.setColor(8607289);
                    var0.drawString(var1, var3, var4 - 1, 17);
                    var0.drawString(var1, var3, var4 + 1, 17);
                }
                break;
            case 2:
                if (var7 != null) {
                    DialogConfig.a(var1, var3, var4 - 1, 17, 8607289, var0);
                    DialogConfig.a(var1, var3, var4 + 1, 17, 8607289, var0);
                    DialogConfig.a(var1, var3 - 1, var4, 17, 8607289, var0);
                    DialogConfig.a(var1, var3 + 1, var4, 17, 8607289, var0);
                } else {
                    var0.setColor(8607289);
                    var0.drawString(var1, var3, var4 - 1, 17);
                    var0.drawString(var1, var3, var4 + 1, 17);
                    var0.drawString(var1, var3 - 1, var4, 17);
                    var0.drawString(var1, var3 + 1, var4, 17);
                }
        }

        if (var7 != null) {
            DialogConfig.a(var1, var3, var4, 17, var2, var0);
        } else {
            var0.setColor(var2);
            var0.drawString(var1, var3, var4, 17);
        }
    }

    public static void a(String var0, int var1, int var2, Font var3, boolean var4, byte var5, DialogConfig var6) {
        C27 var8 = a(var0, var1, var2, true, var3, (byte) -1, var6);
        var2 = var8.C27_f392;
        int[][] var9 = var8.C27_f389;
        var0 = var0;
        pageCount = 1;
        totalTextPages = 0;
        String[] var10 = new String[50];
        int var11 = 0;
        int var12 = var9[0][3];
        int var13 = 0;

        for (int var7 = 0; var7 < var2; ++var7) {
            if (var12 != var9[var7][3]) {
                var12 = var9[var7][3];
                var10[var11++] = var0.substring(var13, var9[var7][0]);
                var13 = var9[var7][0];
            }

            if (var7 == var2 - 1) {
                var10[var11++] = var0.substring(var13, var9[var7][1]);
                break;
            }
        }

        String[] var14 = new String[var11];
        System.arraycopy(var10, 0, var14, 0, var14.length);
        textChunks = var14;
    }

    public static void d(int var0) {
        currentTextPage = var0 / C44.o();
    }

    public static String e(int var0) {
        StringBuffer var1 = new StringBuffer();

        for (int var2 = currentTextPage * totalTextPages; var2 < (var0 * currentTextPage > textChunks.length ? textChunks.length : var0 * currentTextPage); ++var2) {
            var1.append(textChunks[var2]);
        }

        totalTextPages = var0;
        return var1.toString();
    }

    public static int b() {
        return textChunks.length % currentTextPage == 0 ? textChunks.length / currentTextPage : textChunks.length / currentTextPage + 1;
    }

    public static void c() {
        ++pageCount;
    }

    public static String[] a(String var0, char var1) {
        Vector var3 = new Vector();
        int var4 = 0;
        int var5 = 0;

        while (var4 != -1 && (var4 = var0.trim().indexOf(var1, var5)) != -1) {
            String var2 = var0.trim().substring(var5, var4);
            var5 = var4 + 1;
            var3.addElement(var2);
        }

        var3.addElement(var0.substring(var5));
        String[] var6 = new String[var3.size()];
        var3.copyInto(var6);
        return var6;
    }

    public static int b(String var0) {
        return Integer.parseInt(var0);
    }

    public static short c(String var0) {
        return Short.parseShort(var0);
    }

    public static byte d(String var0) {
        return Byte.parseByte(var0);
    }

    public static byte[] e(String var0) {
        String[] var4;
        byte[] var1 = new byte[(var4 = a(var0, ',')).length];

        for (int var2 = 0; var2 < var1.length; ++var2) {
            var1[var2] = Byte.parseByte(var4[var2]);
        }

        return var1;
    }

    public static long[] a(long var0) {
        long[] var2;
        (var2 = new long[3])[0] = var0 / 3600000L;
        var2[1] = var0 / 60000L;
        var2[2] = var0 / 1000L;
        return var2;
    }

    private static byte[] loadStream(String resourcePath) {
        try {
            if (!resourcePath.contains("img_")) {
                return processStream(resourcePath);
            } else {
                byte[] var1 = new byte[1024];
                ByteArrayOutputStream var2 = new ByteArrayOutputStream();
                InputStream var5 = ManagedInputStream.openStream(resourcePath);

                int var3;
                while ((var3 = var5.read(var1)) > 0) {
                    var2.write(var1, 0, var3);
                }

                return var2.toByteArray();
            }
        } catch (Exception var4) {
            var4.printStackTrace();
            return null;
        }
    }
}
