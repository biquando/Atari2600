package Atari2600.MOS6502.Instructions.SetAndClear;

import Atari2600.MOS6502.CPU;
import Atari2600.MOS6502.Instruction;

/**
 * Clear overflow flag.
 */
public class CLV implements Instruction {
    /**
     * 0 -> V
     * flags: V = 0
     * @param cpu processor that executes the instruction
     * @param data empty byte[]
     */
    @Override
    public void execute(CPU cpu, byte[] data) {
        cpu.sr.setFlag(6, false);
        cpu.end();
    }

    @Override
    public String toString() {
        return "CLV";
    }
}
