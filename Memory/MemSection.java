package Atari2600.Memory;

/**
 * Contains an array of bytes to represent a section of memory.
 */
abstract class MemSection {

    protected byte[] data;
    private int offset;

    public MemSection(int offset, int size) {
        data = new byte[size];
        this.offset = offset;
    }

    /**
     * Writes data to a zero page, 1-byte address (256 possible addresses).
     * In the Atari2600, this encompasses the TIA registers [0x00–0x7F]
     * and RAM [0x80–0xFF].
     *
     * @param data byte to be written
     * @param addr address where data should be written [0x00–0xFF]
     */
    public void write(byte data, byte addr) {
        this.data[(int)addr & 0xff - offset] = data;
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
        if (addr1 == 0) {
            write(data, addr0);
            return;
        }

        this.data[(((int) addr0 & 0xff)
                | ((int) addr1 & 0xff) << 8) - offset] = data;
    }

    /**
     * Reads data from the zero page (1-byte address).
     * In the Atari2600, this encompasses the TIA registers and RAM (128 bytes each).
     *
     * @param addr where to read data from
     * @return byte stored at [addr - offset]
     */
    public byte read(byte addr) {
        return data[(int)addr & 0xff - offset];
    }

    /**
     * Reads data from a 2-byte little-endian address.
     * (ex. if addr0 = 0xF7 and addr1 = 0x82, then addr = 0x82F7)
     *
     * @param addr0 the least significant byte in the address
     * @param addr1 the most significant byte in the address
     * @return byte stored at [addr - offset]
     */
    public byte read(byte addr0, byte addr1) {
        if (addr1 == 0) {
            return read(addr0);
        }

        return data[(((int)addr0 & 0xff)
                  | ((int)addr1 & 0xff) << 8) - offset];
    }

    /**
     * Writes an entire array of data at once.
     *
     * @param newData these bytes are copied into the data array
     */
    public void setData(byte[] newData) {
        for (int i = 0; i < newData.length && i < this.data.length; i++) {
            this.data[i] = newData[i];
        }
    }
}
