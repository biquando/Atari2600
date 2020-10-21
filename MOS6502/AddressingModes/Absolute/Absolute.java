package Atari2600.MOS6502.AddressingModes.Absolute;

import Atari2600.MOS6502.AddressingMode;
import Atari2600.MOS6502.CPU;

/**
 * A full 16-bit address is specified and the byte at that address
 * is used to perform the computation.
 */
public class Absolute implements AddressingMode {
    /**
     * 0: addr0
     * 1: addr1
     */
    @Override
    public byte[] fetch(CPU cpu) {
        byte[] data = new byte[2];

        // Cycle 2
        data[0] = cpu.mem.read(cpu.getPCL(), cpu.getPCH());
        cpu.incrementPC();
        cpu.step();

        // Cycle 3
        data[1] = cpu.mem.read(cpu.getPCL(), cpu.getPCH());
        cpu.incrementPC();
        return data;
    }
}
