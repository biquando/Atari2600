package Atari2600.MOS6502.Instructions.SetAndClear;

import Atari2600.MOS6502.CPU;
import Atari2600.MOS6502.Instruction;

/**
 * Set decimal flag.
 */
public class SED implements Instruction {
    /**
     * 1 -> D
     * flags: D = 1
     * @param cpu processor that executes the instruction
     * @param data empty byte[]
     */
    @Override
    public void execute(CPU cpu, byte[] data) {
        cpu.sr.setFlag(3, true);
        cpu.end();
    }

    @Override
    public String toString() {
        return "SED";
    }
}
