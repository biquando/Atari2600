package Atari2600.MOS6502.AddressingModes.ZeroPage.ZeroPageIndexed;

import Atari2600.MOS6502.AddressingModes.ZeroPage.ZeroPage;
import Atari2600.MOS6502.CPU;

/**
 * The value in X/Y is added to the specified zero page address for a sum address.
 * The value at the sum address is used to perform the computation.
 */
public class ZeroPageIndexed extends ZeroPage {
    /**
     * 0: value
     * 1: addr0
     * 2: addr1 = 0
     */
    public byte[] fetch(CPU cpu, byte offset) {
        byte[] data = new byte[3];
        data[1] = super.fetch(cpu)[0];
        data[0] = cpu.mem.read(data[1], data[2]);
        data[1] += offset;
        cpu.step();

        // Cycle 4
        return data;
    }
}
