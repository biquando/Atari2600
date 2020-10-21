package Atari2600.MOS6502.Instructions.IncrementAndDecrement;

import Atari2600.MOS6502.CPU;
import Atari2600.MOS6502.Instruction;

/**
 * Increment memory by one (RMW).
 */
public class INC implements Instruction {
    /**
     * M + 1 -> M
     * Flags: N, Z
     * @param cpu processor that executes the instruction
     * @param data value, addr0, addr1
     */
    @Override
    public void execute(CPU cpu, byte[] data) {
        data[0]++;
        cpu.sr.N(data[0]);
        cpu.sr.Z(data[0]);
        cpu.step();

        // Cycle 6
        cpu.mem.write(data[0], data[1], data[2]);
        cpu.end();
    }

    @Override
    public String toString() {
        return "INC";
    }
}
