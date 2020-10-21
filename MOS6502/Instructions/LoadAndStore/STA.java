package Atari2600.MOS6502.Instructions.LoadAndStore;

import Atari2600.MOS6502.CPU;
import Atari2600.MOS6502.Instruction;

/**
 * Store accumulator in memory (write).
 */
public class STA implements Instruction {
    /**
     * A -> M
     * Flags: none
     * @param cpu processor that executes the instruction
     * @param data addr0, addr1
     */
    @Override
    public void execute(CPU cpu, byte[] data) {
        cpu.mem.write(cpu.getA(), data[0], data[1]);
        cpu.end();
    }

    @Override
    public String toString() {
        return "STA";
    }
}
