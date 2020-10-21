package Atari2600.Memory;

import Atari2600.TV.CRT;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * Consolidates all memory sections into a single object.
 */
public class Memory {

    public TIA tia;
    private RAM ram;
    private Stack stack;
    private RIOT riot;
    private ROM rom;

    public Memory() {
        tia = new TIA();
        stack = new Stack();
        ram = new RAM();
        riot = new RIOT();
    }

    /**
     * Initializes ROM with some data.
     */
    public void loadCartridge(byte[] data) {
        rom = new ROM(data);
    }

    public void loadCartridge(File file) {
        try {
            loadCartridge(Files.readAllBytes(file.toPath()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Connects the TIA to a CRT.
     */
    public void connectCRT(CRT crt) {
        tia.use(crt);
    }

    /**
     * Writes data to a 2-byte little-endian address.
     * (ex. if addr0 = 0xF7 and addr1 = 0x82, then addr = 0x82F7)
     *
     * @param data byte to be written
     * @param addr0 the least significant byte in the address
     * @param addr1 the most significant byte in the address
     */
    public void write(byte data, byte addr0, byte addr1) {
        MemSection sec = getSection(addr0, addr1);
        if (sec != null) {
            sec.write(data, addr0, addr1);
        }
    }

    /**
     * Reads data from a 2-byte little-endian address.
     * (ex. if addr0 = 0xF7 and addr1 = 0x82, then addr = 0x82F7)
     *
     * @param addr0 the least significant byte in the address
     * @param addr1 the most significant byte in the address
     * @return byte stored at addr
     */
    public byte read(byte addr0, byte addr1) {
        MemSection sec = getSection(addr0, addr1);
        if (sec != null) {
            return sec.read(addr0, addr1);
        }
        return 0;
    }

    /**
     * Returns the memory component that contains
     * the specified little-endian address.
     * (ex. if addr0 = 0xF7 and addr1 = 0x82, then addr = 0x82F7)
     */
    private MemSection getSection(byte addr0, byte addr1) {
        int addr = ((int)addr0 & 0xff) | ((int)addr1 & 0xff) << 8;

        if (addr < 0x80) {
            return tia;

        } else if (addr < 0x100) {
            return ram;

        } else if (addr < 0x200) {
            return stack;

        } else if (addr < 0x300) {
            return riot;

        } else if (addr >= 0xF000 && addr < 0x10000) {
            return rom;
        }

        return null;
    }

}
