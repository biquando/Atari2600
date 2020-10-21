package Atari2600.MOS6502.AddressingModes.Absolute.AbsoluteIndexed;

import Atari2600.MOS6502.AddressingModes.Absolute.Absolute;
import Atari2600.MOS6502.CPU;

/**
 * The value in X/Y is added to the specified address for a sum address.
 * The value at the sum address is used to perform the computation.
 */
public class AbsoluteIndexed extends Absolute {
    /**
     * 0: value
     * 1: addr0
     * 2: addr1
     * 3: pageChange ? 1 : 0
     */
    public byte[] fetch(CPU cpu, byte offset) {
        byte[] addr = super.fetch(cpu);
        byte newAddr0 = (byte)(addr[0] + offset);
        cpu.step();

        // Cycle 4
        byte value = cpu.mem.read(newAddr0, addr[1]);
        boolean pageChange = false;
        if (offset < 0 && addr[0] > 0 && newAddr0 < 0) {
            addr[1]--; // underflow
            pageChange = true;
        } else if (offset > 0 && addr[0] < 0 && newAddr0 > 0) {
            addr[1]++; // overflow
            pageChange = true;
        }
        addr[0] = newAddr0;
        return new byte[] { value, addr[0], addr[1], (byte)(pageChange ? 1 : 0)};
    }
}
