package Atari2600.MOS6502.AddressingModes.ZeroPage.IndexedIndirect;

import Atari2600.MOS6502.AddressingModes.ZeroPage.ZeroPage;
import Atari2600.MOS6502.CPU;

/**
 * The value in X is added to the specified zero page address for a sum address.
 * The little-endian address stored at the two-byte pair of sum address (LSB) and
 * sum address plus one (MSB) is loaded and the value at that address is used to
 * perform the computation.
 */
public class IndexedIndirect extends ZeroPage {
    /**
     * 0: addr0
     * 1: addr1
     */
    @Override
    public byte[] fetch(CPU cpu) {
        byte ptrAddr = super.fetch(cpu)[0];
        ptrAddr += cpu.getX();
        cpu.step();

        // Cycle 4
        byte addr0 = cpu.mem.read(ptrAddr, (byte)0);
        cpu.step();

        // Cycle 5
        byte addr1 = cpu.mem.read((byte)(ptrAddr + 1), (byte)0);
        cpu.step();

        // Cycle 6
        return new byte[] { addr0, addr1 };
    }
}
