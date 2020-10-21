package Atari2600.MOS6502.Instructions.SetAndClear;

import Atari2600.MOS6502.CPU;
import Atari2600.MOS6502.Instruction;

/**
 * Set carry flag.
 */
public class SEC implements Instruction {
    /**
     * 1 -> C
     * flags: C = 1
     * @param cpu processor that executes the instruction
     * @param data empty byte[]
     */
    @Override
    public void execute(CPU cpu, byte[] data) {
        cpu.sr.setFlag(0, true);
        cpu.end();
    }

    @Override
    public String toString() {
        return "SEC";
    }
}
