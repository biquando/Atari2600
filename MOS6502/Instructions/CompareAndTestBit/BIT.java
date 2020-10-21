package Atari2600.MOS6502.Instructions.CompareAndTestBit;

import Atari2600.MOS6502.CPU;
import Atari2600.MOS6502.Instruction;

/**
 * Test bits in memory with accumulator (read).
 */
public class BIT implements Instruction {
    /**
     * A & M
     * Flags: N = M7, V = M6, Z
     * @param cpu processor that executes the instruction
     * @param data value
     */
    @Override
    public void execute(CPU cpu, byte[] data) {
        byte res = (byte)(cpu.getA() & data[0]);
        cpu.sr.N(res);
        cpu.sr.V(res);
        cpu.sr.Z(res);
        cpu.end();
    }

    @Override
    public String toString() {
        return "BIT";
    }
}
