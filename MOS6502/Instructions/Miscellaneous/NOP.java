package Atari2600.MOS6502.Instructions.Miscellaneous;

import Atari2600.MOS6502.CPU;
import Atari2600.MOS6502.Instruction;

/**
 * No operation.
 */
public class NOP implements Instruction {
    /**
     * Do nothing.
     * @param cpu processor that executes the instruction
     * @param data empty byte[]
     */
    @Override
    public void execute(CPU cpu, byte[] data) {
        cpu.end();
    }

    @Override
    public String toString() {
        return "NOP";
    }
}
