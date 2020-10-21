package Atari2600.MOS6502.Instructions.LoadAndStore;

import Atari2600.MOS6502.CPU;
import Atari2600.MOS6502.Instruction;

/**
 * Load accumulator with memory (read).
 */
public class LDA implements Instruction {
    /**
     * M -> A
     * Flags: N, Z
     * @param cpu processor that executes the instruction
     * @param data value
     */
    @Override
    public void execute(CPU cpu, byte[] data) {
        cpu.setA(data[0]);
        cpu.sr.N(data[0]);
        cpu.sr.Z(data[0]);
        cpu.end();
    }

    @Override
    public String toString() {
        return "LDA";
    }
}
