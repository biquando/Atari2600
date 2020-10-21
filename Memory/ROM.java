package Atari2600.Memory;

/**
 * Read-only section of memory that stores program data.
 * Between 0x1000 and 0x1FFF (4096 bytes).
 */
class ROM extends MemSection {

    /**
     * Loads a bunch of data into memory, starting at 0x1000.
     * @param data array of bytes to load
     */
    public ROM(byte[] data) {
        super(0xF000, 4096);
        super.setData(data);
    }



    /*************************************************
     * Cannot write to ROM, these methods do nothing.*
     *************************************************/

    @Override
    public void write(byte data, byte addr) {
        return;
    }

    @Override
    public void write(byte data, byte addr0, byte addr1) {
        return;
    }

    @Override
    public void setData(byte[] newData) {
        return;
    }
}
