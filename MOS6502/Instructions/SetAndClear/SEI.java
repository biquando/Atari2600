package Atari2600.MOS6502.Instructions.SetAndClear;

import Atari2600.MOS6502.CPU;
import Atari2600.MOS6502.Instruction;

/**
 * Set interrupt disable status.
 */
public class SEI implements Instruction {
    /**
     * 1 -> I
     * flags: I = 1
     * @param cpu processor that executes the instruction
     * @param data empty byte[]
     */
    @Override
    public void execute(CPU cpu, byte[] data) {
        cpu.sr.setFlag(2, true);
        cpu.end();
    }

    @Override
    public String toString() {
        return "SEI";
    }
}
