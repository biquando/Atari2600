package Atari2600.MOS6502.Instructions.Transfer;

import Atari2600.MOS6502.CPU;
import Atari2600.MOS6502.Instruction;

/**
 * Transfer X to stack pointer.
 */
public class TXS implements Instruction {
    /**
     * X -> S
     * flags: none
     * @param cpu processor that executes the instruction
     * @param data empty byte[]
     */
    @Override
    public void execute(CPU cpu, byte[] data) {
        cpu.setSP(cpu.getX());
        cpu.end();
    }

    @Override
    public String toString() {
        return "TXS";
    }
}
