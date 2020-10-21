package Atari2600.MOS6502.Instructions.Transfer;

import Atari2600.MOS6502.CPU;
import Atari2600.MOS6502.Instruction;

/**
 * Transfer Y to accumulator.
 */
public class TYA implements Instruction {
    /**
     * Y -> A
     * flags: N, Z
     * @param cpu processor that executes the instruction
     * @param data empty byte[]
     */
    @Override
    public void execute(CPU cpu, byte[] data) {
        cpu.setA(cpu.getY());
        cpu.sr.N(cpu.getA());
        cpu.sr.Z(cpu.getA());
        cpu.end();
    }

    @Override
    public String toString() {
        return "TYA";
    }
}
