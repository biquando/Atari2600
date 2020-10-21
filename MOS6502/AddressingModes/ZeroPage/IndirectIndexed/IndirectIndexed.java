package Atari2600.MOS6502.AddressingModes.ZeroPage.IndirectIndexed;

import Atari2600.MOS6502.AddressingModes.ZeroPage.ZeroPage;
import Atari2600.MOS6502.CPU;

/**
 * The value in Y is added to the address at the little-endian address stored at
 * the two-byte pair of the specified address (LSB) and the specified address plus
 * one (MSB). The value at the sum address is used to perform the computation.
 */
public class IndirectIndexed extends ZeroPage {
    /**
     * 0: value
     * 1: addr0
     * 2: addr1
     * 3: pageChange ? 1 : 0
     */
    @Override
    public byte[] fetch(CPU cpu) {
        byte ptrAddr = super.fetch(cpu)[0];
        byte addr0 = cpu.mem.read(ptrAddr, (byte)0);
        cpu.step();

        // Cycle 4
        byte addr1 = cpu.mem.read((byte)(ptrAddr + 1), (byte)0);
        byte newAddr0 = (byte)(addr0 + cpu.getY());
        cpu.step();

        // Cycle 5
        byte value = cpu.mem.read(newAddr0, addr1);
        boolean pageChange = false;
        if (cpu.getY() < 0 && addr0 > 0 && newAddr0 < 0) {
            addr1--; // underflow
            pageChange = true;
        } else if (cpu.getY() > 0 && addr0 < 0 && newAddr0 > 0) {
            addr1++; // overflow
            pageChange = true;
        }
        addr0 = newAddr0;
        return new byte[] { value, addr0, addr1, (byte)(pageChange ? 1 : 0)};
    }
}
