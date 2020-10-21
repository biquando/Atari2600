package Atari2600.Memory;

/**
 * Basic section of memory to store working data.
 * Stored between 0x80 and 0xFF (128 bytes).
 */
class RAM extends MemSection {
    public RAM() {
        super(0x80, 128);
    }
}
