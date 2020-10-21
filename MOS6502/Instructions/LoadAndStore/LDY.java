package Atari2600.MOS6502.Instructions.LoadAndStore;

import Atari2600.MOS6502.CPU;
import Atari2600.MOS6502.Instruction;

/**
 * Load Y with memory (read).
 */
public class LDY implements Instruction {
    /**
     * M -> Y
     * Flags: N, Z
     * @param cpu processor that executes the instruction
     * @param data value
     */
    @Override
    public void execute(CPU cpu, byte[] data) {
        cpu.setY(data[0]);
        cpu.sr.N(data[0]);
        cpu.sr.Z(data[0]);
        cpu.end();
    }

    @Override
    public String toString() {
        return "LDY";
    }
}
